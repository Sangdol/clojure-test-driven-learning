;;; Reader - http://clojure.org/reader
;;; TODO Special forms - http://clojure.org/special_forms
(ns clojure-learning-test.syntax-test
  (:require [clojure.test :refer :all]))

(deftest cond-test
  "switch"
  (is (= (cond
           (> 1 1) "hey"
           (> 2 2) "no"
           :else true) true))
  )

(deftest macroexpand-test
  (is (= (macroexpand '(when 1 2 3)) '(if 1 (do 2 3))))
  )

(deftest if-when-test
  (is (= (if 1 2) 2))
  (is (= (if nil 1 2) 2))
  (is (= (when 1 (def x 2) x) 2))
  )

(deftest metadata-test
  (is (= (meta ^{:any "hoy"} [1]) {:any "hoy"}))
  (is (= (meta (with-meta [1] {:any "hoy"})) {:any "hoy"}))
  (is (= (meta ^String [1]) {:tag java.lang.String}))
  (is (= (meta ^Long [1]) {:tag java.lang.Long}))
  (is (= (meta ^:dynamic [1]) {:dynamic true}))
  )

(deftest dispatch-test
  "#"
  (is (= (class #{}) clojure.lang.PersistentHashSet))
  (is (= (class #"") java.util.regex.Pattern)) ; Compiled at read time
  (is (= (class #'meta) clojure.lang.Var))
  (is (= #'meta (var meta)))
  (is (instance? clojure.lang.IFn  #()))
  (is true #_ignore-this)
  )

(deftest quote-and-syntax-quote-test
  "https://blog.8thlight.com/colin-jones/2012/05/22/quoting-without-confusion.html"
  (is (= `a 'clojure-learning-test.syntax-test/a))
  (is (= 'a (quote a)))
  (is (= `(abc ~(symbol "def") ~'ghi) '(clojure-learning-test.syntax-test/abc def ghi)))
  (is (= `(max ~@(range 3)) '(clojure.core/max 0 1 2)))
  (is (= `(let (+ 1 1)) '(clojure.core/let (clojure.core/+ 1 1))))
  (is (= (count (distinct `(foo# foo#))) 1)) ; output of clojure.core/gensym e.g. foo_1865__auto__. to avoid variable capture problems
  (is (= `[:a c] [:a 'clojure-learning-test.syntax-test/c]))
  (is (= `[:a c] `[:a ~`c]))
  (is (= `{:a '~(+ 1 1)} {:a '(quote 2)}))
  (is (= `{:a '~@(list 1 2)} {:a '(quote 1 2)}))
  (is (= `(1 (2 3)) '(1 (2 3))))
  (is (= `(1 `(2 3)) '(1 (clojure.core/seq (clojure.core/concat (clojure.core/list 2) (clojure.core/list 3))))))
  (is (= (eval `(list 1 `(2 3))) '(1 (2 3))))
  )

(defn get-if-mod-of-3-5 [n]
  "https://clojuredocs.org/clojure.core/defn"
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

(deftest loop-test
  (is (= (for [x [0 1] :let [y (* x 2)]] y), [0 2]))
  (is (= (for [x [0 1] :let [y (* x 2)] :when (> y 0)] y), [2]))
  (def digits (seq [1 2]))
  (is (= (for [x1 digits x2 digits x3 digits] (* x1 x2 x3))
         [1 2 2 4 2 4 4 8]))
  (is (= (for [x digits y digits z digits :when (= x y z)] x) [1 2]))
  (is (= (for [x digits y digits z digits :while (= x y z)] x) [1])))

(deftest class-test
  (is (= (class 1) Long))
  (is (= (class 1.) Double))
  (is (= (class "") String))
  (is (= (class false) Boolean))
  (is (= (class nil) nil))
  (is (= (class clojure.lang.IFn) java.lang.Class)))

(deftest literal-test
  (is (not= '(+ 1 2) (+ 1 2)))
  (is (= '(+ 1 2) (list '+ 1 2)))
  (is (= (eval '(+ 1 2)) (+ 1 2)))
  (is (= (first (first '((1)))) 1))
  (is (= '((1)) [[1]])))

(deftest function-test
  "
  Function is an anonymous class from the perspective of JVM
  http://stackoverflow.com/questions/3708516/what-type-is-a-function
  "
  (is (instance? clojure.lang.IFn (fn [] "")))
  (is (= ((fn [] "Hello world")) "Hello world"))
  (testing "name of fn can be used for internal recursion
      http://stackoverflow.com/questions/10490513/how-to-do-recursion-in-anonymous-fn-without-tail-recursion"
    (is (= ((fn pow [n e]
              (if (zero? e)
                1
                (* n (pow n (dec e))))) 2 3) 8)))

  (def x 1)
  (is (= x 1))

  (def hello-world (fn [] "Hello world"))
  (is (= (hello-world) "Hello world"))

  (defn hello-world [] "Hello world")
  (is (= (hello-world) "Hello world"))

  (defn hello [name] (str "Hello " name))
  (is (= (hello "world") "Hello world"))

  (def hello2 #(str "Hello " %1))
  (is (= (hello2 "world") "Hello world"))

  (def hello2-1 #(str "Hello " %))
  (is (= (hello2-1 "world") "Hello world"))

  ;; #([%]) - syntax error
  ;; http://stackoverflow.com/questions/4921566/clojure-returning-a-vector-from-an-anonymous-function
  (is (= (#(vector %1) 1) [1]))

  (defn hello3
    ([] "Hello world")
    ([name] (str "Hello " name))
    ([name age] (str "Hello " name "(" age ")")))
  (is (= (hello3) "Hello world"))
  (is (= (hello3 "world") "Hello world"))
  (is (= (hello3 "world" 33) "Hello world(33)"))

  ;; Pack arguments up in a seq
  (defn count-args [& args]
    (str (count args) " args: " args))
  (is (= (count-args 1 2 3) "3 args: (1 2 3)"))

  (defn hello-count [name & args]
    (str "Hello " name ", args: " args))
  (is (= (hello-count "SH" 1 2 3) "Hello SH, args: (1 2 3)"))
  )

(deftest useful-forms-test
  (is (= (if false "a" "b") "b"))
  (is (= (if false "a") nil))

  ;; Use let to create temporary bindings
  (is (= (let [a 1 b 2] (> b a))))

  ;; Group statements together with do
  (do
    (def a 1)
    (def b 2)
    (is (< a b)))

  ;; Functions have an implicit do
  (defn test-impl-do [name]
    (def n "SH")
    (is (= name n)))
  (test-impl-do "SH")

  ;; So does let
  (let [name "SH"]
    (is (= name "SH"))
    (is (not (= name "HJ"))))

  ;; letfn
  ;; https://clojuredocs.org/clojure.core/letfn
  (letfn [(add-5 [x]
            (+ x 5))]
    (is (= (add-5 3) 8)))

  ;; Use the threading macros (-> and ->>)
  ;; thread-first
  (is (= (->
           {:a 1 :b 2}
           (assoc :c 3)
           (dissoc :b))
         (dissoc (assoc {:a 1 :b 2} :c 3) :b)))

  ;; thread-last
  ;; http://stackoverflow.com/questions/26034376/clojure-thread-first-macro-and-thread-last-macro
  (is (= (->>
           (range 10)
           (map inc)
           (filter odd?)
           (into [])) ;;[1 3 5 7 9]))
         (vec (filter odd? (map inc (range 10))))))
  )

(deftest stm-test
  "
  Software Transactional Memory is the mechanism Clojure uses to handle
  persistent state. There are a few constructs in Clojure that use this.
  "
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
