(ns tests.kilim
  (:use [kilim])
  (:use [clojure.test]))

(kilim-debug false)

(deftest should-define-lambda-task []
  (let [request-mbox (make-mbox)
        answer-mbox (make-mbox)
        task (pausable [] (let [request (mbox-get-pausable request-mbox)]
                            (mbox-put answer-mbox (inc request))))]
    (task-start task)
    (mbox-put request-mbox 1)
    (let [res (mbox-get-blocking answer-mbox)]
      (is (= res 2)))))


(def ^:dynamic *request-mbox* (make-mbox))
(def ^:dynamic *answer-mbox* (make-mbox))

(def-pausable answer-fn []
  (let [req (mbox-get-pausable *request-mbox*)]
    (mbox-put *answer-mbox* (inc req))))

(deftest should-define-task []
  (let [task (task-start answer-fn)]
    (mbox-put *request-mbox* 2)
    (is (= (mbox-get-blocking *answer-mbox* ) 3))))



(def-pausable sleep-some-time [millisecs]
  (task-sleep millisecs))

(deftest pausable-may-call-pausable []
  (let [mbox (make-mbox)
        task (pausable []
                (do
                   (call-pausable sleep-some-time 2000)
                   (mbox-put mbox "finished")))]
    (println "** task will sleep 2 secs")
    (task-start task)
    (is (= (mbox-get-blocking mbox) "finished"))))


(deftest task-should-inform-on-exit []
  (let [mbox (make-mbox)
        flag (promise)
        task (pausable []
                  (task-sleep 1000)
                  (deliver flag :ok)
                  (task-return "delivered!"))]
    (println (str "** task will sleep 1 sec"))
    (-> task (task-start) (task-inform-on-exit mbox))
    (let [res (mbox-get-blocking mbox)]
        (is (= (task-return-value res) "delivered!"))
        (is (= @flag :ok)))))

(deftest task-should-inform-on-exitmany-mailboxes []
  (let [mbox1 (make-mbox)
        mbox2 (make-mbox)
        flag (promise)
        task (pausable []
                  (task-sleep 1000)
                  (deliver flag :ok)
                  (task-return "delivered!"))]
    (println (str "** task will sleep 1 sec"))
    (-> task (task-start) (task-inform-on-exit mbox1) (task-inform-on-exit mbox2))
    (let [res1 (mbox-get-blocking mbox1)
          res2 (mbox-get-blocking mbox2)]
        (is (= (task-return-value res1) "delivered!"))
        (is (= (task-return-value res2) "delivered!"))
        (is (= @flag :ok)))))

(deftest task-return-should-terminate-the-task []
  (let [mbox (make-mbox)
        task (pausable [] (task-sleep 1000)
                          (task-return 10)
                          (task-sleep 1000)
                          (task-return 5))]
    (-> task (task-start) (task-inform-on-exit mbox))
    (let [result (mbox-get-blocking mbox)]
       (is (= (task-return-value result) 10)))))


(deftest task-should-have-an-id []
  (let [mbox (make-mbox)
        finish-mbox (make-mbox)
        task (pausable [] (mbox-get-pausable mbox) (task-return :ok))]
  (task-start task)
  (task-inform-on-exit task finish-mbox)
  (let [id (task-id task)]
    (is (not= id nil))
    (mbox-put mbox "continue")
    (let [res (mbox-get-blocking finish-mbox)]
      (is (= (task-return-value res) :ok))
      (is (= (task-return-id res) (task-id task)))))))



(deftest task-groups-should-return-map-of-results []
  (println "** tasks will sleep 3 secs")
  (let [task1 (task-start (pausable [] (do (task-sleep 3000)
                                         (task-return 1))))
        task2 (task-start (pausable [] (do (task-sleep 3000)
                                         (task-return 2))))
        task3 (task-start (pausable [] (do (task-sleep 3000)
                                         (task-return 3))))
        results (-> (make-task-group)
                    (task-group-add task1)
                    (task-group-add task2)
                    (task-group-add task3)
                    (task-group-join-blocking)
                    (task-group-results))]
       (is (= 3 (count results)))
       (is (= '(1 2 3) (sort (vals results))))
       (is (= 1 (get results (task-id task1))))
       (is (= 2 (get results (task-id task2))))
       (is (= 3 (get results (task-id task3))))))

(deftest task-groups-inside-pausable-function []
  (println "** tasks will sleep 3 secs")
  (let [task1  (pausable [] (do (task-sleep 3000)
                                         (task-return 1)))
        task2  (pausable [] (do (task-sleep 3000)
                                         (task-return 2)))
        task3  (pausable [] (do (task-sleep 3000)
                                         (task-return 3)))
        mbox   (make-mbox)
        task-creator (pausable [] (let [results (-> (make-task-group)
                                                    (task-group-add (task-start task1))
                                                    (task-group-add (task-start task2))
                                                    (task-group-add (task-start task3))
                                                    (task-group-join-pausable)
                                                    (task-group-results))]
                                        (task-return results)))
        _ (-> task-creator (task-start) (task-inform-on-exit mbox))
        results (task-return-value (mbox-get-blocking mbox))]
       (is (= 3 (count results)))
       (is (= '(1 2 3) (sort (vals results))))))


(deftest should-create-lambda-generators []
  (let [a 1
        gen-fn (generator [g] (generator-yield g (+ a 2)) (generator-yield g (+ a 3)))
        g (try (generator-start gen-fn)
                (catch Exception e (println (str "EXCEPTION: \n" (.getMessage e)))))]
    (is (= 3 (generator-next g)))
    (is (= 4 (generator-next g)))
    (is (nil? (generator-next g)))))

(def-generator fib [g]
  (generator-yield g java.math.BigInteger/ZERO)
  (loop [i java.math.BigInteger/ZERO
         j java.math.BigInteger/ONE]
    (generator-yield g j)
    (recur j (.add i j))))

(deftest should-create-a-generator  []
  (let [g1 (generator-start fib)
        g2 (generator-start fib)
        r1 [(generator-next g1) (generator-next g1)]
        r2 [(generator-next g2) (generator-next g2)]]
    (is (= r1 r2))
    (is (= (take 3 (generator-seq fib))
           (take 3 (generator-seq fib))))))

(deftest should-yield-nil-values []
  (let [gen-fn (generator [g] (generator-yield g nil) (generator-yield g 1))
        g (generator-start gen-fn)]
    (is (nil? (generator-next g)))
    (is (= (generator-next g) 1))
    (is (nil? (generator-next g)))))


;; Run all the tests
(run-tests)