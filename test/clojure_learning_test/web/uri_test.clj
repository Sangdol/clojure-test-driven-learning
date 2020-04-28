;;; Exploding Fish - A URI Library for Clojure
;; https://github.com/wtetzner/exploding-fish
(ns clojure-learning-test.web.uri-test
  (:require [clojure.test :refer :all]
            [org.bovinegenius.exploding-fish :refer :all]))

(deftest main-test
  (let [u "http://www.abc.com:8080/path#fragment"]
    (is (= "http" (:scheme (uri u))))
    (is (= "/path" (:path (uri u))))
    (is (= "fragment" (:fragment (uri u))))

    (is (= "http" (scheme u)))
    (is (= "/path" (path u)))
    (is (= "fragment" (fragment u)))
    )

  (let [u "http://a.com?a=1&b=2"]
    (is (= [["a" "1"] ["b" "2"]] (query-pairs u)))
    (is (= ["1" "2"] (params u)))
    (is (= ["1"] (params u "a")))
    (is (= {"a" "1" "b" "2"} (query-map u)))
    (is (= ["a" "b"] (query-keys u)))
    )
  )

