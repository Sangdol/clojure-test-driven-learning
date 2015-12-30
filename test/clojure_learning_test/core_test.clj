;;; References
;;; * Test API https://clojure.github.io/clojure/clojure.test-api.html
;;; * In y minutes http://learnxinyminutes.com/docs/clojure/
;;; * Docs https://clojuredocs.org/

(ns clojure-learning-test.core-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]))

(deftest misc-test
  (is (= (when true 1) 1))
  (is (= (when false 1) nil))
  (is (= (butlast [1 2]) [1]))
  (is (= (remove even? [1 2 3 4 5]) [1 3 5]))
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
  (is (= (concat [1 2] '(3 4)) '(1 2 3 4)))
  (is (= (concat [1 2] nil '([1]) #{:a :b}) [1 2 [1] :b :a]))
  (is (= (concat "ab" "cd") '(\a \b \c \d)))

  ;; mapcat = map and concat
  (is (= (mapcat reverse [[1 0] [3 2]]) [0 1 2 3]))
  (is (= (mapcat (fn [n] [n (+ n 1)]) [1 2 3]) [1 2 2 3 3 4]))
  )

(deftest binding-test
  (is (thrown-with-msg?
        IllegalStateException
        #"Can't dynamically bind non-dynamic var"
        (binding [max min] ())))

  (def ^:dynamic x 1)
  (def ^:dynamic y 2)
  (binding [x 10]
    (is (= (+ x y) 12))
    (set! x 100)
    (is (= (+ x y) 102))
    (is (thrown-with-msg?
          IllegalStateException
          #"Can't change/establish root binding of"
          (set! y 100)))
    )
  (is (= (+ x y) 3)))

(deftest keyword-and-symbol-test
  (is (= (keyword "abc") :abc))
  (is (= (keyword "abc" "def") :abc/def))
  (is (= (keyword 1) nil)) ; only convert strings

  ;; http://kotka.de/blog/2010/05/Did_you_know_III.html
  (is (= ::a :clojure-learning-test.core-test/a))
  (is (= ::math/abs :clojure.math.numeric-tower/abs))
  
  (is (= (symbol "abc") 'abc))
  (is (= (symbol "abc" "def") 'abc/def))
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
  (def fiblet (iterate (fn [[a b]] [b (+ a b)]) [1 1]))
  (is (= (take 3 fiblet) '([1 1] [1 2] [2 3])))
  (is (= (take 5 (map first fiblet)) [1 1 2 3 5]))
  )

(deftest reverse-test
  "
  http://www.4clojure.com/problem/23#prob-title
  http://www.4clojure.com/problem/solutions/23
  "
  (is (= (reverse [1 2]) [2 1]))
  (is (= (rseq [1 2]) [2 1]))
  (is (= (reduce conj () [1 2]) [2 1]))
  (is (= (apply conj () [1 2]) [2 1]))
  (is (= (into () [1 2]) [2 1]))
  
  (is (= (reverse "abc") (list \c \b \a))) ; Need to use clojure.string/reverse for getting "cba"
  (is (= (apply str (reverse "abc")) "cba"))
  )

(deftest some-test
  (is (= (some even? '(1 2 3)) true))
  (is (= (some even? '(1 3 5)) nil))
  (is (not= (some even? '(1 3 5)) false))
  (is (= (some identity '(nil false 1)) 1))
  (is (= (some #(and (even? %) %) '(-1 3 1 2)) 2))
  )

(deftest cons-conj-test
  ;; Use cons to add an item to the beginning of a list or vector
  (is (= (cons 4 [1 2 3]) [4 1 2 3]))
  (is (= (cons 4 '(1 2 3)) '(4 1 2 3)))
  (is (= (cons [] [1]) '([] 1)))
  (is (= (cons [] [1]) [[] 1]))

  ;; Conj add an item to a collection in the most effective way.
  (is (= (conj [1 2 3] 4) [1 2 3 4]))
  (is (= (conj '(1 2 3) 4) '(4 1 2 3)))
  (is (= (conj [1 2] 3 4) [1 2 3 4]))
  (is (= (conj [1 2] '(3 4)) [1 2 '(3 4)]))
  (is (= (conj [1 2] [3 4]) [1 2 [3 4]]))
  (is (= (conj {1 2} [3 4]) {1 2 3 4})) ;; Map
  (is (= (conj {1 2} [3 4] {5 6}) {1 2 3 4 5 6}))

  (is (= [:a :b :c] '(:a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))

  ;; What is difference between cons and conj?
  ;; http://stackoverflow.com/questions/3008411/clojure-consseq-vs-conjlist
  ;; conj takes any number of arguments (conj = conjoin an item into a collection)
  (is (= (conj '(1 2 3) 4 5 6) '(6 5 4 1 2 3)))
  (is (= (class (conj '(1 2) 3)) clojure.lang.PersistentList))
  ;; cons takes just one (cons = construct a seq)
  (is (thrown? IllegalArgumentException (cons 1 2 3 '(4 5 6))))
  (is (= (class (cons 3 '(1 2))) clojure.lang.Cons))
  (is (= (class (next (cons 3 '(1 2)))) clojure.lang.PersistentList))
  (is (= (next (cons 3 '(1 2))) '(1 2))))

(deftest partial-test
  ;; partial
  ;; https://clojuredocs.org/clojure.core/partial
  (def hundred-times (partial * 100))
  (is (= (hundred-times 3) 300))
  (is (= (hundred-times 3 2) 600))

  (def minus-from-hundred (partial - 100))
  (is (= (minus-from-hundred 30) 70))
  (is (= (minus-from-hundred 30 20) 50))

  (defn add-and-multiply [m]
    (partial (fn [m n] (* (+ m n) n)) m))
  (is (= ((add-and-multiply 2) 3) 15))
  )

(deftest map-filter-reduce-test
  (is (= (map inc '(1 2 3)) '(2 3 4)))
  (is (= (filter even? '(1 2 3)) '(2)))
  (is (= (filter even? '[1 2 3]) '(2)))
  (is (= (reduce + '(1 2 3 4)) 10))
  (is (= (reduce - '(1 2 3 4)) -8))
  (is (= (reduce conj [] '(1 2 3)) [1 2 3]))
  (is (= (reduce cons 0 [[1] [2] [3]]) [[[0 1] 2] 3]))

  ;; count http://www.4clojure.com/problem/solutions/22
  (is (= (count [1 2 3]) 3))
  (is (= (count "abc") 3))
  (is (= (reduce #(and %2 (inc %1)) 0 [1 2 3]) 3))
  (is (= (reduce (fn [n _] (inc n)) 0 [1 2 3]) 3)))

(deftest comp-test
  "
  https://clojuredocs.org/clojure.core/comp
  "
  (let [muliply-and-minus (comp - *)
        countif (comp count filter)]
    (is (= (muliply-and-minus 2 3) -6))
    (is (= (countif even? [1 2 3]) 1)))
  )

(deftest apply-test
  "
  https://clojuredocs.org/clojure.core/apply
  "
  (let [li ["a" "b" "c"]]
    (is (= (str li) "[\"a\" \"b\" \"c\"]"))
    (is (= (apply str li) "abc")))
  (is (= (apply + 1 2 [3 4]) 10))
  )


(deftest identity-test
  (is (= (identity 1) 1))
  (is (= (filter identity [1 2 3 nil]) '(1 2 3)))
  (is (= (partition-by identity "HHa") '((\H \H) (\a))))
  )
