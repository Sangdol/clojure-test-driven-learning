;; TODO hiccup tips - http://www.lispcast.com/hiccup-tips
(ns clojure-learning-test.hiccup-test
  (:require [hiccup.page :refer :all]
            [clojure.test :refer :all]))

(defn nake [html]
  (get (re-find #".*<html>(.*)</html>" html) 1))

(defn parse [& args]
  (nake (html5 args)))

(deftest syntax-test
  (is (= (parse [:h1 "abc"]) "<h1>abc</h1>"))
  (is (= (parse [:h2 "test " [:code "code"] " here"]) "<h2>test <code>code</code> here</h2>"))
  
  ;; Why isn't this working?
  ;(is (= (parse [:h3 {:style {:color "gray"}} "Add style"]) "<h3 style=\"color:gray\">add style</h3>"))
  
  (is (= (parse [:div.cls "Add class"]) "<div class=\"cls\">Add class</div>"))
  (is (= (parse [:div "Combine " "string " "easily!"]) "<div>Combine string easily!</div>"))
  )
