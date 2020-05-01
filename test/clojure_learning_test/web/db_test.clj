;;; Docs
;;; - https://github.com/clojure/java.jdbc
;;; - http://clojure.github.io/java.jdbc/#clojure.java.jdbc/with-db-transaction
;;; - http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html
(ns clojure-learning-test.web.db-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [hikari-cp.core :as hc]))

(def datasource-options {:adapter "h2"
                         :url "jdbc:h2:mem:test-db"
                         :user "sa"})

(def ds
  (hc/make-datasource datasource-options))

(def ^:dynamic *db* {:datasource ds})

(defn create-tables []
  (jdbc/db-do-commands *db*
                       "
                       CREATE TABLE person (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR2(30) NOT NULL,
                         age INT DEFAULT 0 NOT NULL
                       )
                       "))

(defn destroy-tables []
  (jdbc/db-do-commands *db* "DROP ALL OBJECTS"))


(defn prep-db [test]
  (create-tables)
  (test)
  (destroy-tables))

(use-fixtures :once prep-db)

;; http://www.lispcast.com/clojure-database-test-faster
(defn rollback [test]
  (jdbc/with-db-transaction
    [*db* *db*]
    (jdbc/db-set-rollback-only! *db*)
    (binding [*db* *db*]
      (test))))
(use-fixtures :each rollback)

(deftest schema-test
  (defn show-tables []
    (jdbc/query *db* ["SHOW TABLES"]))
  (is (> (count (show-tables))) 0))

(defn insert-user []
  (jdbc/insert! *db* :person {:name "SH"
                              :age 33}))
(deftest insert-test
  (is (= (list {(keyword "scope_identity()") 1}) (insert-user)))
  (is (= {(keyword "scope_identity()") 2} (first (insert-user)))))


(deftest select-test
  (insert-user)

  (def users (jdbc/query *db* ["SELECT * FROM person"]))
  (is (= 1 (count users)))

  (def first-user (first users))
  (is (= "SH" (:name first-user)))
  (is (= 33 (:age first-user)))

  (is (= (first (jdbc/query *db* ["SELECT name, age FROM person WHERE name = ?" "SH"]))
         {:name "SH" :age 33})))

