(ns clojure-learning-test.string_and_regex_test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]))


;; Don't use re-matcher, re-groups, and re-find (Java Matcher object).
;; - from "The joy of Clojure"


(deftest strip-quotes-test
  (let [re-no-quote #"[\^\"\"$]"]
    (is (= "abc" (s/replace "\"abc\"" re-no-quote "")))))


(deftest split-test
  ; multiline
  (is (= ["" "\n" "\n"] (s/split "a\na\na" #"(?m)^a")))

  ; limit
  (is (= ["a b c"] (s/split "a b c" #" " 1)))
  (is (= ["a" "b c"] (s/split "a b c" #" " 2))))


(deftest concat-str-test
  (is (= "abcd" (str "ab" "cd"))))


(deftest misc-test
  (is (= "cba" (s/reverse "abc"))))


(deftest re-seq-test
  (is (= ["A", "B", "C"] (re-seq #"[A-Z]" "AaaBbbCcc")))
  (is (= ["aaa" "Aaa"] (re-seq #"(?i)[a].." "aaa Aaa bbb"))))


(deftest re-finder-test
  (is (= "123" (re-find #"\d+" "123a")))

  (def matcher (re-matcher #"\d+" "123-456-789"))
  (is (= "123" (re-find matcher)))
  (is (= "456" (re-find matcher)))
  (is (= "789" (re-find matcher)))

  (is (= ["abc-1234" "abc" "1234"] (re-find #"(\S+)-(\d+)" "abc-1234")))
  (is (= ["1234" "1234"] (re-find #"(\d+)" "abc-1234"))))


(deftest contains-test
  (is (.contains "Abc" "b"))
  (is (s/includes? "abc" "bc"))
  (is (not (s/includes? "abc" "cd")))
  (is (re-find #"b" "Abc"))
  (is (= "b" (re-find #"b" "Abc")))
  (is (not (re-find #"d" "Abc"))))

