(ns clojure-learning-test.4clojure
  (:require [clojure.test :refer :all]))


(deftest gcd-test
  (letfn [(gcd [a b]
            (let [[a b] (sort [a b])
                  m (mod b a)]
              (if (= m 0)
                a
                (recur m a))))]
    (is (= 2 (gcd 2 4)))
    (is (= 1 (gcd 7 5))))

  (letfn [(gcd [a b]
            (if (zero? b)
              a
              (recur b (mod a b))))]
    (is (= 2 (gcd 2 4)))
    (is (= 1 (gcd 7 5)))))
