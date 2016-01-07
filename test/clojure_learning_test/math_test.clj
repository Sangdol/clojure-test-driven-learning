(ns clojure-learning-test.math-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]))

(deftest core-math-test
  (is (= 3 (quot 15 5)))
  (is (= 3 (quot 15 4)))
  (is (= -3 (quot -15 4)))

  (is (= -15/4 (/ -15 4)))
  )

; Math
; clojure.math.numeric-tower
; https://github.com/clojure/math.numeric-tower
(deftest math-test
         (is (= 1024 (math/expt 2 10)))
         (is (= 3 (math/sqrt 9)))
         (is (= [2 1] (math/exact-integer-sqrt 5)))
         (is (= 1 (math/abs -1)))
         (is (= 6 (math/gcd 24 18)))
         (is (= 72 (math/lcm 24 18)))
         (is (= 1.0 (math/floor 1.9)))
         (is (= 2.0 (math/ceil 1.1)))
         (is (= 2 (math/round 1.5)))
         )
