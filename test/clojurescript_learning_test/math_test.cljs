(ns clojurescript-learning-test.math-test
  (:require [cljs.test :refer-macros [deftest is testing]]))

(deftest math-test
  (is (= 3.141592653589793 (.-PI js/Math)))
  ;; Math pseudo namesapce https://cljs.github.io/api/syntax/Math-namespace
  (is (= 3.141592653589793 Math/PI))
  (is (= 1 (.sin js/Math (/ (.-PI js/Math) 2))))
  (is (= 1 (Math/sin (/ (.-PI js/Math) 2)))))
