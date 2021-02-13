;; https://github.com/dakrone/cheshire
(ns clojure-learning-test.cheshire-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer :all]))

(deftest parse-test
  (is (= [1 2] (parse-string "[1, 2]")))
  (is (= ["a", "b"] (parse-string "[\"a\", \"b\"]")))
  (is (= 1 (parse-string "1"))))
