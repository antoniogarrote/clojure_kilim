;; Kilim includes some classes supporting asynchronous input-output
;; using Java NIO selectors.
;; Functions in this namespace wrap this functionality for TCP sockets
;; and HTTP servers.
;; 
(ns kilim.nio
  (:import [clojure.asm ClassWriter Type Opcodes ClassVisitor]
           [java.lang.reflect Modifier Constructor]
           [clojure.asm.commons Method GeneratorAdapter]
           [clojure.lang DynamicClassLoader]
           [kilim Mailbox Pausable Task]))

(defn- to-types
  "Builds a java array of types"
  ([cs] (if (pos? (count cs))
          (into-array (map (fn [^Class c] (. Type (getType c))) cs))
          (make-array Type 0))))

(defn http-handler
  "Accepts a pausable function that will be used as the handler of incoming HTTP connections"
  ([^clojure.lang.ITaskFn fn]
     (let [cv (new ClassWriter (. ClassWriter COMPUTE_MAXS))
           cname (str "clojure/lang/" (gensym "KilimHTTPProxy__"))
           ctype (. Type (getObjectType cname))
           super kilim.http.HttpSession
           super-type (. Type (getType kilim.http.HttpSession))
           itaskfn-type (. Type (getType clojure.lang.ITaskFn))
           ataskfn-type (. Type (getType clojure.lang.ATaskFn))
           ifn-type (. Type (getType clojure.lang.IFn))
           kilim-session-type (. Type (getType kilim.http.HttpSession))
           pausable-type (. Type (getType kilim.Pausable))
           className (.getName (class fn))
           fn-type (. Type (getType (Class/forName className)))
           obj-type (. Type (getType Object))
           pausable-exceptions (into-array (list pausable-type (. Type (getType java.lang.Exception))))]

       ;; declare class
       (. cv (visit (. Opcodes V1_5) ; version
                    (+ (. Opcodes ACC_PUBLIC) (. Opcodes ACC_SUPER)) ; access
                    cname ; name
                    nil ; signature
                    "kilim/http/HttpSession" ; supername
                    (make-array String 0);interfaces
                    ))

       ;; generate constructors
       (doseq [^Constructor ctor (. super (getDeclaredConstructors))]
         (when-not (. Modifier (isPrivate (. ctor (getModifiers))))
           (let [ptypes (to-types (. ctor (getParameterTypes)))
                 m (new Method "<init>" (. Type VOID_TYPE) ptypes)
                 gen (new GeneratorAdapter (. Opcodes ACC_PUBLIC) m nil nil cv)]
             (. gen (visitCode))
             ;call super ctor
             (. gen (loadThis))
             (. gen (dup))
             (. gen (loadArgs))
             (. gen (invokeConstructor super-type m))

             (. gen (returnValue))
             (. gen (endMethod)))))

       ;; generate execute method
       (let [m (. Method (getMethod "void execute()"))
             gen (new GeneratorAdapter (. Opcodes ACC_PUBLIC) m nil pausable-exceptions cv)]
         (. gen (visitCode))
         ; build instance of the function class
         (. gen (newInstance fn-type))
         (. gen (dup))
         (. gen (invokeConstructor fn-type (new Method "<init>" (. Type VOID_TYPE) (make-array Type 0))))
         ; invoke invokeTask passing 'this' as an argument
         (. gen (checkCast itaskfn-type))
         (. gen (loadThis))
         (. gen (checkCast kilim-session-type))
         (. gen (invokeInterface itaskfn-type (new Method "invokeTask" obj-type (into-array [obj-type]))))
         (. gen (pop))
         (. gen (returnValue))
         (. gen (endMethod)))

       ;; end of generation
       (. cv (visitEnd))

       ;; generate bytecode, weave and load
       (let [bytecode (. cv toByteArray)
             weaver (kilim.analysis.ClassWeaver. bytecode)
             infos (.getClassInfos weaver)]
         (doseq [info infos]
              (let [pname (.replace (.className info) "/" ".")]
                (. ^DynamicClassLoader (deref clojure.lang.Compiler/LOADER) (defineClass pname (.bytes info) nil))))
         (Class/forName (.replace cname "/" "."))))))


(defn start-http-server
  "Starts a new HTTP server for the provided HTTP pausable handler and port"
  [port ^kilim.http.HttpSession handler] (kilim.http.HttpServer. port  handler))


(defn socket-server
  "Accepts a pausable function that will be used as the handler of incoming TCP connections"
  ([^clojure.lang.ITaskFn fn]
     (let [cv (new ClassWriter (. ClassWriter COMPUTE_MAXS))
           cname (str "clojure/lang/" (gensym "KilimTCPProxy__"))
           ctype (. Type (getObjectType cname))
           super kilim.nio.SessionTask
           super-type (. Type (getType kilim.nio.SessionTask))
           kilim-session-type super-type
           itaskfn-type (. Type (getType clojure.lang.ITaskFn))
           ataskfn-type (. Type (getType clojure.lang.ATaskFn))
           ifn-type (. Type (getType clojure.lang.IFn))
           pausable-type (. Type (getType kilim.Pausable))
           className (.getName (class fn))
           fn-type (. Type (getType (Class/forName className)))
           obj-type (. Type (getType Object))
           end-point-type (. Type (getType (Class/forName "kilim.nio.EndPoint")))
           pausable-exceptions (into-array (list pausable-type (. Type (getType java.lang.Exception))))
           io-exception (into-array (list (. Type (getType java.io.IOException))))]

       ;; declare class
       (. cv (visit (. Opcodes V1_5) ; version
                    (+ (. Opcodes ACC_PUBLIC) (. Opcodes ACC_SUPER)) ; access
                    cname ; name
                    nil ; signature
                    "kilim/nio/SessionTask" ; supername
                    (make-array String 0);interfaces
                    ))

       ;; generate constructors
       (doseq [^Constructor ctor (. super (getDeclaredConstructors))]
         (when-not (. Modifier (isPrivate (. ctor (getModifiers))))
           (let [ptypes (to-types (. ctor (getParameterTypes)))
                 m (new Method "<init>" (. Type VOID_TYPE) ptypes)
                 gen (new GeneratorAdapter (. Opcodes ACC_PUBLIC) m nil nil cv)]
             (. gen (visitCode))
             ;call super ctor
             (. gen (loadThis))
             (. gen (dup))
             (. gen (loadArgs))
             (. gen (invokeConstructor super-type m))

             (. gen (returnValue))
             (. gen (endMethod)))))

       ;; generate execute method
       (let [m (. Method (getMethod "void execute()"))
             gen (new GeneratorAdapter (. Opcodes ACC_PUBLIC) m nil pausable-exceptions cv)]
         (. gen (visitCode))
         ; build instance of the function class
         (. gen (newInstance fn-type))
         (. gen (dup))
         (. gen (invokeConstructor fn-type (new Method "<init>" (. Type VOID_TYPE) (make-array Type 0))))
         ; invoke invokeTask passing 'this' as an argument
         (. gen (checkCast itaskfn-type))
         (. gen (loadThis))
         (. gen (checkCast kilim-session-type))
         (. gen (invokeVirtual kilim-session-type (new Method "getEndPoint" end-point-type (make-array Type 0))))
         (. gen (invokeInterface itaskfn-type (new Method "invokeTask" obj-type (into-array [obj-type]))))
         (. gen (pop))
         (. gen (returnValue))
         (. gen (endMethod)))

       ;; end of generation
       (. cv (visitEnd))

       ;; generate bytecode, weave and load
       (let [bytecode (. cv toByteArray)
             weaver (kilim.analysis.ClassWeaver. bytecode)
             infos (.getClassInfos weaver)]
         (doseq [info infos]
              (let [pname (.replace (.className info) "/" ".")]
                (. ^DynamicClassLoader (deref clojure.lang.Compiler/LOADER) (defineClass pname (.bytes info) nil))))
         (Class/forName (.replace cname "/" "."))))))

(defn start-socket-server [port server-handler]
  (let [sch (kilim.Scheduler/getDefaultScheduler)
        sel (kilim.nio.NioSelectorScheduler.)]
    (. sel (listen port server-handler sch))))