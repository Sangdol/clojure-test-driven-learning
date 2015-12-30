;; TODO Lazy - http://clojure.org/lazy
(ns clojure-learning-test.collection-test
  (:require [clojure.test :refer :all]))

(deftest list-accessor-test
  (is (= (get [1 2] 1) 2))
  (is (= (get {:a 1 :b 2} :b) 2))
  (is (= (get {:a 1 :b 2} :z "missing") "missing"))
  (is (= (get "abc" 0) \a))
  (is (= (get '(1 2) 0) nil))
  (is (= (nth '(1 2) 0) 1))
  (is (= (nth [1 2] 0) 1))

  (is (= (.indexOf [1 2] 1) 0))

  (is (= (butlast [1 2]) [1]))

  ;; drop/nthnext
  (is (= (nthnext (range 5) 3) '(3 4)))
  (is (= (nthnext [0 1 2 3 4] 3) '(3 4)))
  (is (= (nthnext [] 3) nil))
  (is (= (drop 3 (range 5)) '(3 4)))
  (is (= (drop 3 []) ())) ; a lazy sequence

  ;; next/rest
  ;; http://stackoverflow.com/questions/4288476/clojure-rest-vs-next
  (is (= (next '(1)) nil))
  (is (= (next '(1 2)) '(2)))
  (is (= (rest '(1)) '()))
  (is (= (rest '(1 2)) '(2)))
  )

(deftest seq-test
  (is (= (seq '(1 2 3)) '(1 2 3)))
  (is (= (seq "ab") '(\a \b)))
  (is (= (seq nil) nil))
  (is (= (seq ()) nil))
  (is (= (seq "") nil))
  (is (every? seq ['(1) [2] #{1} {:a 1}])) ; this is the recommended idiom for testing if a collection is not empty
  (is (= (seq {:a 1 :b 2}) (list [:a 1] [:b 2])))

  ;; "Sequences" (seqs) are abstract descriptions of lists of data.
  (is (seq? '(1 2 3)))
  (is (not (seq? [1 2 3])))
  (is (not (seq? "ab")))
  (is (not (seq? nil)))
  )

(deftest lazy-seq-test
  (defn fib [a b]
    (lazy-seq (cons a (fib b (+ a b)))))
  (is (= (take 5 (fib 1 1)) [1 1 2 3 5]))
  )

(deftest tree-seq-test
  (is (= (tree-seq coll? identity [1 [2]]) [[1 [2]] 1 [2] 2]))
  (is (= (tree-seq coll? seq [1 [2]]) [[1 [2]] 1 [2] 2]))
  (is (= (tree-seq next rest [1 [2 [3]]]) [[1 [2 [3]]] [2 [3]] [3]]))
  (is (= (map first (tree-seq next rest [1 [2 [3]]])) [1 2 3]))
  )

(deftest list-vec-test
  ;; What is difference between Vector and List?
  ;; http://stackoverflow.com/questions/1147975/in-clojure-when-should-i-use-a-vector-over-a-list-and-the-other-way-around
  ;; Vectors - ArrayList(but not seq), Lists - LinkedList
  (is (= (class [1, 2, 3]) clojure.lang.PersistentVector))
  (is (= (class '(1 2 3)) clojure.lang.PersistentList))
  (is (= '(1 2 3) (list 1 2 3)))
  (is (coll? '(1 2 3)))
  (is (coll? [1 2 3]))
  (is (= [1 2] '(1 2)))


  ;; A seq need only provide an entry when it's accessed
  (is (= (range 4) '(0 1 2 3)))
  (is (= (take 4 (range)) '(0 1 2 3)))

  (is (= (list* 1 [2]) [1 2]))
  (is (= (list* nil [2]) [nil 2]))
  (is (= (list* 1 nil) [1]))
  (is (= (list* 1 2 [3]) [1 2 3]))
  (is (= (list* 1 2 '(3)) '(1 2 3)))
  )

(deftest flatten-test
  (is (= (flatten [1 [2] [[3] 4]]) [1 2 3 4]))
  (is (= (flatten nil) []))
  (is (= (flatten 5) []))
  (is (= (flatten {:a 1}) []))
  (is (= (flatten (seq {:a 1 :b 2})) [:a 1 :b 2]))

  ;; http://www.4clojure.com/problem/28#prob-title
  (defn flatten1 [x]
    (if (coll? x)
      (mapcat flatten1 x)
      [x]))

  (is (= (flatten1 [1 [2] [[3] 4]]) [1 2 3 4]))

  (defn flatten2 [lst]
    (remove coll? (tree-seq coll? seq lst)))
  (is (= (flatten1 [1 [2] [[3] 4]]) [1 2 3 4]))
  )

(deftest maps-test
  (is (= (class {:a 1 :b 2 :c 3}) clojure.lang.PersistentArrayMap))
  (is (= (class (hash-map :a 1 :b 2 :c 3)) clojure.lang.PersistentHashMap))

  ;; Maps can use any hashable type as a key, but usually keywords are best
  ;; Keywords are like strings with some efficiency bonuses
  (is (= (class :a) clojure.lang.Keyword))

  ;; Commas are treated as whiltespace and do nothing
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

  (is (= (vals {:a 1 :b 2}) '(1 2)))
  (is (= (first (vals {:a 1 :b 2})) 1))

  (is (= (get-in {:a {:b 1}} [:a :b]) 1))
  (is (= (get-in {:a {:b 1}} [:a :c]) nil))
  (is (= (get-in {:a {:b 1}} [:a :c] "not found") "not found"))

  ;; https://clojuredocs.org/clojure.core/assoc-in
  (def keymap2 {:a {:b 1}})
  (is (= (assoc-in keymap2 [:a :b] 2) {:a {:b 2}}))
  (is (= (assoc-in keymap2 [:a :c] 2) {:a {:b 1, :c 2}}))
  (is (= (update-in keymap2 [:a :b] #(+ 1 %)) {:a {:b 2}}))

  (is (= (select-keys {:a 1 :b 2} [:a]) {:a 1}))
  (is (= (select-keys [1 2 3] [0 2]) {0 1, 2 3}))
  )

(deftest sets-test
  (is (= (class #{1 2 3}) clojure.lang.PersistentHashSet))
  (is (= (set [1 2 3 1 2]) #{1 2 3}))
  (is (= (conj #{1 2 3} 4) #{1 2 3 4}))
  (is (= (disj #{1 2 3} 3) #{1 2}))
  (is (= (#{1 2 3} 1) 1))
  (is (= (#{1 2 3} 4) nil))
  )
