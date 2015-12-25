(ns clojure-learning-test.math-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]))

; Math
; clojure.math.numeric-tower
; https://github.com/clojure/math.numeric-tower
(deftest math-test
         (is (= (math/expt 2 10) 1024))
         (is (= (math/sqrt 9) 3))
         (is (= (math/exact-integer-sqrt 5) [2 1]))
         (is (= (math/abs -1) 1))
         (is (= (math/gcd 24 18) 6))
         (is (= (math/lcm 24 18) 72))
         (is (= (math/floor 1.9) 1.0))
         (is (= (math/ceil 1.1) 2.0))
         (is (= (math/round 1.5) 2)))
