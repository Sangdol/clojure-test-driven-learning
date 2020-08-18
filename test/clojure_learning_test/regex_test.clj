(ns clojure-learning-test.regex-test
  (:require [clojure.test :refer :all]))


;;
;; Check out Java regex document https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
;; Don't use re-matcher, re-groups, and re-find (Java Matcher object).
;;


(deftest basic-test
  ; Compiled at read time
  ; e.g., It's compiled when the code is read.
  ;       It won't be compiled every time even if it's in a loop.
  (is (= java.util.regex.Pattern (class #"")))
  (is (= (str #"\d") (str (java.util.regex.Pattern/compile "\\d")))))


(deftest re-seq-test
  (is (= '("one" "two") (re-seq #"\w+" "one/two")))

  ; capturing group
  (is (= '(("one" "e") ("two" "o")) (re-seq #"\w+(\w)" "one/two")))
  (is (= '(("one" "one") ("two" "two")) (re-seq #"(\w+)" "one/two"))))
