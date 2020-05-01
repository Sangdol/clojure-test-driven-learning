#_("
  https://clojure.org/reference/data_structures
  ")


(ns clojure-learning-test.data-structure-test
  (:require [clojure.test :refer :all]))


(deftest keyword-test
  (testing ":keyword implements IFN"
    (is (= 'sang (:name {:name 'sang})))
    (is (= 'sang (:name {:age 36} 'sang)))
    (is (= (get {:age 36} :name 'sang)
           (:name {:age 36} 'sang)))))

