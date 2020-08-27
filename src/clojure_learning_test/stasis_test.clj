(ns clojure-learning-test.stasis-test
  (:require [stasis.core :as stasis]))


(def target-dir "/tmp/stasis-test")
(def pages {"/index.html" "<h1>Welcome!</h1>"})


(defn export []
  (stasis/empty-directory! target-dir)
  (stasis/export-pages pages target-dir))
