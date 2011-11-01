### Kilim/Clojure integration

## USE:

Library:

    [clj-kilim "0.2.0-SNAPSHOT"]

JVM options:

    :jvm-opts ["-javaagent:/Users/antonio/Desktop/kilimtest/lib/clj-kilim-0.2.0-20111019.122151-1.jar"]

## Examples

 - Simple task:

    (use 'kilim)

    (defn actor-mbox
      ([i] (let [mbox (make-mbox)]
             (task-start (pausable [] (let [rec (mbox-get mbox)]
                                        (println (str "received " rec " - " i)))))
             mbox)))
     
    (defn test-actor []
      (doseq [mbox (map (fn [x] (actor-mbox x)) (range 0 1000))]
        (mbox-put  mbox "hi!")))

  - Chain example:

    (defn chain
      ([prev-mbox]
         (chain prev-mbox (make-mbox)))
      ([prev-mbox next-mbox]
         (let [node (pausable []
                              (let [rec (mbox-get-pausable prev-mbox)]
                                (if (nil? next-mbox)
                                  (println (str rec "world"))
                                  (mbox-put next-mbox (str "hello " rec)))))]
           (task-start node)
           next-mbox)))
     
    (defn chain-example
      ([chain-length]
         (let [initial-mbox (make-mbox)
               prev-last-mbox (reduce (fn [prev-mbox _] (chain prev-mbox))
                                      initial-mbox
                                      (range 0 (dec chain-length)))]
           (chain prev-last-mbox nil)
           (mbox-put initial-mbox "hello "))))

  - Timed task:

    (defn timed-task
      ([i exitmb]
         (let [task (pausable [] (do (println (str "*** Task #" i " going to sleep ..."))
                                     (task-sleep 2000)
                                     (println (str "!!! Task #" i " waking up"))))]
           (-> task (task-start) (task-inform-on-exit  exitmb)))))
     
    (defn timed-task-example
      ([num-tasks]
         (let [exitmb (make-mbox)]
           (doseq [i (range 0 num-tasks)]
             (timed-task i exitmb))
           (mbox-get-blocking exitmb)
           (println (str "finished...")))))

  - Group example:

    (defn group-example
      ([]
         (let [task1 (pausable [] (do (println (str "Task #" 1 " going to sleep ..."))
                                      (task-sleep 1000)
                                      (println (str "           Task #" 1 " waking up"))
                                      (task-return "RETURNING 1")))
               task2 (pausable [] (do (println (str "Task #" 2 " going to sleep ..."))
                                      (task-sleep 1000)
                                      (println (str "           Task #" 2 " waking up"))
                                      (task-return "RETURNING 2")))
               results (-> (make-task-group)
                           (task-group-add (task-start task1))
                           (task-group-add (task-start task2))
                           (task-group-join-blocking)
                           (task-group-results))]
           (println (str "finished -> " results)))))

  - Generators:

    (def ^:dynamic *test-generator* (generator-start  (generator [g] (generator-yield g 1) (generator-yield g 2))))
     
    (println (str "*** should be true --> "
                  (= 3 (+ (generator-next *test-generator*)
                          (generator-next *test-generator*)))))
     
     
    (def-generator fib [g]
      (generator-yield g java.math.BigInteger/ZERO)
      (loop [i java.math.BigInteger/ZERO
             j java.math.BigInteger/ONE]
        (generator-yield g j)
        (recur j (.add i j))))
     
    (defn test-generator
      ([] (let [g (generator-seq fib)]
            (doseq [n (take 100 g)]
              (println (str "GOT " n))))))
     
    (println (str "SOME FIB NUMBERS: " (vec (take 100 (generator-seq fib)))))

 - Socket server:

    (use 'kilim.nio)
     
    ;; Socket server
     
    (start-socket-server 5019
                         (socket-server (pausable [ep]
                                                  (loop []
                                                    (let [buffer (java.nio.ByteBuffer/allocate 10)]
                                                      (. ep (fill buffer 10))
                                                      (.flip buffer)
                                                      (. ep (write buffer))
                                                      (recur))))))

 - HTTP server:

    (def echo-handler
      (http-handler (pausable [^kilim.http.HttpSession session]
                              (let [req (kilim.http.HttpRequest.)
                                    resp (kilim.http.HttpResponse.)]
                                (loop []
                                  (.readRequest session req)
                                  (let [pw (java.io.PrintWriter. (.getOutputStream resp))]
                                    (.append pw (str "<html><body><h1>Request!</h1> <br/> <p>Path: " (.uriPath req) "</p></body></html>"))
                                    (.flush pw)
                                    (.sendResponse session resp)
                                    (if (.keepAlive req)
                                      (recur)
                                      (println "ending execution"))))))))
     
    (start-http-server 7302  echo-handler)



