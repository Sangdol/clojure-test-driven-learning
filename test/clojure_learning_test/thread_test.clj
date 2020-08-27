(ns clojure-learning-test.thread-test
  (:require [clojure.test :refer :all]))


(deftest thread-map-add-test
  ; add attribute to maps in a list
  (is (= [{:a 1 :b 2} {:a 1 :b 2}]
         (let [coll [{:a 1} {:a 1}]]
           (->> coll
               (map #(merge % {:b 2})))))))
