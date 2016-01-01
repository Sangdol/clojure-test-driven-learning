(ns clojure-learning-test.string_and_regex_test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]))

(deftest misc-test
  (is (= "cba" (s/reverse "abc")))
  )

(deftest re-finder-test
  (is (= "123" (re-find #"\d+" "123a")))

  (def matcher (re-matcher #"\d+" "123-456-789"))
  (is (= "123" (re-find matcher)))
  (is (= "456" (re-find matcher)))
  (is (= "789" (re-find matcher)))

  (is (= ["abc-1234" "abc" "1234"] (re-find #"(\S+)-(\d+)" "abc-1234")))
  (is (= ["1234" "1234"] (re-find #"(\d+)" "abc-1234")))
  )

(deftest contains-test
  (is (.contains "Abc" "b"))
  (is (re-find #"b" "Abc"))
  (is (= "b" (re-find #"b" "Abc")))
  (is (not (re-find #"d" "Abc")))
  )
