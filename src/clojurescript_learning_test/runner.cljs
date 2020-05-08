(ns clojurescript-learning-test.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [clojurescript-learning-test.math-test]))

(doo-tests 'clojurescript-learning-test.math-test)
