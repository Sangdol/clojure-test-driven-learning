;; TODO Lazy - http://clojure.org/lazy
(ns clojure-learning-test.collection-test
  (:require [clojure.test :refer :all]))

(deftest list-accessor-test
  (is (= 2 (get [1 2] 1)))
  (is (= 2 (get {:a 1 :b 2} :b)))
  (is (= "missing" (get {:a 1 :b 2} :z "missing")))
  (is (= \a (get "abc" 0)))
  (is (= nil (get '(1 2) 0)))
  (is (= 1 (nth '(1 2) 0)))
  (is (= 1 (nth [1 2] 0)))

  (is (= 0 (.indexOf [1 2] 1)))

  (is (= [1] (butlast [1 2])))
  (is (= [1 2 3] (drop-last [1 2 3 4])))
  (is (= [1 2] (drop-last 2 [1 2 3 4])))
  (is (= [] (drop-last 4 [1 2 3 4])))

  ;; drop/nthnext
  (is (= '(3 4) (nthnext (range 5) 3)))
  (is (= '(3 4) (nthnext [0 1 2 3 4] 3)))
  (is (= nil (nthnext [] 3)))
  (is (= '(3 4) (drop 3 (range 5))))
  (is (= () (drop 3 []))) ; a lazy sequence

  ;; next/rest
  ;; http://stackoverflow.com/questions/4288476/clojure-rest-vs-next
  (is (= nil (next '(1))))
  (is (= '(2) (next '(1 2))))
  (is (= '() (rest '(1))))
  (is (= '(2) (rest '(1 2))))

  ; pop/peek
  (is (= 3 (peek [1 2 3])))
  (is (= [1 2] (pop [1 2 3])))
  (is (= 1 (peek '(1 2 3))))
  (is (= '(2 3) (pop '(1 2 3)) ))
  
  (is (= [1 2] ((juxt :a :b) {:a 1 :b 2 :c 3})))
  (is (= [:keyword "keyword"] ((juxt identity name) :keyword)))
  (is (= [13 72 3 6] ((juxt + * min max) 3 4 6)))

  (is (= [3 4] (subvec [1 2 3 4] 2)))
  (is (= [3] (subvec [1 2 3 4] 2 3)))
  )

(deftest seq-test
  (is (= '(1 2 3) (seq '(1 2 3))))
  (is (= '(\a \b) (seq "ab")))
  (is (= nil (seq nil)))
  (is (= nil (seq ())))
  (is (= nil (seq [])))
  (is (= nil (seq "")))
  (is (every? seq ['(1) [2] #{1} {:a 1}])) ; this is the recommended idiom for testing if a collection is not empty
  (is (= (list [:a 1] [:b 2]) (seq {:a 1 :b 2})))

  ;; "Sequences" (seqs) are abstract descriptions of lists of data.
  (is (seq? '(1 2 3)))
  (is (not (seq? [1 2 3])))
  (is (not (seq? "ab")))
  (is (not (seq? nil)))
  )

(deftest lazy-seq-test
  (defn fib [a b]
    (lazy-seq (cons a (fib b (+ a b)))))
  (is (= [1 1 2 3 5] (take 5 (fib 1 1))))
  )

(deftest tree-seq-test
  (is (= [[1 [2]] 1 [2] 2] (tree-seq coll? identity [1 [2]])))
  (is (= [[1 [2]] 1 [2] 2] (tree-seq coll? seq [1 [2]])))
  (is (= [[1 [2 [3]]] [2 [3]] [3]] (tree-seq next rest [1 [2 [3]]])))
  (is (= [1 2 3] (map first (tree-seq next rest [1 [2 [3]]]))))
  )

(deftest list-vec-test
  ;; What is difference between Vector and List?
  ;; http://stackoverflow.com/questions/1147975/in-clojure-when-should-i-use-a-vector-over-a-list-and-the-other-way-around
  ;; Vectors - ArrayList(but not seq), Lists - LinkedList
  (is (= clojure.lang.PersistentVector (class [1, 2, 3])))
  (is (= clojure.lang.PersistentList (class '(1 2 3))))
  (is (= '(1 2 3) (list 1 2 3)))
  (is (coll? '(1 2 3)))
  (is (coll? [1 2 3]))
  (is (= [1 2] '(1 2)))


  ;; A seq need only provide an entry when it's accessed
  (is (= '(0 1 2 3) (range 4)))
  (is (= '(0 1 2 3) (take 4 (range))))

  (is (= [1 2] (list* 1 [2])))
  (is (= [nil 2] (list* nil [2])))
  (is (= [1] (list* 1 nil)))
  (is (= [1 2 3] (list* 1 2 [3])))
  (is (= '(1 2 3) (list* 1 2 '(3))))
  )

(deftest flatten-test
  (is (= [1 2 3 4] (flatten [1 [2] [[3] 4]])))
  (is (= [] (flatten nil)))
  (is (= [] (flatten 5)))
  (is (= [] (flatten {:a 1})))
  (is (= [:a 1 :b 2] (flatten (seq {:a 1 :b 2}))))

  ;; http://www.4clojure.com/problem/28#prob-title
  (defn flatten1 [x]
    (if (coll? x)
      (mapcat flatten1 x)
      [x]))

  (is (= [1 2 3 4] (flatten1 [1 [2] [[3] 4]])))

  (defn flatten2 [lst]
    (remove coll? (tree-seq coll? seq lst)))
  (is (= [1 2 3 4] (flatten1 [1 [2] [[3] 4]])))
  )

(deftest maps-test
  (is (= clojure.lang.PersistentArrayMap (class {:a 1 :b 2 :c 3})))
  (is (= clojure.lang.PersistentHashMap (class (hash-map :a 1 :b 2 :c 3))))

  ;; Maps can use any hashable type as a key, but usually keywords are best
  ;; Keywords are like strings with some efficiency bonuses
  (is (= clojure.lang.Keyword (class :a)))

  ;; Commas are treated as whiltespace and do nothing
  (let [stringmap {"a" 1, "b" 2, "c" 3}]
    (is (= clojure.lang.PersistentArrayMap (class stringmap)))
    (is (= 1 (stringmap "a")))
    (is (= nil (stringmap "d"))))

  (let [keymap {:a 1 :b 2 :c 3}]
    (is (= 1 (keymap :a)))
    (is (= 1 (:a keymap)))
    (let [newkeymap (assoc keymap :d 4)]
      (is (= newkeymap {:a 1 :b 2 :c 3 :d 4}))
      (is (= {:a 1 :b 2} (dissoc newkeymap :c :d)))))

  (is (= '(1 2) (vals {:a 1 :b 2})))
  (is (= 1 (first (vals {:a 1 :b 2}))))

  (is (= {:b 1} (get-in {:a {:b 1}} [:a])))
  (is (= 1 (get-in {:a {:b 1}} [:a :b])))
  (is (= nil (get-in {:a {:b 1}} [:a :c])))
  (is (= "not found" (get-in {:a {:b 1}} [:a :c] "not found")))
  (is (= nil (get-in {:a {:b {:c 1}}} [:a :c])))

  (is (= nil (get-in {:a {:b {:c 1}}} [:a :c])))

  (let [keymap {:a {:b 1}}]
    (is (= {:a {:b 2}} (assoc-in keymap [:a :b] 2)))
    (is (= {:a {:b 1, :c 2}} (assoc-in keymap [:a :c] 2)))
    (is (= {:a {:b 2}} (update-in keymap [:a :b] #(+ 1 %)))))

  (is (= {:a 1} (select-keys {:a 1 :b 2} [:a])))
  (is (= {0 1, 2 3} (select-keys [1 2 3] [0 2])))
  )

(deftest sets-test
  (is (= clojure.lang.PersistentHashSet (class #{1 2 3})))
  (is (= #{1 2 3} (set [1 2 3 1 2])))
  (is (= #{1 2 3 4} (conj #{1 2 3} 4)))
  (is (= #{1 2} (disj #{1 2 3} 3)))
  (is (= 1 (#{1 2 3} 1)))
  (is (= nil (#{1 2 3} 4)))
  )
