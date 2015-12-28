(ns clojure-learning-test.string_and_regex_test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]))

(deftest misc-test
  (is (= (s/reverse "abc") "cba"))
  )

(deftest re-finder-test
  (is (= (re-find #"\d+" "123a") "123"))

  (def matcher (re-matcher #"\d+" "123-456-789"))
  (is (= (re-find matcher) "123"))
  (is (= (re-find matcher) "456"))
  (is (= (re-find matcher) "789"))

  (is (= (re-find #"(\S+)-(\d+)" "abc-1234") ["abc-1234" "abc" "1234"]))
  (is (= (re-find #"(\d+)" "abc-1234") ["1234" "1234"]))
  )

(deftest contains-test
  (is (.contains "Abc" "b"))
  (is (re-find #"b" "Abc"))
  (is (= (re-find #"b" "Abc") "b"))
  (is (not (re-find #"d" "Abc")))
  )
