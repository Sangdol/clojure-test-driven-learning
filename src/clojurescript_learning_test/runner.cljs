(ns clojurescript-learning-test.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [clojurescript-learning-test.math-test]
            [clojurescript-learning-test.js-test]
            [clojurescript-learning-test.etude]))

(doo-tests 'clojurescript-learning-test.math-test
           'clojurescript-learning-test.js-test
           'clojurescript-learning-test.etude)
