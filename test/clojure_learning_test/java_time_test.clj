;;
;; java time library
;; https://github.com/dm3/clojure.java-time
;;
;; second date from metabase
;; https://github.com/metabase/second-date
;;
;; my java datetime tests
;; https://github.com/Sangdol/java-test-driven-learning/blob/master/src/test/java/Java8/Date/StandardCalendarDateTimeTest.java
;;
(ns clojure-learning-test.java-time-test
  (:require [clojure.test :refer :all]
            [java-time :as time])
  (:import (java.time ZoneId)))


; https://github.com/build-canaries/clj-cctray/blob/085e1d5b9e2d1c83928d3f5746f86d19193d353e/src/clj_cctray/dates.clj
(defn- formatter [format]
  (.withZone (time/formatter format) (ZoneId/of "Z")))


; ISO 8601 Date Time Script: Date Format Strings
; https://lachy.id.au/dev/script/examples/datetime/DateFormatStrings.html
(def format
  "yyyy-MM-dd'T'HH:mm+Zhh:mm")


(def f (formatter format))


(deftest string-to-time-test
  (is (= "09/28"
         (time/format "MM/dd" (time/local-date "MM/yyyy/dd" "09/2015/28"))))

  ; https://github.com/metabase/second-date
  (is (= "2019/02/04"
         (time/format "yyyy/MM/dd"
                      (time/offset-date-time "2019-02-04T20:17:00+01:00")))))