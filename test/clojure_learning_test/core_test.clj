;;; References
;;; * Test API https://clojure.github.io/clojure/clojure.test-api.html
;;; * In y minutes http://learnxinyminutes.com/docs/clojure/
;;; * Docs https://clojuredocs.org/

(ns clojure-learning-test.core-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]))

(deftest misc-test
  (is (= 1 (when true 1)))
  (is (= nil (when false 1)))
  (is (= [1] (butlast [1 2])))
  (is (= [1 3 5] (remove even? [1 2 3 4 5])))
  (is (= [1 1 1] (repeat 3 1)))
  )

(deftest interleave-test
  (is (= [1 2 1 2 1 2] (interleave [1 1 1] [2 2 2])))
  (is (= [1 2 3 1 2 3] (interleave [1 1] [2 2] [3 3])))
  )

(deftest pred-test
  (is (odd? 1))
  (is (even? 0))
  (is (pos? 1))
  (is (neg? -1))
  (is (zero? 0))
  (is (nil? nil))
  )

(deftest concat-mapcat-test
  (is (= '(1 2 3 4) (concat [1 2] '(3 4))))
  (is (= [1 2 [1] :b :a] (concat [1 2] nil '([1]) #{:a :b})))
  (is (= '(\a \b \c \d) (concat "ab" "cd")))

  ;; mapcat = map and concat
  (is (= [0 1 2 3] (mapcat reverse [[1 0] [3 2]])))
  (is (= [1 2 2 3 3 4] (mapcat (fn [n] [n (+ n 1)]) [1 2 3])))
  )

(deftest binding-test
  (is (thrown-with-msg?
        IllegalStateException
        #"Can't dynamically bind non-dynamic var"
        (binding [max min] ())))

  (def ^:dynamic x 1)
  (def ^:dynamic y 2)
  (binding [x 10]
    (is (= 12 (+ x y)))
    (set! x 100)
    (is (= 102 (+ x y)))
    (is (thrown-with-msg?
          IllegalStateException
          #"Can't change/establish root binding of"
          (set! y 100)))
    )
  (is (= 3 (+ x y)))
  )

(deftest keyword-and-symbol-test
  (is (= :abc (keyword "abc")))
  (is (= :abc/def (keyword "abc" "def")))
  (is (= nil (keyword 1))) ; only convert strings

  ;; http://kotka.de/blog/2010/05/Did_you_know_III.html
  (is (= :clojure-learning-test.core-test/a ::a))
  (is (= :clojure.math.numeric-tower/abs ::math/abs))

  (is (= 'abc (symbol "abc")))
  (is (= 'abc/def (symbol "abc" "def")))
  )

(deftest truthy-falsey-test
  "only 'false' and 'nil' are falsey"
  (is "a")
  (is 0)
  (is -1)
  (is true)
  (is (not nil))
  (is (not false))
  )

(deftest contains?-test
  "Searches indicies or keys"
  (is (contains? {:a 1 :b 2} :a))
  (is (contains? "a" 0))
  (is (not (contains? "a" 1)))
  (is (contains? [:a] 0))
  (is (not (contains? [:a] :a)))
  (is (contains? #{"a" "b" "c"} "a")) ; the members of a set are the keys
  )

(deftest iterate-test
  (is (= [0 1 2] (take 3 (iterate inc 0))))
  (is (= [1 1 1] (take 3 (iterate identity 1))))

  (def fiblet (iterate (fn [[a b]] [b (+ a b)]) [1 1]))
  (is (= '([1 1] [1 2] [2 3]) (take 3 fiblet)))
  (is (= [1 1 2 3 5] (take 5 (map first fiblet))))
  )

(deftest reverse-test
  "
  http://www.4clojure.com/problem/23#prob-title
  http://www.4clojure.com/problem/solutions/23
  "
  (is (= [2 1] (reverse [1 2])))
  (is (= [2 1] (rseq [1 2])))
  (is (= [2 1] (reduce conj () [1 2])))
  (is (= [2 1] (apply conj () [1 2])))
  (is (= [2 1] (into () [1 2])))

  (is (= (list \c \b \a) (reverse "abc"))) ; Need to use clojure.string/reverse for getting "cba"
  (is (= "cba" (apply str (reverse "abc"))))
  )

(deftest some-test
  (is (= true (some even? '(1 2 3))))
  (is (= nil (some even? '(1 3 5))))
  (is (= 1 (some identity '(nil false 1))))
  (is (= 2 (some #(and (even? %) %) '(-1 3 1 2))))
  )

(deftest cons-conj-test
  ;; Use cons to add an item to the beginning of a list or vector
  (is (= [4 1 2 3] (cons 4 [1 2 3])))
  (is (= '(4 1 2 3) (cons 4 '(1 2 3))))
  (is (= '([] 1) (cons [] [1])))
  (is (= [[] 1] (cons [] [1])))

  ;; Conj add an item to a collection in the most effective way.
  (is (= [1 2 3 4] (conj [1 2 3] 4)))
  (is (= '(4 1 2 3) (conj '(1 2 3) 4)))
  (is (= [1 2 3 4] (conj [1 2] 3 4)))
  (is (= [1 2 '(3 4)] (conj [1 2] '(3 4))))
  (is (= [1 2 [3 4]] (conj [1 2] [3 4])))
  (is (= {1 2 3 4} (conj {1 2} [3 4]))) ;; Map
  (is (= {1 2 3 4 5 6} (conj {1 2} [3 4] {5 6})))

  (is (= [:a :b :c] '(:a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))

  ;; What is difference between cons and conj?
  ;; http://stackoverflow.com/questions/3008411/clojure-consseq-vs-conjlist
  ;; conj takes any number of arguments (conj = conjoin an item into a collection)
  (is (= '(6 5 4 1 2 3) (conj '(1 2 3) 4 5 6)))
  (is (= clojure.lang.PersistentList (class (conj '(1 2) 3))))
  ;; cons takes just one (cons = construct a seq)
  (is (thrown? IllegalArgumentException (cons 1 2 3 '(4 5 6))))
  (is (= clojure.lang.Cons (class (cons 3 '(1 2)))))
  (is (= clojure.lang.PersistentList (class (next (cons 3 '(1 2))))))
  (is (= '(1 2)) (next (cons 3 '(1 2)))))

(deftest partial-test
  ;; partial
  ;; https://clojuredocs.org/clojure.core/partial
  (def hundred-times (partial * 100))
  (is (= 300 (hundred-times 3)))
  (is (= 600 (hundred-times 3 2)))

  (def minus-from-hundred (partial - 100))
  (is (= 70 (minus-from-hundred 30)))
  (is (= 50 (minus-from-hundred 30 20)))

  (defn add-and-multiply [m]
    (partial (fn [m n] (* (+ m n) n)) m))
  (is (= 15 ((add-and-multiply 2) 3)))
  )

(deftest map-filter-reduce-test
  (is (= '(2 3 4) (map inc '(1 2 3))))
  (is (= '(2) (filter even? '(1 2 3))))
  (is (= '(2) (filter even? '[1 2 3])))
  (is (= 10 (reduce + '(1 2 3 4))))
  (is (= -8 (reduce - '(1 2 3 4))))
  (is (= [1 2 3] (reduce conj [] '(1 2 3))))
  (is (= [[[0 1] 2] 3] (reduce cons 0 [[1] [2] [3]])))

  ;; count http://www.4clojure.com/problem/solutions/22
  (is (= 3 (count [1 2 3])))
  (is (= 3 (count "abc")))
  (is (= 3 (reduce #(and %2 (inc %1)) 0 [1 2 3])))
  (is (= 3) (reduce (fn [n _] (inc n)) 0 [1 2 3])))

(deftest comp-test
  "
  https://clojuredocs.org/clojure.core/comp
  "
  (let [muliply-and-minus (comp - *)
        countif (comp count filter)]
    (is (= -6 (muliply-and-minus 2 3)))
    (is (= 1) (countif even? [1 2 3])))
  )

(deftest apply-test
  "
  https://clojuredocs.org/clojure.core/apply
  "
  (let [li ["a" "b" "c"]]
    (is (= "[\"a\" \"b\" \"c\"]" (str li)))
    (is (= "abc") (apply str li)))
  (is (= 10 (apply + 1 2 [3 4])))
  )


(deftest identity-test
  (is (= 1 (identity 1)))
  (is (= '(1 2 3) (filter identity [1 2 3 nil])))
  (is (= '((\H \H) (\a)) (partition-by identity "HHa")))
  )
