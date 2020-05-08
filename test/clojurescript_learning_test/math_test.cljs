(ns clojurescript-learning-test.math-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]))

(deftest math-test
  (is (= 3.141592653589793 (.-PI js/Math))))
