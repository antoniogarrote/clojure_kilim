(use 'kilim)

;; Simple task creation test

(defn actor-mbox
  ([i] (let [mbox (make-mbox)]
       (task-start (pausable [] (let [rec (mbox-get mbox)]
                                  (println (str "received " rec " - " i)))))
       mbox)))

(defn test-actor []
  (doseq [mbox (map (fn [x] (actor-mbox x)) (range 0 1000))]
    (mbox-putnb  mbox "hi!")))


;; Chain example

(defn chain
  ([prev-mbox]
     (chain prev-mbox (make-mbox)))
  ([prev-mbox next-mbox]
     (let [node (pausable []
                    (let [rec (mbox-get prev-mbox)]
                      (if (nil? next-mbox)
                        (println (str rec "world"))
                        (mbox-putnb next-mbox (str "hello " rec)))))]
       (task-start node)
       next-mbox)))

(defn chain-example
  ([chain-length]
     (let [initial-mbox (make-mbox)
           prev-last-mbox (reduce (fn [prev-mbox _] (chain prev-mbox))
                                  initial-mbox
                                  (range 0 (dec chain-length)))]
       (chain prev-last-mbox nil)
       (mbox-putnb initial-mbox "hello "))))

;; Timed task

(defn timed-task
  ([i exitmb]
     (let [task (pausable [] (do (println (str "Task #" i " going to sleep ..."))
                     (task-sleep 2000)
                     (println (str "           Task #" i " waking up"))))]
       (-> task (task-start) (task-inform-on-exit  exitmb)))))

(defn timed-task-example
  ([num-tasks]
     (let [exitmb (make-mbox)]
       (doseq [i (range 0 num-tasks)]
         (timed-task i exitmb))
       (mbox-getb exitmb)
       (println (str "finished...")))))


;;; Group example

(defn group-example
  ([]
     (let [task1 (pausable [] (do (println (str "Task #" 1 " going to sleep ..."))
                     (task-sleep 1000)
                     (println (str "           Task #" 1 " waking up"))
                     1))
           task2 (pausable [] (do (println (str "Task #" 2 " going to sleep ..."))
                     (task-sleep 1000)
                     (println (str "           Task #" 2 " waking up"))
                     2))
           results (-> (make-task-group)
                       (task-group-add (task-start task1))
                       (task-group-add (task-start task2))
                       (task-group-join)
                       (task-group-results))]
       (println (str "finished -> " results)))))


;; Generators

(start-generator  (generator [g] (generator-yield g 1) (generator-yield g 2)))

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


(println (str "SOME FIB NUMBERS " (vec (take 100 (generator-seq fib)))))