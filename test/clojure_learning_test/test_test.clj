(ns clojure-learning-test.test-test
  (:require [clojure.test :refer :all]))

(deftest mocking-test
  (defn f [] false)
  (with-redefs-fn {#'f (fn [] true)}
    #(is (= true (f))))

  ;; Why mocking a core function is not working?
  (with-redefs-fn {#'+ (fn [] 1)}
    #(is (= 2 (+ 1 1))))
  )

(deftest assertions
  (is (= 4 (+ 2 2)))
  (is (instance? Long 2) "This is Long")
  (is (.startsWith "abcde" "ab"))
  (is (thrown? ArithmeticException (/ 1 0)))
  (is (thrown-with-msg? ArithmeticException #"Divide by zero" (/ 1 0)))

  ;; 'notThrown?' is not possible - http://stackoverflow.com/questions/11232203/unable-to-resolve-symbol-thrown
  ;(not (thrown? ArithmeticException (/ 2 2)))

  ;; Error while loading cannot be catched with thrown?
  ;(is (thrown? IllegalArgumentException (^{:any "hoy"} "hm")))
  )

; documenting tests
; "must occur in a test function (deftest)
; https://clojure.github.io/clojure/clojure.test-api.html#clojure.test/testing
(deftest test-testing
  (testing "Arithmetic"
    (testing "with positive integers"
      (is (= 4 (+ 2 2)))
      (is (= 7 (+ 3 4)))
      (testing "with negative integers"
        (is (= -4 (+ -2 -2)))
        (is (= -1 (+ 3 -4)))))))

; This does not work with `defmacro`
; with-test cannot have a name so cannot run individually
(with-test
  (defn add [a b]
    (+ a b))
  (is (= 4 (add 2 2)))
  (is (= 7 (add 3 4))))

(deftest addition
  (is (= 4 (+ 2 2)))
  (is (= 7 (+ 3 4))))

(deftest comparison
  (is (if (= 1 1) true false))
  (is (and true true))
  (is (or true false))
  (is (= (or nil false 1) 1))
  (is (= (or nil 0 1) 0)))

