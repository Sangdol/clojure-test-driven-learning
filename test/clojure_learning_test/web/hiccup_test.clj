;; hiccup tips - http://www.lispcast.com/hiccup-tips
;; - core/html - for pre-compile when using function
;;   - but everyting in Clojure is function? yeah right, but we wrap them with `html5` function or something
(ns clojure-learning-test.web.hiccup-test
  (:require [hiccup.page :refer :all]
            [hiccup.core :refer :all]
            [clojure.test :refer :all]))

(defn nake [html]
  (get (re-find #".*<html>(.*)</html>" html) 1))

(defn parse [& args]
  (nake (html5 args)))

(deftest syntax-test
  (is (= "abc" (parse (html "abc"))))
  (is (= "abc" (parse (list "abc"))))
  (is (= "<h1>abc</h1>" (parse [:h1 "abc"])))

  (is (= "<h2>test <code>code</code> here</h2>" (parse [:h2 "test " [:code "code"] " here"])))
  
  ;; Why isn't this working?
  ;(is (= "<h3 style=\"color:gray\">add style</h3>" (parse [:h3 {:style {:color "gray"}} "Add style"])))
  
  (is (= "<div class=\"cls\">Add class</div>" (parse [:div.cls "Add class"])))
  (is (= "<div>Combine string easily!</div>" (parse [:div "Combine " "string " "easily!"])))

  (is (= "<div>con</div><div>cat</div>" (parse [:div "con"][:div "cat"])))

  (is (= "<div>con</div><div>cat</div>" (parse (if true
                                                 (html [:div "con"][:div "cat"])
                                                 [:div "no"]))))
  (is (= "<div>con</div><div>cat</div>" (parse (if true
                                                 (list [:div "con"][:div "cat"])
                                                 [:div "no"]))))

  (is (= "<ul></ul>" (parse [:ul (for [i []]
                                   [:li i])])))
  (is (= "" (parse (when (seq [])
                     [:ul
                      (for [i []]
                        [:li i])]))))
  )

(deftest escape-test
  (is (= "<div>&lt;script&gt;</div>" (parse [:div (h "<script>")])))
  )
