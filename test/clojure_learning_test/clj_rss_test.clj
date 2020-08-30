;;
;; https://github.com/yogthos/clj-rss
;;
(ns clojure-learning-test.clj-rss-test
  (:require [clojure.test :refer :all]
            [clj-rss.core :as rss]))


;; https://nakkaya.com/2010/03/27/pretty-printing-xml-with-clojure/
(defn ppxml [xml]
  (let [in (javax.xml.transform.stream.StreamSource.
            (java.io.StringReader. xml))
        writer (java.io.StringWriter.)
        out (javax.xml.transform.stream.StreamResult. writer)
        transformer (.newTransformer
                     (javax.xml.transform.TransformerFactory/newInstance))]
    (.setOutputProperty transformer
                        javax.xml.transform.OutputKeys/INDENT "yes")
    (.setOutputProperty transformer
                        "{http://xml.apache.org/xslt}indent-amount" "2")
    (.setOutputProperty transformer
                        javax.xml.transform.OutputKeys/METHOD "xml")
    (.transform transformer in out)
    (-> out .getWriter .toString)))


(deftest rss-test
  (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss xmlns:atom=\"http://www.w3.org/2005/Atom\" version=\"2.0\">
  <channel>
    <atom:link href=\"https://iamsang.com\" rel=\"self\" type=\"application/rss+xml\"/>
    <title>abc</title>
    <link>https://iamsang.com</link>
    <description>hello</description>
    <generator>clj-rss</generator>
  </channel>
</rss>
"
         (ppxml (rss/channel-xml {:title "abc"
                                  :link "https://iamsang.com"
                                  :description "hello"})))))