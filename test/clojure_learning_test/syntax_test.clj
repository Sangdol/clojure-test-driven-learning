;; Comment guideline
;; https://github.com/bbatsov/clojure-style-guide#comments
(comment
  Reader - http://clojure.org/reader

  TODO Special forms - http://clojure.org/special_forms)

;; Rich comment block
;; https://betweentwoparens.com/rich-comment-blocks#example-comments-ref
(comment "
  Another way to put a multiline comment.
  This form doesn't need to follow the rule of the reader.
")

;; Discard comment
#_(can I just put like this?
       This is another way to put a multiline comment)

#_("
  Another way to put anything like this:
  https://clojure.org/reference/data_structures#Keywords
  ")

(ns clojure-learning-test.syntax-test
  (:require [clojure.test :refer :all]))

(deftest def-and-function-test
  "
  Function is an anonymous class from the perspective of JVM
  http://stackoverflow.com/questions/3708516/what-type-is-a-function
  "
  (is (instance? clojure.lang.IFn (fn [] "")))
  (is (= "Hello world" ((fn [] "Hello world"))))
  (testing "name of fn can be used for internal recursion
      http://stackoverflow.com/questions/10490513/how-to-do-recursion-in-anonymous-fn-without-tail-recursion"
    (is (= ((fn pow [n e]
              (if (zero? e)
                1
                (* n (pow n (dec e))))) 2 3) 8)))

  (def x 1)
  (is (= x 1))

  (def hello-world (fn [] "Hello world"))
  (is (= "Hello world" (hello-world)))

  (defn hello-world [] "Hello world")
  (is (= "Hello world" (hello-world)))

  (defn hello [name] (str "Hello " name))
  (is (= "Hello world" (hello "world")))

  (def hello2 #(str "Hello " %1))
  (is (= "Hello world" (hello2 "world")))

  (def hello2-1 #(str "Hello " %))
  (is (= "Hello world" (hello2-1 "world")))

  ;; #([%]) - syntax error
  ;; http://stackoverflow.com/questions/4921566/clojure-returning-a-vector-from-an-anonymous-function
  (is (= [1] (#(vector %1) 1)))

  (is (= 6 (#(reduce + %&) 1 2 3)))

  ;; arity overloading
  (defn hello3
    ([] "Hello world")
    ([name] (str "Hello " name))
    ([name age] (str "Hello " name "(" age ")")))
  (is (= "Hello world" (hello3)))
  (is (= "Hello world" (hello3 "world")))
  (is (= "Hello world(33)" (hello3 "world" 33)))

  ;; Pack arguments up in a seq
  (defn count-args [& args]
    (str (count args) " args: " args))
  (is (= "3 args: (1 2 3)" (count-args 1 2 3)))

  (defn hello-count [name & args]
    (str "Hello " name ", args: " args))
  (is (= "Hello SH, args: (1 2 3)" (hello-count "SH" 1 2 3)))

  (defn default-arg-func
    [a & {:keys [b] :or {b 1}}]
    (+ a b))
  (is (= 2 (default-arg-func 1)))
  (is (= 10 (default-arg-func 5 :b 5))))


(deftest let-test
  (let [name "SH"]
    (is (= name "SH"))
    (is (not (= name "HJ"))))

  (defn f []
    (let [a "a"]
      {:a a}))

  (is (= {:a "a"} (f)))

  (letfn [(add-5 [x]
            (+ x 5))]
    (is (= 8 (add-5 3))))

  (letfn [(add-3 [x] (+ x 3))]
    (let [x 1]
      (is (= 4 (add-3 x))))))

(deftest cond-test
  "switch"
  (is (= (cond
           (> 1 1) "hey"
           (> 2 2) "no"
           :else true) true)))


;;; special forms
(deftest destructuring-test
  (let [[a b c & d :as e] [1 2 3 4 5 6]]
    (is (= [a b c d e] [1 2 3 '(4 5 6) [1 2 3 4 5 6]])))

  (let [[[a b][c d]] [[1 2][3 4]]]
    (is (= [a b c d] [1 2 3 4])))

  (let [[a b & c :as str] "abcd"]
    (is (= [a b c str] [\a \b '(\c \d) "abcd"])))

  (let [{a :a, b :b, :as m :or {a 2 b 3}} {:a 5 :c 6}]
    (is (= [a b m] [5 3 {:a 5 :c 6}])))

  ; :keys reduces repetition e.g., {abc :abc ...}
  (let [{:keys [a b c]} {:a 1 :b 2}]
    (is (= [a b c] [1 2 nil])))

  ; it works with a list as well
  (let [{:keys [a b c]} '(:a 1 :b 2)]
    (is (= [a b c] [1 2 nil])))

  ; associative destructuring
  ; \a: char a
  (let [{first 0 third 2} "abcd"]
    (is (= [first third] [\a \c])))

  (let [m {:x/a 1, :y/b 2}
        {:keys [x/a y/b]} m]
    (is (= [a b] [1 2])))

  ; auto-resolved keyword
  ; what is that? http://stackoverflow.com/questions/2481984/when-should-clojure-keywords-be-in-namespaces
  (let [m {::a 1, ::b 2}
        {:keys [::a ::b]} m]
    (is (= [a b] [1 2])))

  (letfn [(keysfn [{:keys [name pw]}]
            (str name " "pw))]
    (is (= "name pw" (keysfn {:name "name" :pw "pw"})))))


(deftest if-when-test
  (is (= 2 (if 1 2)))
  (is (= 2 (if nil 1 2)))

  ;; 'when' is like 'if' + 'do' but without 'else'
  (is (= 2 (when 1 (def x 2) x)))
  (when true
    (is (= 1 1))
    (is (= 2 2)))

  ; (if :truthy-thing)
  ;   (let [res :truthy-thing] (println res))
  ;
  ; the above code can be reduced using if-let:
  ;
  ; (if-let [res :truthy-thing] (println res)
  (let [x [1 2] y []]
    (is (= 1 (if-let [a (seq x)] (first a) "else")))
    (is (= "else" (if-let [a (seq y)] (first a) "else"))))


  ;; when-let binds if the value is logical true
  (let [x [1 2] y []]
    (is (= 1 (when-let [a (seq x)] (first a))))
    ;; seq returns nil if the collection is empty
    (is (= nil (when-let [a (seq y)] (first a))))))
    ;(is (= 1 (when-let [a (seq x) b (seq y)] (cons a b)))) ;; cannot bind more than 1 in when-let


;; https://clojure.org/reference/metadata
(deftest metadata-test
  (is (= {:any "hoy"} (meta ^{:any "hoy"} [])))
  (is (= nil (meta ^{:any "hoy"} 'a)))  ; why is this nil?
  (is (= {:any "hoy"} (meta (with-meta 'data {:any "hoy"}))))

  (is (= {:tag java.lang.String} (meta ^String [])))
  (is (= {:tag java.lang.Long} (meta ^Long [])))

  (is (= {:dynamic true} (meta ^:dynamic [])))

  (let [wm (with-meta [] {:a 10})]
    (is (= {:a 10} (meta wm)))
    (is (= {:a 10 :b 20} (meta (vary-meta wm assoc :b 20))))
    (is (= {:a 11 :c 1} (meta (vary-meta wm merge {:a 11 :c 1}))))))


(deftest dispatch-test
  "#"
  (is (= clojure.lang.PersistentHashSet (class #{})))
  (is (= java.util.regex.Pattern (class #""))) ; Compiled at read time
  (is (= clojure.lang.Var (class #'meta)))
  (is (= #'meta (var meta)))
  (is (instance? clojure.lang.IFn  #()))
  (is true #_ignore-this))


(deftest symbol-quote-and-syntax-quote-test
  "https://blog.8thlight.com/colin-jones/2012/05/22/quoting-without-confusion.html"
  (is (= `a 'clojure-learning-test.syntax-test/a))
  (is (= 'a (quote a)))
  (is (= 'a (symbol 'a)))
  (is (= 'a (symbol "a")))

  ;; unquote
  (is (= `(abc ~(symbol "def") ~'ghi) '(clojure-learning-test.syntax-test/abc def ghi)))

  ;; unquote-splicing
  (is (= `(max ~@(range 3)) '(clojure.core/max 0 1 2)))

  ;; run 'reduce' instead of splicing
  (is (= `(max ~(reduce + (range 3))) '(clojure.core/max 3)))

  ;; unquoting vs. not-unquoting
  (is (= `(let ~(+ 1 1)) '(clojure.core/let 2)))
  (is (= `(let (+ 1 1)) '(clojure.core/let (clojure.core/+ 1 1))))

  ; output of clojure.core/gensym e.g. foo_1865__auto__. to avoid variable capture problems
  (is (= (count (distinct `(foo# foo#))) 1))

  (is (= `[:a c] [:a 'clojure-learning-test.syntax-test/c]))
  (is (= `[:a c] `[:a ~`c]))
  (is (= `{:a '~(+ 1 1)} {:a '(quote 2)}))
  (is (= `{:a '~@(list 1 2)} {:a '(quote 1 2)}))
  (is (= `(1 (2 3)) '(1 (2 3))))
  (is (= `(1 `(2 3)) '(1 (clojure.core/seq (clojure.core/concat (clojure.core/list 2) (clojure.core/list 3))))))
  (is (= (eval `(list 1 `(2 3))) '(1 (2 3)))))


(deftest loop-test
  ;; The difference is that for builds a lazy sequence and returns it
  ;; while doseq is for executing side-effects and returns nil.
  ;; https://stackoverflow.com/questions/4725417/difference-between-doseq-and-for-in-clojure/5287434
  (testing "for loop"
    (is (= [0 2] (for [x [0 1] :let [y (* x 2)]] y)))
    (is (= [2] (for [x [0 1] :let [y (* x 2)] :when (> y 0)] y)))
    (def digits (seq [1 2]))
    (is (= [1 2 2 4 2 4 4 8]
           (for [x1 digits x2 digits x3 digits] (* x1 x2 x3))))
    (is (= [1 2] (for [x digits y digits z digits :when (= x y z)] x)))
    (is (= [1] (for [x digits y digits z digits :while (= x y z)] x))))

  (testing "for loop with index"
    (is (= [[0 'a] [1 'b]] (for [[i c] (map-indexed vector ['a 'b])]
                             [i c]))))

  ;; recur avoids stack consumption and stackoverflow.
  ;; JVM doesn't support tail call optimization.
  ;; recur is an explicit way of the optimization.
  (testing "loop loop"
    (is (= 120 (loop [x 5 acc 1] ;; factorial
                 (if (> x 1)
                   (recur (dec x) (* acc x))
                   acc)))))

  (testing "zip"
    (is (= [[1 "a"] [2 "b"]] (map vector [1 2] ["a" "b"])))))


(deftest class-test
  (is (= Long (class 1)))
  (is (= Double (class 1.)))
  (is (= String (class "")))
  (is (= Boolean (class false)))
  (is (= nil (class nil)))
  (is (= java.lang.Class (class clojure.lang.IFn))))


(deftest literal-test
  (is (not= '(+ 1 2) (+ 1 2)))
  (is (= '(+ 1 2) (list '+ 1 2)))
  (is (= (+ 1 2) (eval '(+ 1 2))))
  (is (= (first (first '((1)))) 1))
  (is (= '((1)) [[1]])))


(deftest useful-forms-test
  (is (= "b" (if false "a" "b")))
  (is (= nil (if false "a")))

  ;; Use let to create temporary bindings
  (let [a 1 b 2]
    (is (> b a)))

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

  ;; Use the threading macros (-> and ->>)
  ;; thread-first
  (is (= (->
           {:a 1 :b 2}
           (assoc :c 3)
           (dissoc :b))
         (dissoc (assoc {:a 1 :b 2} :c 3) :b)))

  ;; you need to put an extra parens for anonymous functions
  ;; https://stackoverflow.com/questions/10740265/threading-macro-with-anonymous-functions
  (is (= (->
           {:a 1 :b 2}
           (#(assoc % :c 3))
           ((fn [x] (dissoc x :b))))
         (dissoc (assoc {:a 1 :b 2} :c 3) :b)))

  (is (= (->
           10
           -) -10))

  ;; thread-last
  ;; http://stackoverflow.com/questions/26034376/clojure-thread-first-macro-and-thread-last-macro
  (is (= (->>
           (range 10)
           (map inc)
           (filter odd?)
           (into [])) ;;[1 3 5 7 9]))
         (vec (filter odd? (map inc (range 10))))))

  (is (= (->>
           (range 10)
           (reduce + 0)) 45))

  (is (= (->>
           10
           -) -10)))


(deftest stm-test
  "
  Software Transactional Memory is the mechanism Clojure uses to handle
  persistent state. There are a few constructs in Clojure that use this.
  "
  (def my-atom (atom {}))
  (is (= {:a 1} (swap! my-atom assoc :a 1)))
  (is (= {:a 1 :b 2} (swap! my-atom assoc :b 2)))
  (is (= clojure.lang.Atom (class my-atom)))
  (is (= @my-atom {:a 1 :b 2}))

  (def counter (atom 0))
  (defn inc-counter []
    (swap! counter inc))
  (inc-counter)
  (is (= @counter 1)))

