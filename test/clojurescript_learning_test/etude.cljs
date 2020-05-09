#_("

Etudes for ClojureScript

")

(ns clojurescript-learning-test.etude
  (:require [cljs.test :refer-macros [deftest is testing]]))

(deftest ch-3
  (letfn [(move-zeros [l]
            (concat (filter #(not= %1 0) l) (filter zero? l)))]
    (is (= [1 2 3 0 0 0] (move-zeros [1 0 2 0 0 3])))))
