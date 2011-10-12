(ns kilim)

(defn kilim-debug [should-debug]
  (clojure.kilim.KilimTransformerAgent/setDebugging should-debug))

(defmacro pausable [& body]
  (let [nfn (gensym "pausable__fn__")]
    (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str nfn) "-" "_"))
    (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "task")
    `(do
       (def ~nfn (fn ~@body))
       ~nfn)))

(defmacro def-pausable [name & rest]
  (do (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str name) "-" "_"))
      (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "task")
      `(defn ~name ~@rest)))

(defmacro generator [args & body]
  (let [nfn (gensym "pausable__fn__")
        orig-g (first args)]
    (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str nfn) "-" "_"))
    (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "generator")
    `(do
       (def ~nfn (fn [g#] (let [~orig-g (cast clojure.lang.AGeneratorFunction g#)] ~@body)))
       ~nfn)))

(defmacro def-generator [name args & body]
  (let [orig-g (first args)]
    (clojure.kilim.KilimTransformerAgent/setNextFunctionToWeave (.replace (str name) "-" "_"))
    (clojure.kilim.KilimTransformerAgent/setKindOfTransformation "generator")
    `(do
       (def ~name (fn [g#] (let [~orig-g (cast clojure.lang.AGeneratorFunction g#)] ~@body))))))

(defmacro start-generator [g]
  `(.newInstance (class ~g)))

(defn generator-seq
  ([f]
     (letfn [(next-value [g] (let [next (.next g)] (if (nil? next) next (lazy-cat (list next) (next-value g)))))]
       (let [g (start-generator f)
             first-value (let [v (.next g)] (if (nil? v) v (list v)))]
         (lazy-cat
          first-value
          (next-value g))))))

(defmacro generator-yield [g data] `(. ~g yield ~data))

(defmacro make-mbox [] `(kilim.Mailbox.))

(defmacro call-pausable [& rest]
  `(.invokeTask ~@rest))

(defmacro mbox-get [mbox]
  `(let [m# (cast kilim.Mailbox ~mbox)]
    (.get m#)))

(defmacro mbox-getb [mbox]
  `(let [m# (cast kilim.Mailbox ~mbox)]
     (.getb m#)))

(defn mbox-putnb [^kilim.Mailbox mbox msg]
  (.putnb mbox msg))

(defmacro mbox-put [mbox data]
  `(.put ~mbox ~data))

(defn task-start [^kilim.Task task]
  (.start task))

(defmacro task-id [task] `(.id ~task))

(defmacro task-return [exit-value] `(kilim.Task/exit ~exit-value))

(defmacro task-return-value [result] `(.result ~result))
(defmacro task-return-id [result] `(.. ~result task id))

(defn task-inform-on-exit [^kilim.Task task ^kilim.Mailbox mbox]
  (.informOnExit task mbox))

(defmacro task-sleep [millisecs]
  `(kilim.Task/sleep ~millisecs))

(defmacro make-task-group [] `(kilim.TaskGroup.))

(defn task-group-add [^kilim.TaskGroup task-group ^kilim.Task task]
  (.add task-group task)
  task-group)

(defn task-group-join [^kilim.TaskGroup task-group]
  (.joinb task-group)
  task-group)

(defn task-group-results [^kilim.TaskGroup task-group]
  (.results task-group))


;; Misc

(defn show-java-methods
  "Collections and optionally prints the methods defined in a Java object"
  ([obj should-show?]
     (let [ms (.. obj getClass getDeclaredMethods)
           max (alength ms)]
       (loop [count 0
              acum []]
         (if (< count max)
           (let [m (aget ms count)
                 params (.getParameterTypes m)
                 params-max (alength params)
                 return-type (.getReturnType m)
                 to-show (str (loop [acum (str (.getName m) "(")
                                     params-count 0]
                                (if (< params-count params-max)
                                  (recur (str acum " " (aget params params-count))
                                         (+ params-count 1))
                                  acum))
                              " ) : " return-type)]
             (when should-show? (println (str to-show)))
             (recur (+ 1 count)
                    (conj acum (str to-show))))
           acum))))
  ([obj] (show-java-methods obj true)))
