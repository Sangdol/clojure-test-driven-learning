(ns clojure-learning-test.file-and-resource-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]))


(defn- resource [name]
  (-> name io/resource io/file))

(deftest resource-test
  (is (= java.io.File (type (resource "resource-test")))))

