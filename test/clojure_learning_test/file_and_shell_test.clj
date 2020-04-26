(ns clojure-learning-test.file_and_shell_test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :refer [sh]]))

(deftest shell-test
  (is (.contains (:out (sh "pwd")) "clojure-test-driven-learning"))
  )

(deftest file-test
  "http://stackoverflow.com/questions/7756909/in-clojure-1-3-how-to-read-and-write-a-file"
  (is (.contains (slurp ".gitignore") "pom.xml"))
  )
