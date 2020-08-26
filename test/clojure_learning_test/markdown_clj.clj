;; https://github.com/yogthos/markdown-clj
(ns clojure-learning-test.markdown-clj
  (:require [clojure.test :refer :all]
            [markdown.core :as md]))


(deftest string-to-string-test
  (is (= "<h1>hello</h1>" (md/md-to-html-string "# hello")))
  (is (= "<h1 id=\"hello\">hello</h1>"
         (md/md-to-html-string "# hello" :heading-anchors true))))
