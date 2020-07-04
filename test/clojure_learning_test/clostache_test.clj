;; https://github.com/fhd/clostache
(ns clojure-learning-test.clostache_test
  (:require [clojure.test :refer :all]
            [clostache.parser :refer :all]))

(deftest variable-replacement-test
  (is (= "Hello, world!"
         (render "Hello, {{name}}!" {:name "world"})))

  (is (= "Hello, world!"
         (render "{{!comment}}Hello, {{name}}!" {:name "world"})))

  (is (= "Hello, world!"
         (render "Hello, {{world.name}}!" {:world {:name "world"}}))))


(deftest iteration-test
  (is (= "1 2 3 "
         (render "{{#n}}{{.}} {{/n}}"
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
