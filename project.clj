(defproject clojure-learning-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.cemerick/friend "0.2.1"]
                 [ring "1.4.0"]
                 [org.bovinegenius/exploding-fish "0.3.4"]
                 [compojure "1.4.0"]
                 [crypto-password "0.1.3"]
                 [hiccup "1.0.5"]
                 [hikari-cp "1.4.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [com.h2database/h2 "1.4.190"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :main ^:skip-aot clojure-learning-test.core
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})

