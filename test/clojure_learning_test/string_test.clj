(ns clojure-learning-test.string-test
  (:require [clojure.test :refer :all]))

(deftest contains-test
  (is (.contains "Abc" "b"))
  (is (re-find #"b" "Abc"))
  (is (= (re-find #"b" "Abc") "b"))
  (is (not (re-find #"d" "Abc")))
  )
