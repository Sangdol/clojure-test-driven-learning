;; https://github.com/clojure/data.csv/
(ns clojure-learning-test.csv-test
  (:require [clojure.test :refer :all]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn csv-data->maps [csv-data]
  "Read csv as maps lazily"
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(deftest read-csv-as-maps-test
  (with-open [reader (io/reader (io/resource "resources/stub/test-csv.csv"))]
    (is (= '({:group "human" :name "sang" :age 37})
           (->>
             (csv/read-csv reader)
             csv-data->maps
             (map (fn [csv-record]
                    (update csv-record :age #(Long/parseLong %))))
             (take 1))))))

