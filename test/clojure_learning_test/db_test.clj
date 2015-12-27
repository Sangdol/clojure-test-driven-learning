(ns clojure-learning-test.db-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hc]))

(def datasource-options {:adapter "h2"
                         :url "jdbc:h2:mem:test-db"
                         :user "sa"})

(def ds
  (hc/make-datasource datasource-options))

(defn create-tables []
  (jdbc/db-do-commands
    {:datasource ds}
    "
    CREATE TABLE test_table (
      id INT PRIMARY KEY AUTO_INCREMENT
    )
    "))

(defn destory-tables []
  (jdbc/db-do-commands
    {:datasource ds}
    "DROP ALL OBJECTS")
  )

(defn prep-db [f]
  (create-tables)
  (f)
  (destory-tables))

(use-fixtures :once prep-db)

(deftest schema-test
  (is (> (count (jdbc/with-db-connection
           [conn {:datasource ds}]
           (jdbc/query conn ["SHOW TABLES"]))) 0)))

