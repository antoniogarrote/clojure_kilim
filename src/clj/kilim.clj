;; ## Enabling Kilim threads in Clojure

;; [Kilim](http://www.malhar.net/sriram/kilim/) is a framework that can be used to build applications where thousands
;; of lightweight threads known as Kilim tasks are being executed concurrently.
;; This is possible thanks to a bytecode transformation performed by Kilim where a `Fiber` object, containing
;; information about the task stack, is associated to the task, and its instructions are reorder so it can be paused
;; and resumed at a later point.
;; These tasks will be executed in concurrently in a thread pool but they must yield the execution thread explicitly,
;; since Kilim's scheduler cannot execute tasks in a preemptive way.
;; Java methods that can yield the execution thread in a Kilim task, must be declared as `Pausable`, Kilim bytecode
;; weaver will recognise these methods and modify their bytecode.
;;
;; clj-kilim makes possible to declare Clojure functions as pausable functions so they can be transformed by Kilim's
;; weaver and started later as concurrent Kilim tasks.
;;
;; Kilim also include a `Mailbox` object that can be used to exchange without copy messages between Kilim tasks and
;; Java threads as well as a collection of additonal features built on top of these basic objects.
(ns kilim
  (:import [clojure.asm Type]))

;; ## Debug

(defn kilim-debug
  "Turns on/off debug messages from the instrumentation agent. By defult it is turned off."
  [should-debug]
  (clojure.kilim.KilimTransformerAgent/setDebugging should-debug))

;; ## Pausable Tasks


(defmacro pausable
  "Defines a new pausable lambda function.
   If the function is defined without arguments, it can be started as a new Kilim task using
   the function task-start.
   Pausable function can only be invoked by other pausable functions. The function call-pausable
   mut be used for these invocations instead of regular function application."
  [& body]
  (let [nfn (gensym "pausable__fn__")]
    (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str nfn) "-" "_"))
    (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "task")
    `(do
       (def ~nfn (fn ~@body))
       ~nfn)))

(defmacro def-pausable
  "Defines a new named pausable function.
   Check the documentation of pausable for details about the invocation and use of this function."
  [name & rest] (do (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str name) "-" "_"))
                  (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "task")
                  `(defn ~name ~@rest)))

(defmacro call-pausable
  "Invokes the pausable function whose name and arguments are passed as arguments to this function."
  [& rest] `(.invokeTask ~@rest))

(defn task-start
  "Starts the execution of a new Kilim task. A task object is returned that can be used in additional calls
   like task-id to retrieve the task identifier or task-infor-on-exit to associate a mailbox that will be
   notified when the task ends its execution, etc."
  [^kilim.Task task] (.start task))

(defmacro task-id
  "Returns the identifier of a Kilim task being executed."
  [task] `(.id ~task))

(defmacro task-return
  "This function will finish the execution of a Kilim task with the provided exit value.
   This value will be inserted in the mailbox provided in the task-infor-on-exit function if this
   function was invoked.
   This value can be retrieved later using the task-return-value function.
   The exit message will also include the task id information."
  [exit-value] `(kilim.Task/exit ~exit-value))

(defmacro task-return-value
  "Extracts the return value from an exit message returned from a Kilim task using the task-return function."
  [result] `(.result ~result))

(defmacro task-return-id
  "Extracts the task id information from an exit message returned from a Kilim task using the task-return function."
  [result] `(.. ~result task id))

(defn task-inform-on-exit
  "Associates a mailbox to a task that will receive an exit message from the task when it finished its execution.
   If the task invokes the task-return function, that value and task-id will be the content of the message, otherwise
   the 'ok' string and the task id will be inserted by default.
   The function can be invoked many times with different mailboxes and they will all get the exit message."
  [^kilim.Task task ^kilim.Mailbox mbox] (.informOnExit task mbox) task)

(defmacro task-sleep
  "Pauses the execution of a kilim task for the provided number of milliseconds"
  [millisecs] `(kilim.Task/sleep ~millisecs))


;; ## Generators

(defmacro generator
  "Defines a new lambda function as a Kilim generator.
   Generators can be used perform computations in several steps that can be yield and then recovered in other
   Kilim task or Java thread.
   Generator functions must accept a single argument, the instance of the generator being executed.
   Generators must be started using the generator-start function. The generator function can return new
   computed values using the generator-yield function. Other functions can retrieve the next result yielded
   by the generator using the generator-next function."
  [args & body] (let [nfn (gensym "pausable__fn__")
                      orig-g (first args)]
                  (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str nfn) "-" "_"))
                  (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "generator")
                  `(fn []
                     (def ~nfn (fn [g#] (let [~orig-g (cast clojure.lang.AGeneratorFunction g#)] ~@body)))
                     ~nfn)))


(defmacro def-generator
  "Defines a named function as a Kilim generator. Check the documentation of generator for details about
   the invocation and use of this function."
  [name & rest] `(def ~name (generator ~@rest)))

(defmacro generator-start
  "Starts the execution of a new generator, results yielded from this generator can be retrieved passing the
   value returned by this function to the generator-next function."
  [g] `(~g))

(defn generator-seq
  "Wraps a generator in a Clojure's lazy sequence. The single argument of this function must be a generator
   function that will be started by the generator-seq function."
  [f] (letfn [(next-value [g] (let [next (.next g)] (if (nil? next)
                                                      next
                                                      (lazy-cat (list next) (next-value g)))))]
        (let [g (generator-start f)
              first-value (let [v (.next g)] (if (nil? v) v (list v)))]
          (lazy-cat
           first-value
           (next-value g)))))

(defmacro generator-yield
  "Yields a new value as a result of this generator."
  [g data] `(. ~g yield ~data))

(defmacro generator-next
  "Retrieves the next value yielded by this generator. If all values have been yielded, an additional nil value will be
   returned. All additional calls to generator-next will throw an exception."
  [g] `(.next ~g))

;; ## Mailboxes

(defmacro make-mbox
  "Creates a new Kilim mailbox that can be used for inter task or thread/task communication.
   Mailboxes must have only one reader, but they can have multiple writers."
  [] `(kilim.Mailbox.))

(defmacro mbox-get-pausable
  "Tries to retrieve a message from a mailbox, pausing the calling Kilim task if no message is available.
   The function is pausable so it can only be invoked within a pausable function.
   Blocking reception of messages from a Java thread can be accomplished using the mbox-get-blocking function."
  [mbox] `(let [m# (cast kilim.Mailbox ~mbox)]
            (.get m#)))

(defmacro mbox-get-blocking
  "Tries to retrieve a message from a mailbox, pausing the calling Java thread if no message is available.
   The function is not pausable so it can be invoked from regular functions. Kilim task must call the
   pausable version, since invoking this function will block the scheduler thread instead of the task."
  [mbox] `(let [m# (cast kilim.Mailbox ~mbox)]
            (.getb m#)))

(defmacro mbox-get
  "Tries to retrieve a message from a message box without blocking the calling thread or pausing the
   the calling kilim Task.
   It is not a pausable function, so it can be invoked from regular functions.
   It returns the received message, if any available, or nil."
  [mbox] `(let [m# (cast kilim.Mailbox ~mbox)]
            (.getb m#)))

(defn mbox-put
  "Inserts a new message in the selected mailbox and returns immediately without pausing or blocking.
   The function can be invoked safely form pausable and regular functions."
  [^kilim.Mailbox mbox msg] (.putnb mbox msg))

(defmacro mbox-put-pausable
  "Pausable version of the put operation. It pauses the the calling Kilim task until there is space in the
   mailbox to insert the message.
   This is is a pausable function that can only be invoked from pausable functions. Java threads should use
   the blocking version: mbox-put-blocking."
  [mbox data] `(.put ~mbox ~data))

(defmacro mbox-put-blocking
  "Blocking version of the put operation. It blocks the calling Java thread until thres is space in the
   mailbox to insert the message.
   This function must not be invoked by Kilim tasks since it will block the scheduler thread. The pausable
   version mbox-put-pausable must be used instead."
  [mbox data] `(.putb ~mbox ~data))

;; ## Task groups

(defmacro make-task-group
  "Creates a TaskGroup that can be used to control the execution of a set of Kilim tasks."
  [] `(kilim.TaskGroup.))

(defn task-group-add
  "Adds a new task to the task group. The task must have already been started.
   The function returns the task group passed as the first argument."
  [^kilim.TaskGroup task-group ^kilim.Task task] (.add task-group task)
                                                  task-group)

(defn task-group-join-blocking
  "Blocks the execution of the current thread until all the Kilim tasks in the group have finished their
   execution.
   This function must not be invoked by Kilim tasks since it will blocked the scheduler thread, task-group-join-pausable
   can be used instead.
   The function returns the task group passsed as ther first argument."
  [^kilim.TaskGroup task-group] (.joinb task-group)
                                task-group)

(defn task-group-join-pausable
  "Pauses the execution of the current Kilim task until all the Kilim tasks in the group have finished their
   execution.
   This function is pausable and must be invoked only from pausable methods. Regular function being executed in a
   Java thread can invoke the blocking variant, task-group-join-blocking.
   The function returns the task group passsed as ther first argument."
  [^kilim.TaskGroup task-group] (.joinb task-group)
                                task-group)


(defn task-group-results
  "Returns a map with the results emmited by a task group. The keys of the map are the task ids and the
   values the returned value."
  [^kilim.TaskGroup task-group] (reduce (fn [^clojure.lang.PersistentArrayMap acum ^kilim.ExitMsg result]
                                          (assoc acum (task-return-id result) (task-return-value result)))
                                  {} (.results task-group)))

