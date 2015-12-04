(ns clojure-learning-test.core-test
  (:require [clojure.test :refer :all]
            [clojure-learning-test.core :refer :all]
            [clojure.math.numeric-tower :as math])
  (:import java.time.LocalDateTime
           java.time.LocalDate))

; Use "use" to get all functions from the module
; e.g., intersection, difference
(use 'clojure.set)
; You can choose a subset of functions to import, too
; (use '[clojure.set :only [intersection]])

; Use 'require' to import a module
(require 'clojure.string)

; You can give a module a shorter name on "import"
(require '[clojure.string :as str])

; Use import to load a Java module
(import java.util.Date)
(import java.util.Calendar)

; References
; * Test API https://clojure.github.io/clojure/clojure.test-api.html
; * In y minutes http://learnxinyminutes.com/docs/clojure/
; * Docs https://clojuredocs.org/

; Commands
; $ lein test
; $ watch-vi "*" "lein test" # in this directory

;
; assertions
;
(is (= 4 (+ 2 2)))
(is (instance? Long 2) "This is Long")
(is (.startsWith "abcde" "ab"))

(is (thrown? ArithmeticException (/ 1 0)))
(is (thrown-with-msg? ArithmeticException #"Divide by zero" (/ 1 0)))

;
; documenting tests
;
(testing "Arithmetic"
  (testing "with positive integers"
    (is (= 4 (+ 2 2)))
    (is (= 7 (+ 3 4)))
  (testing "with negative integers"
    (is (= -4 (+ -2 -2)))
    (is (= -1 (+ 3 -4))))))

;
; defining tests
;

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

; not regarded as a test
(deftest addition-withdefn
  (defn add [a b]
    (+ a b))
  (is (= 4 (add 2 2)))
  (is (= 7 (add 3 4))))

(deftest comparison
  (is (if (= 1 1) true false))
  (is (and true true))
  (is (or true false)))

; defn
; https://clojuredocs.org/clojure.core/defn
(defn get-if-mod-of-3-5 [n]
  (if (or (= (mod n 3)  0) (= (mod n 5) 0))
    n 0))

(deftest get-if-mod-of-3-5-test
  (is (= (get-if-mod-of-3-5 3) 3))
  (is (= (get-if-mod-of-3-5 15) 15))
  (is (= (get-if-mod-of-3-5 2) 0)))

(defn sum-of-mod-of-3-5
  ([n s]
    (if (= n 0)
      s
      (sum-of-mod-of-3-5 (- n 1)
          (+ s (get-if-mod-of-3-5 n)))))
  ([n]
    (if (= n 0)
      0
      (+ (sum-of-mod-of-3-5 (- n 1)) (get-if-mod-of-3-5 n)))))

(deftest sum-of-mod-of-3-5-test
  (is (= (sum-of-mod-of-3-5 999 0) 233168))
  (is (= (sum-of-mod-of-3-5 999) 233168)))

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

; Learn X in Y minutes
; http://learnxinyminutes.com/docs/clojure/
;
; What is difference between Vector and List?
(deftest learn-x-in-y-minutes
  (is (= (str "Hello" " " "World") "Hello World"))
  (is (= (+ 0.1 0.2) 0.30000000000000004))

  ; Class
  (is (= (class 1) Long))
  (is (= (class 1.) Double))
  (is (= (class "") String))
  (is (= (class false) Boolean))
  (is (= (class nil) nil))

  ; Literal
  (is (not (= '(+ 1 2) (+ 1 2))))
  (is (= '(+ 1 2) (list '+ 1 2)))
  (is (= (eval '(+ 1 2)) (+ 1 2)))

  ; What is difference between Vector and List?
  ; http://stackoverflow.com/questions/1147975/in-clojure-when-should-i-use-a-vector-over-a-list-and-the-other-way-around
  ; Vectors - ArrayList(but not seq), Lists - LinkedList
  (is (= (class [1, 2, 3]) clojure.lang.PersistentVector)) ; Why need full name?
  (is (= (class '(1 2 3)) clojure.lang.PersistentList))
  (is (= '(1 2 3) (list 1 2 3)))
  (is (coll? '(1 2 3)))
  (is (coll? [1 2 3]))

  ; "Sequences" (seqs) are abstract descriptions of lists of data.
  (is (seq? '(1 2 3)))
  (is (not (seq? [1 2 3])))

  ; A seq need only provide an entry when it's accessed
  (is (= (range 4) '(0 1 2 3)))
  (is (= (take 4 (range)) '(0 1 2 3)))

  ; Use cons to add an item to the beginning of a list or vector
  (is (= (cons 4 [1 2 3]) [4 1 2 3]))
  (is (= (cons 4 '(1 2 3)) '(4 1 2 3)))

  ; Conj add an item to a collection in the most effective way.
  (is (= (conj [1 2 3] 4) [1 2 3 4]))
  (is (= (conj '(1 2 3) 4) '(4 1 2 3)))

  ; What is difference between cons and conj?
  ; http://stackoverflow.com/questions/3008411/clojure-consseq-vs-conjlist
  ; conj takes any number of arguments (conj = conjoin an item into a collection)
  (is (= (conj '(1 2 3) 4 5 6) '(6 5 4 1 2 3)))
  (is (= (class (conj '(1 2) 3)) clojure.lang.PersistentList))
  ; cons takes just one (cons = construct a seq)
  (is (thrown? IllegalArgumentException (cons 1 2 3 '(4 5 6))))
  (is (= (class (cons 3 '(1 2))) clojure.lang.Cons))
  (is (= (class (next (cons 3 '(1 2)))) clojure.lang.PersistentList))
  (is (= (next (cons 3 '(1 2))) '(1 2)))

  ; next/rest
  ; http://stackoverflow.com/questions/4288476/clojure-rest-vs-next
  (is (= (next '(1)) nil))
  (is (= (next '(1 2)) '(2)))
  (is (= (rest '(1)) '()))
  (is (= (rest '(1 2)) '(2)))

  (is (= (concat [1 2] '(3 4)) '(1 2 3 4)))

  (is (= (map inc '(1 2 3)) '(2 3 4)))
  (is (= (filter even? '(1 2 3)) '(2)))
  (is (= (filter even? '[1 2 3]) '(2)))
  (is (= (reduce + '(1 2 3 4)) 10))
  (is (= (reduce - '(1 2 3 4)) -8))
  (is (= (reduce conj [] '(1 2 3)) [1 2 3]))
  ;(is (= (reduce cons [] '(1 2 3)) [1 2 3]))

  ;;;;;;;;;;;
  ; Functions
  ;;;;;;;;;;;

  ; Function is an anonymous class from the perspective of JVM
  ; http://stackoverflow.com/questions/3708516/what-type-is-a-function
  (is (instance? clojure.lang.IFn (fn [] "")))
  (is (= ((fn [] "Hello world")) "Hello world"))

  (def x 1)
  (is (= x 1))

  (def hello-world (fn [] "Hello world"))
  (is (= (hello-world) "Hello world"))

  (defn hello-world [] "Hello world")
  (is (= (hello-world) "Hello world"))

  (defn hello [name] (str "Hello " name))
  (is (= (hello "world") "Hello world"))

  (def hello2 #(str "Hello " %1))
  (is (= (hello "world") "Hello world"))

  (defn hello3
    ([] "Hello world")
    ([name] (str "Hello " name)))
  (is (= (hello3) "Hello world"))
  (is (= (hello3 "world") "Hello world"))

  ; Pack arguments up in a seq
  (defn count-args [& args]
    (str (count args) " args: " args))
  (is (= (count-args 1 2 3) "3 args: (1 2 3)"))

  (defn hello-count [name & args]
    (str "Hello " name ", args: " args))
  (is (= (hello-count "SH" 1 2 3) "Hello SH, args: (1 2 3)"))

  ;;;;;;
  ; Maps
  ;;;;;;

  (is (= (class {:a 1 :b 2 :c 3}) clojure.lang.PersistentArrayMap))
  (is (= (class (hash-map :a 1 :b 2 :c 3)) clojure.lang.PersistentHashMap))

  ; Maps can use any hashable type as a key, but usually keywords are best
  ; Keywords are like strings with some efficiency bonuses
  (is (= (class :a) clojure.lang.Keyword))

  ; Commas are treated as whiltespace and do nothing
  (def stringmap {"a" 1, "b" 2, "c" 3})
  (is (= (class stringmap) clojure.lang.PersistentArrayMap))
  (is (= (stringmap "a") 1))
  (is (= (stringmap "d") nil))

  (def keymap {:a 1 :b 2 :c 3})
  (is (= (keymap :a) 1))
  (is (= (:a keymap) 1))

  (def newkeymap (assoc keymap :d 4))
  (is (= newkeymap {:a 1 :b 2 :c 3 :d 4}))
  (is (= (dissoc newkeymap :c :d) {:a 1 :b 2}))

  ;;;;;;
  ; Sets
  ;;;;;;

  (is (= (class #{1 2 3}) clojure.lang.PersistentHashSet))
  (is (= (set [1 2 3 1 2]) #{1 2 3}))
  (is (= (conj #{1 2 3} 4) #{1 2 3 4}))
  (is (= (disj #{1 2 3} 3) #{1 2}))
  (is (= (#{1 2 3} 1) 1))
  (is (= (#{1 2 3} 4) nil))

  ;;;;;;;;;;;;;;
  ; Useful forms
  ;;;;;;;;;;;;;;

  (is (= (if false "a" "b") "b"))
  (is (= (if false "a") nil))

  ; Use let to create temporary bindings
  (is (= (let [a 1 b 2] (> b a))))

  ; Group statements together with do
  (do
    (def a 1)
    (def b 2)
    (is (< a b)))

  ; Functions have an implicit do
  (defn test-impl-do [name]
      (def n "SH")
      (is (= name n)))
  (test-impl-do "SH")

  ; So does let
  (let [name "SH"]
    (is (= name "SH"))
    (is (not (= name "HJ"))))

  ; Use the threading macros (-> and ->>)
  (is (= (->
           {:a 1 :b 2}
           (assoc :c 3)
           (dissoc :b))
         (dissoc (assoc {:a 1 :b 2} :c 3) :b)))

  (is (= (->>
           (range 10)
           (map inc)
           (filter odd?)
           (into [])) ;[1 3 5 7 9]))
         (vec (filter odd? (map inc (range 10))))))

  ;;;;;;;;;
  ; Modules
  ;;;;;;;;;

  ; Use "use" to get all functions from the module (outside of functions)
  ; Then we can use set operations
  (is (= (intersection #{1 2 3} #{2 3 4}) #{2 3}))
  (is (= (difference #{1 2 3} #{2 3 4}) #{1})) ; not #{1 4}

  ; Use '/' call functions from a module
  (is (= (clojure.string/blank? "")))
  ; #"" denotes a regular expression literal
  (is (= (str/replace "abcd" #"[a-b]" str/upper-case) "ABcd"))

  ;;;;;;
  ; Java
  ;;;;;;

  (is (= (instance? Date (Date.))))
  (is (> (. (Date.) getTime) 0))
  (is (> (.getTime (Date.)) 0))

  ; doto for mutable classes
  (is (= (instance? Date (doto (Calendar/getInstance)
           (.set 2000 1 1 0 0 0)
           .getTime))))

  (is (> (.getTime
           (.getTime
             (doto (Calendar/getInstance)
               (.set 2000 1 1 0 0 0)))) 0))

  ;;;;;
  ; STM
  ;;;;;

  ; Software Transactional Memory is the mechanism Clojure uses to handle
  ; persistent state. There are a few constructs in Clojure that use this.

  (def my-atom (atom {}))
  (is (= (swap! my-atom assoc :a 1) {:a 1}))
  (is (= (swap! my-atom assoc :b 2) {:a 1 :b 2}))
  (is (= (class my-atom) clojure.lang.Atom))
  (is (= @my-atom {:a 1 :b 2}))

  (def counter (atom 0))
  (defn inc-counter []
    (swap! counter inc))
  (inc-counter)
  (is (= @counter 1))
)
