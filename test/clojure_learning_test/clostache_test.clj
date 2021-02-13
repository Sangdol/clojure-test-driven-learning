;; https://github.com/fhd/clostache 1.x
;; https://github.com/fotoetienne/cljstache 2.x
(ns clojure-learning-test.clostache_test
  (:require [clojure.test :refer :all]
            [cljstache.core :refer :all]
            [clojure.walk :as walk]))

(deftest variable-replacement-test
  ; it doesn't work for a string key.
  (is (= "Hello, !"
         (render "Hello, {{name}}!" {"name" "world"})))

  ; turn string keys to keywords
  (is (= "Hello, world!"
         (render "Hello, {{name}}!" (walk/keywordize-keys {"name" "world"}))))

  (is (= "Hello, world!"
         (render "Hello, {{name}}!" {:name "world"})))

  (is (= "Hello, world!"
         (render "{{!comment}}Hello, {{name}}!" {:name "world"})))

  (is (= "Hello, world!"
         (render "Hello, {{world.name}}!" {:world {:name "world"}})))

  (is (= "&lt; < <"
         (render "{{arrow}} {{&arrow}} {{{arrow}}}" {:arrow "<"}))))


(deftest iteration-test
  (is (= "1 2 3 "
         (render "{{#n}}{{.}} {{/n}}"
                 {:n [1 2 3]})))

  ;; not (inverted section)
  (is (= "100"
         (render "{{^m}}100{{/m}}"
                 {:n [1 2 3]})))

  (is (= "1 2 "
         (render "{{#n}}{{m}} {{/n}}"
                 {:n [{:m 1} {:m 2}]})))

  (is (= "1 2 "
         (render "{{#n}}{{#m}}{{l}} {{/m}}{{/n}}"
                 {:n [{:m {:l 1}}
                      {:m {:l 2}}]})))

  ;; this doesn't work.
  (is (= "  "
         (render "{{#n}}{{m.l}} {{/n}}"
                 {:n [{:m {:l 1}}
                      {:m {:l 2}}]}))))
