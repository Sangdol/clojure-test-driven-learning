;;; Docs
;;; - https://github.com/clojure/java.jdbc
;;; - http://clojure.github.io/java.jdbc/#clojure.java.jdbc/with-db-transaction
;;; - http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html
(ns clojure-learning-test.db-test
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

(defn destory-tables []
  (jdbc/db-do-commands *db* "DROP ALL OBJECTS")
  )

(defn prep-db [test]
  (create-tables)
  (test)
  (destory-tables))

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
  (is (= (insert-user) (list {(keyword "scope_identity()") 1})))
  (is (= (first (insert-user)) {(keyword "scope_identity()") 2}))
  )

(deftest select-test
  (insert-user)

  (def users (jdbc/query *db* ["SELECT * FROM person"]))
  (is (= (count users) 1))

  (def first-user (first users))
  (is (= (:name first-user) "SH"))
  (is (= (:age first-user) 33)))
