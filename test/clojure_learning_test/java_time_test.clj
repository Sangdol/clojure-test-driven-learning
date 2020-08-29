(ns clojure-learning-test.java-time-test
  (:require [clojure.test :refer :all]
            [java-time :as time]))


(deftest string-to-time-test
  (is (= (time/format "MM/dd"
                      (time/local-date "MM/yyyy/dd" "09/2015/28"))
         "09/28")))

