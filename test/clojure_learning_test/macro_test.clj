(ns clojure-learning-test.macro-test
  (:require [clojure.test :refer :all]
            [clojure.walk :as walk]))


(deftest eval-test
  ; take a list and execute it
  (is (= 3 (eval '(+ 1 2))))
  (is (= 3 (eval (list + 1 2))))

  (is (= '((inc 1)) '((inc 1))))
  (is (= '(2) (list (inc 1))))

  (let [x [1 2]]
    (is (= 5 (eval `(+ ~@(map inc x) 0))))))


(deftest macroexpand-test
  (testing "when is macro"
    (is (= '(if 1 (do 2 3)) (macroexpand-1 '(when 1 2 3))))
    (is (= '(if 1 (do 2 3)) (macroexpand '(when 1 2 3))))))

(defmacro return-if [& clauses]
  (when (seq clauses)
    (list 'if (first clauses)
          (second clauses)
          (cons 'return-if (nnext clauses)))))


(defmacro do-until [& clauses]
  (when (seq clauses)
    (list 'clojure.core/when (first clauses)
          (second clauses)
          (cons 'do-until (nnext clauses)))))


(deftest macroexpand-test2
  (is (= 1 (return-if true 1)))
  (is (= 3 (return-if false 2 true 3)))

  ; be wary of the context
  (is (= '(return-if true 1)
         (macroexpand-1 '(return-if true 1))))

  (is (= '(if true 1 (return-if))
         (macroexpand-1 '(clojure-learning-test.macro-test/return-if true 1))))

  (is (= '(clojure.core/when true 1 (do-until))
         (macroexpand-1 '(clojure-learning-test.macro-test/do-until true 1))))

  (is (= '(if true (do 1 (do-until)))
         (macroexpand '(clojure-learning-test.macro-test/do-until true 1))))

  ; this will expand subforms as well (what are subforms?).
  (is (= '(if true (do 1 (do-until)))
         (walk/macroexpand-all '(clojure-learning-test.macro-test/do-until true 1)))))


; this is the same as 'if'
(defmacro ternary [& xs]
  (list 'if (first xs)
        (second xs)
        (nth xs 2)))


(defmacro mfirst [& xs]
  `(first '~xs))


(deftest ternary-test
  (is (= 1 (ternary true 1 2)))
  (is (= 2 (ternary false 1 2)))

  (is (= true (mfirst true 1 2)))
  (is (= 1 (mfirst 1 2))))
  ;(is (= 2 (ternary2 false 1 2))))


(defn just-unquote-func [& xs]
  `~xs)


(defmacro just-unquote [& xs]
  `~xs)


(deftest just-unquote-test
  (is (= '(1 2 3 4) (just-unquote-func 1 2 3 4)))
  (is (= 10 (just-unquote + 1 2 3 4)))
  (is (= 24 (just-unquote * 1 2 3 4))))


(def x 100)

; being resolved at macro-expansion time
(defmacro resolution [x] `x)

; how does this take 1?
;   it's `x` after an expansion.
(defmacro resolution2 [x] 'x)

(defmacro resolution3 [x] `~x)
(defmacro resolution4 [x] ``~~x)
(defmacro resolution5 [x] `(+ ~'x))


(deftest resolution-test
  (let [x 1]
    (is (= 100 (resolution 2)))
    (is (= 'clojure-learning-test.macro-test/x
           (macroexpand '(clojure-learning-test.macro-test/resolution 2))))

    (is (= 1 (resolution2 2)))
    (is (= 'x
           (macroexpand '(clojure-learning-test.macro-test/resolution2 2))))

    (is (= 2 (resolution3 2)))
    (is (= 2
           (macroexpand '(clojure-learning-test.macro-test/resolution3 2))))

    (is (= 2 (resolution4 2)))

    (is (= 1 (resolution5 2)))
    (is (= '(clojure.core/+ x)
           (macroexpand '(clojure-learning-test.macro-test/resolution5 2))))))


(defn hello [target]
  (str "Hello, " target "!"))


(defmacro def-hello [name target]
  (list 'defn
        (symbol name)
        []
        (list 'hello target)))


(def-hello hello-sang "Sang")
(def-hello hello-hj "HJ")


(deftest simple-defmacro-test
  (is (= "Hello, Sang!" (hello-sang)))
  (is (= "Hello, HJ!" (hello-hj))))


(deftest syntax-quote-test
  (testing "backtick fully qualifies"
    (is (= '(clojure.core/first []) `(first []))))

  (testing "x isn't resolved by default"
    (is (= `(first [x 1 2])
          (let [x 0]
            `(first [x 1 2])))))

  (testing "tilde makes x resolved (unquote)"
    (is (= `(first [0 1 2])
           (let [x 0]
             `(first [~x 1 2])))))

  ;; quote unquote crazy
  (let [x '(- x)]
    (is (= 'x 'x))
    (is (= '(- x) x))
    (is (= 'clojure-learning-test.macro-test/x `x))
    (is (= (quote 'clojure-learning-test.macro-test/x) ``x))
    (is (= (quote 'clojure-learning-test.macro-test/x) '`x))
    (is (= (quote 'clojure-learning-test.macro-test/x) `'x))
    (is (= (list 'quote 'clojure-learning-test.macro-test/x) `'x))
    (is (= (quote (clojure.core/unquote x)) '~x))
    (is (= '(- x) `~x))
    (is (= 'clojure-learning-test.macro-test/x ``~x))
    (is (= (quote '(- x)) `'~x)))

  ;; unquote-splice
  (let [x `(2 3)]
    (is (= '(1 (2 3)) `(1 ~x)))
    (is (= '(1 2 3) `(1 ~@x)))
    (is (= '(1 3 4) `(1 ~@(map inc [2 3]))))))


(defmacro f [exp]
  (let [[a op b] exp]
    (list op a b)))


(defmacro f2 [exp]
  (let [[a op b] exp]
    `(~op ~a ~b)))


(deftest macro-op-test
  (is (= 3 (f (1 + 2))))
  (is (= 2 (f (1 * 2))))
  (is (= 2 (f2 (1 * 2)))))


