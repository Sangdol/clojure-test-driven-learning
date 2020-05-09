#_ ("
JS namespace
https://cljs.github.io/api/syntax/js-namespace
")

(ns clojurescript-learning-test.js-test
  (:require [cljs.test :refer-macros [deftest is testing]]))

(deftest js-object-test
  (is (= 3.5 (.parseFloat js/window "3.5")))
  (is (= "ABC" (.toUpperCase "abc")))
  (is (= "ABC" (.substring "0ABC" 1 5)))
  (is (= 3 (.-length "ABC")))

  ;; Using underlying JS namespace
  (is (= 3.5 (js/window.parseFloat "3.5"))))

(deftest creating-object-test
  ;; (new Date).getHours()
  (is (<= 0 (.getHours (js/Date.)))))
