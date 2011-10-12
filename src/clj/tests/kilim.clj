(ns tests.kilim
  (:use [kilim])
  (:use [clojure.test]))

(kilim-debug false)

(deftest should-define-lambda-task []
  (let [request-mbox (make-mbox)
        answer-mbox (make-mbox)
        task (pausable [] (let [request (mbox-get request-mbox)]
                            (mbox-put answer-mbox (inc request))))]
    (task-start task)
    (mbox-putnb request-mbox 1)
    (let [res (mbox-getb answer-mbox)]
      (is (= res 2)))))


(def ^:dynamic *request-mbox* (make-mbox))
(def ^:dynamic *answer-mbox* (make-mbox))

(def-pausable answer-fn []
  (let [req (mbox-get *request-mbox*)]
    (mbox-put *answer-mbox* (inc req))))

(deftest should-define-task []
  (let [task (task-start answer-fn)]
    (mbox-putnb *request-mbox* 2)
    (is (= (mbox-getb *answer-mbox* ) 3))))



(def-pausable sleep-some-time [millisecs]
  (task-sleep millisecs))

(deftest pausable-may-call-pausable []
  (let [mbox (make-mbox)
        task (pausable []
                (do
                   (call-pausable sleep-some-time 5000)
                   (mbox-put mbox "finished")))]
    (println "** task will sleep 5 secs")
    (task-start task)
    (is (= (mbox-getb mbox) "finished"))))



(deftest task-should-inform-on-exit []
  (let [mbox (make-mbox)
        flag (promise)
        task (pausable []
                  (task-sleep 1000)
                  (deliver flag :ok)
                  (task-return "delivered!"))]
    (println (str "** task will sleep 1 sec"))
    (-> task (task-start) (task-inform-on-exit mbox))
    (let [res (mbox-getb mbox)]
        (is (= (task-return-value res) "delivered!"))
        (is (= @flag :ok)))))


(deftest task-should-have-an-id []
  (let [mbox (make-mbox)
        finish-mbox (make-mbox)
        task (pausable [] (mbox-get mbox) (task-return :ok))]
  (task-start task)
  (task-inform-on-exit task finish-mbox)
  (let [id (task-id task)]
    (is (not= id nil))
    (mbox-putnb mbox "continue")
    (let [res (mbox-getb finish-mbox)]
      (is (= (task-return-value res) :ok))
      (is (= (task-return-id res) (task-id task)))))))

;; Run all the tests
(run-tests)