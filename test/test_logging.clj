(ns test-logging
  (:use [clojure.test])
  (:require 
     [clojure.contrib.logging :as l]
     [tjpext.logging :as lx]))

(deftest output-captured
  (let [out-orig *out*
        err-orig *err*]
    (l/with-logs "logging"
      (is (not= *out* out-orig))
      (is (not= *err* err-orig))
      (println "Hello from println")
      (.println (System/out) "Hello from stdout")
      (.println (java.io.PrintWriter. *err*) "Hello from *err*")
      (.println (System/err) "Hello from stderr"))
    (l/log :debug "Hello from :debug")
    (l/spy (+ 1 2))
    @(future (l/spy *out*))
    (lx/logged-future (/ 1 0))
    ; wait without forcing the future
    (Thread/sleep 10)
    (is (= *out* out-orig))
    (is (= *err* err-orig))))

