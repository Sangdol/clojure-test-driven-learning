;;; References
;;; * Test API https://clojure.github.io/clojure/clojure.test-api.html
;;; * In y minutes http://learnxinyminutes.com/docs/clojure/
;;; * Docs https://clojuredocs.org/

(ns clojure-learning-test.core-test
  (:require [clojure.test :refer :all]
            [clojure.math.numeric-tower :as math]))

(deftest misc-test
  (is (every? odd? [1 3]))
  (is (not-any? odd? [2 4]))
  (is (some odd? [1 2]))
  (is (= 1 (when true 1)))
  (is (= nil (when false 1)))
  (is (= [1] (butlast [1 2])))
  (is (= [1 3 5] (remove even? [1 2 3 4 5])))
  (is (= [1 1 1] (repeat 3 1)))
  (is (= 10 ((constantly 10) 3)))
  (is (= [[1 2] [3 4 5]] (split-at 2 [1 2 3 4 5])))
  (is (= {'a 3 'b 1} (frequencies ['a 'a 'a 'b]))))


(deftest fnil-test
  (letfn [(hello [h w] (str h " " w))]
    (let [hello-world (fnil hello "Hello" "world")]
      (is (= "Hello world" (hello-world nil nil)))
      (is (= "Hi world" (hello-world "Hi" nil)))
      (is (= "Hello earth" (hello-world nil "earth"))))))

(deftest var-test
  "The symbol must resolve to a var, and the Var object itself is returned."
  (is (= #'clojure.core/defn #'defn))
  (is (= #'clojure.core/defn (var defn)))
  (is (= #'clojure.core/+ (var +))))


(deftest partition-test
  (is (= '((0 1) (2 3)) (partition 2 (range 4))))
  (is (= '((0 1)) (partition 2 (range 3))))
  (is (= '((0 1) (3 4)) (partition 2 3 (range 6))))
  (is (= '((0 1) (1 2) (2 3) (3 4) (4 5)) (partition 2 1 (range 6))))
  (is (= '((0 1 2)) (partition 3 3 (range 4))))
  (is (= '((0 1 2) (3)) (partition 3 3 [] (range 4))))
  (is (= '((0 1 2) (3 10)) (partition 3 3 [10] (range 4))))

  (is (= '((0 1) (2)) (partition-all 2 (range 3))))
  (is (= '((0 1) (3)) (partition-all 2 3 (range 4)))))


(deftest repeatedly-test
  (is (= 1 (count (set (repeat 5 (rand-int 1000))))))
  (is (< 1 (count (set (repeatedly 5 #(rand-int 1000)))))))


(deftest interleave-test
  (is (= [1 2 1 2 1 2] (interleave [1 1 1] [2 2 2])))
  (is (= [1 2 3 1 2 3] (interleave [1 1] [2 2] [3 3])))
  (is (= [1 2 1 2] (interleave [1 1] [2 2 2]))))


(deftest interpose-test
  (is (= [1 0 2 0 3] (interpose 0 [1 2 3])))
  (is (= "1, 2, 3" (apply str (interpose ", " ["1", "2", "3"])))))


(deftest pred-test
  (is (odd? 1))
  (is (even? 0))
  (is (pos? 1))
  (is (neg? -1))
  (is (zero? 0))
  (is (nil? nil))
  (is (some? 1))                                            ; for not-nil?
  (is (complement neg?) 0)
  (is (complement neg?) 1))


(deftest concat-mapcat-test
  (is (= '(1 2 3 4) (concat [1 2] '(3 4))))
  (is (= [1 2 [1] :b :a] (concat [1 2] nil '([1]) #{:a :b})))
  (is (= '(\a \b \c \d) (concat "ab" "cd")))

  ;; mapcat = map and concat
  (is (= [0 1 2 3] (mapcat reverse [[1 0] [3 2]])))
  (is (= [1 2 2 3 3 4] (mapcat (fn [n] [n (+ n 1)]) [1 2 3])))
  (is (= [1 2 1 2] (mapcat list [1 1] [2 2]))))


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
          (set! y 100))))

  (is (= 3 (+ x y))))


(deftest keyword-and-symbol-test
  (is (= :abc (keyword "abc")))
  (is (= :abc/def (keyword "abc" "def")))
  (is (= nil (keyword 1)))                                  ; only convert strings

  ;; http://kotka.de/blog/2010/05/Did_you_know_III.html
  ;; :: -> qualified or prefixed keyword
  (is (= :clojure-learning-test.core-test/a ::a))
  (is (= :clojure.math.numeric-tower/abs ::math/abs))

  (is (= 'abc (symbol "abc")))
  (is (= 'abc/def (symbol "abc" "def")))
  (is (not (identical? 'a 'a)))
  (is (= 'a 'a))
  (let [x 'a, y x]
    (is (identical? x y))))


(deftest truthy-falsey-test
  "only 'false' and 'nil' are falsey"
  (is "a")
  (is 0)
  (is -1)
  (is true)
  (is [])
  (is (not nil))
  (is (not false))

  ; to differentiate 'nil' and 'false'
  (is (nil? nil))
  (is (false? false))
  (is (not (false? nil)))

  ; to check emptiness of a seq
  (is (not (seq [])))
  (is (empty? [])))


(deftest contains?-test
  "Searches indicies or keys"
  (is (contains? {:a 1 :b 2} :a))
  (is (contains? "a" 0))
  (is (not (contains? "a" 1)))
  (is (contains? [:a] 0))
  (is (not (contains? [:a] :a)))
  ; the members of a set are the keys
  (is (contains? #{"a" "b" "c"} "a"))

  ;; don't get confused with string contains
  (is (.contains "Abc def" "bc")))


(deftest iterate-test
  (is (= [0 1 2] (take 3 (iterate inc 0))))
  (is (= [1 1 1] (take 3 (iterate identity 1))))

  (def fiblet (iterate (fn [[a b]] [b (+ a b)]) [1 1]))
  (is (= '([1 1] [1 2] [2 3]) (take 3 fiblet)))
  (is (= [1 1 2 3 5] (take 5 (map first fiblet)))))


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

  (is (= (list \c \b \a) (reverse "abc")))                  ; Need to use clojure.string/reverse for getting "cba"
  (is (= "cba" (apply str (reverse "abc")))))


(deftest apply-reduce-test
  ;; https://stackoverflow.com/questions/3153396/clojure-reduce-vs-apply
  ;; apply is more idiomatic
  (is (= (apply + (range 10)) (reduce + (range 10))))

  ;; they are not the same
  (is (= {:a 1 :b 2} (apply hash-map [:a 1 :b 2])))
  (is (= {{{:a 1} :b} 2} (reduce hash-map [:a 1 :b 2]))))


(deftest some-test
  (is (= true (some even? '(1 2 3))))
  (is (= nil (some even? '(1 3 5))))
  (is (= 1 (some identity '(nil false 1))))
  (is (= 2 (some #(and (even? %) %) '(-1 3 1 2)))))


(deftest cons-conj-test
  ;; Use cons to add an item to the beginning of a list or vector
  (is (= [4 1 2 3] (cons 4 [1 2 3])))
  (is (= '(4 1 2 3) (cons 4 '(1 2 3))))
  (is (= '([] 1) (cons [] [1])))
  (is (= [[] 1] (cons [] [1])))
  (is (= (cons 1 '(2 3)) (conj '(2 3) 1)))

  ;; Conj add an item to a collection in the most effective way.
  ;; e.g.,
  ;;   - vec(ArrayList): add to the back
  ;;   - list(LinkedList): add to the front
  (is (= [1 2 3 4] (conj [1 2 3] 4)))
  (is (= '(4 1 2 3) (conj '(1 2 3) 4)))
  (is (= [1 2 3 4] (conj [1 2] 3 4)))
  (is (= [1 2 '(3 4)] (conj [1 2] '(3 4))))
  (is (= [1 2 [3 4]] (conj [1 2] [3 4])))
  (is (= {1 2 3 4} (conj {1 2} [3 4])))                     ;; Map
  (is (= {1 2 3 4 5 6} (conj {1 2} [3 4] {5 6})))

  (is (= [:a :b :c] '(:a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))

  ;; What is difference between cons and conj?
  ;; http://stackoverflow.com/questions/3008411/clojure-consseq-vs-conjlist
  ;; conj takes any number of arguments (conj = conjoin an item into a collection)
  (is (= '(6 5 4 1 2 3) (conj '(1 2 3) 4 5 6)))
  (is (= clojure.lang.PersistentList (class (conj '(1 2) 3))))
  ;; cons takes just one (cons = construct a seq)
  ;; (commenting this to avoid IDE error)
  ;(is (thrown? IllegalArgumentException (cons 1 2 3 '(4 5 6))))
  (is (= clojure.lang.Cons (class (cons 3 '(1 2)))))
  (is (= clojure.lang.PersistentList (class (next (cons 3 '(1 2))))))
  (is (= '(1 2) (next (cons 3 '(1 2))))))


(deftest partial-test
  (let [hundred-times (partial * 100)]
    (is (= 300 (hundred-times 3)))
    (is (= 600 (hundred-times 3 2))))

  (let [minus-from-hundred (partial - 100)]
    (is (= 70 (minus-from-hundred 30)))
    (is (= 50 (minus-from-hundred 30 20))))

  (letfn [(add-and-multiply [m]
            (partial (fn [m n] (* (+ m n) n)) m))]
    (is (= 15 ((add-and-multiply 2) 3)))))


(deftest map-filter-reduce-test
  (is (= '(2 3 4) (map inc '(1 2 3))))
  (is (= '((1 2) (1 2)) (map list [1 1] [2 2])))
  (is (= '(6 9) (map + [2 3] [2 3] [2 3])))
  (is (= '(6 9) (map + [2 3] [2 3] [2 3 3])))

  (is (= '(2) (filter even? '(1 2 3))))
  (is (= '(2) (filter even? '[1 2 3])))
  (is (= [[2 3]] (filter #(> (count %) 1) '[[1] [2 3]])))
  (is (= [] (filter #(> (count %) 1) [[7] [6] [5] [4]])))

  (is (= 10 (reduce + '(1 2 3 4))))
  (is (= -8 (reduce - '(1 2 3 4))))
  (is (= [1 2 3] (reduce conj [] '(1 2 3))))
  (is (= [[[0 1] 2] 3] (reduce cons 0 [[1] [2] [3]])))

  ;; count http://www.4clojure.com/problem/solutions/22
  (is (= 3 (count [1 2 3])))
  (is (= 3 (count "abc")))
  (is (= 3 (reduce #(and %2 (inc %1)) 0 [1 2 3])))
  (is (= 3 (reduce (fn [n _] (inc n)) 0 [1 2 3]))))


(deftest comp-test
  (let [muliply-and-minus (comp - *)
        countif (comp count filter)]
    (is (= -6 (muliply-and-minus 2 3)))
    (is (= 1 (countif even? [1 2 3])))))


(deftest apply-test
  (let [li ["a" "b" "c"]]
    (is (= "[\"a\" \"b\" \"c\"]" (str li)))
    (is (= "abc" (apply str li))))
  (is (= 10 (apply + 1 2 [3 4]))))


(deftest identity-test
  (is (= 1 (identity 1)))
  (is (= '(1 2 3) (filter identity [1 2 3 nil])))
  (is (= '((\H \H) (\a)) (partition-by identity "HHa"))))


(deftest read-string-test
  "read one object from string"
  (is (= 100 (read-string "100")))
  (is (= '(+ 1 2) (read-string "(+ 1 2)")))
  (is (= 3 (eval (read-string "(+ 1 2)")))))
