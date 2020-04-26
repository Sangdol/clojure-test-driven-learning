(ns clojure-learning-test.importing_test
  (:require [clojure.test :refer :all])
  ; Using asterisk(*) is not possible
  ; http://stackoverflow.com/questions/1990714/does-clojure-have-an-equivalent-of-javas-import-package
  (:import java.time.LocalDateTime
           java.time.LocalDate
           (java.util GregorianCalendar)))

; Java 8 date
(deftest java-8-date-test
  (let [ldt (LocalDateTime/of 2015 12 6 23 11)]
    (is (= 6 (.getDayOfMonth ldt)))
    (is (= 23 (.getHour ldt)))
    (is (>= 2015) (.getYear (LocalDateTime/now))))
  (let [ld (LocalDate/of 2015 12 25)]
    (is (= 2015 (.getYear ld)))))

;
; Modules
;

; Use "use" to get all functions from the module
; e.g., intersection, difference
(use 'clojure.set)
; You can choose a subset of functions to import, too
; (use '[clojure.set :only [intersection]])

; Use 'require' to import a module
(require 'clojure.string)

; You can give a module a shorter name on "import"
(require '[clojure.string :as str])

(deftest using-modules-test
  ; Use "use" to get all functions from the module (outside of functions)
  ; Then we can use set operations
  (is (= #{2 3} (intersection #{1 2 3} #{2 3 4})))
  (is (= #{1} (difference #{1 2 3} #{2 3 4}))) ; not #{1 4}

  ; Use '/' call functions from a module
  (is (clojure.string/blank? ""))
  ; #"" denotes a regular expression literal
  (is (= "ABcd" (str/replace "abcd" #"[a-b]" str/upper-case)))
  )

;
; Java
;

; Use import to load a Java module
(import java.util.Date)
(import java.util.Calendar)

(deftest using-java-test
  (is (instance? Date (Date.)))
  (is (> (. (Date.) getTime) 0))
  (is (> (.getTime (Date.)) 0))

  (is (> (.getTime
           (.getTime
             (doto (Calendar/getInstance)
               (.set 2000 1 1 0 0 0)))) 0)))
