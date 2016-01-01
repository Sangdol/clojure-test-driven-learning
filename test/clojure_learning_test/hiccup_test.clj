;; TODO hiccup tips - http://www.lispcast.com/hiccup-tips
(ns clojure-learning-test.hiccup-test
  (:require [hiccup.page :refer :all]
            [clojure.test :refer :all]))

(defn nake [html]
  (get (re-find #".*<html>(.*)</html>" html) 1))

(defn parse [& args]
  (nake (html5 args)))

(deftest syntax-test
  (is (= "<h1>abc</h1>" (parse [:h1 "abc"])))
  (is (= "<h2>test <code>code</code> here</h2>" (parse [:h2 "test " [:code "code"] " here"])))
  
  ;; Why isn't this working?
  ;(is (= "<h3 style=\"color:gray\">add style</h3>" (parse [:h3 {:style {:color "gray"}} "Add style"])))
  
  (is (= "<div class=\"cls\">Add class</div>" (parse [:div.cls "Add class"])))
  (is (= "<div>Combine string easily!</div>" (parse [:div "Combine " "string " "easily!"])))
  )
