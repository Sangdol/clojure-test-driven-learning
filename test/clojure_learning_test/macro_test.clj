(ns clojure-learning-test.macro-test
  (:require [clojure.test :refer :all]))


(deftest macroexpand-test
  (testing "when is macro"
    (is (= '(if 1 (do 2 3)) (macroexpand-1 '(when 1 2 3))))
    (is (= '(if 1 (do 2 3)) (macroexpand '(when 1 2 3))))))

(defn hello [target]
  (str "Hello, " target "!"))

(defmacro def-hello [name target]
  (list 'defn
        (symbol name)
        []
        (list 'hello target)))

(def-hello hello-sang "Sang")
(def-hello hello-hj "HJ")

(deftest defmacro-test
  (is (= "Hello, Sang!" (hello-sang)))
  (is (= "Hello, HJ!" (hello-hj))))


(deftest syntax-quote-test
  (testing "backtick fully qualifies"
    (is (= '(clojure.core/first []) `(first []))))
  (testing "x doesn't resolve by default"
    (is (= `(first [x 1 2])
          (let [x 0]
            `(first [x 1 2])))))
  (testing "tilde makes x resolve"
    (is (= `(first [0 1 2])
           (let [x 0]
             `(first [~x 1 2]))))))

