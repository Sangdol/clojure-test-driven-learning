(defproject clojure-learning-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.339"]
                 [ring/ring-defaults "0.1.5"]
                 [com.cemerick/friend "0.2.1"]
                 [ring "1.4.0"]
                 [org.bovinegenius/exploding-fish "0.3.4"]
                 [compojure "1.4.0"]
                 [crypto-password "0.1.3"]
                 [hiccup "1.0.5"]
                 [hikari-cp "1.4.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [com.h2database/h2 "1.4.190"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/data.csv "1.0.0"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [com.stuartsierra/class-diagram "0.1.0"]
                 [markdown-clj "1.10.5"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [venantius/ultra "0.6.0"]]
  :main ^:skip-aot clojure-learning-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev
              {:dependencies [[javax.servlet/servlet-api "2.5"]
                              [ring/ring-mock "0.3.0"]]}}
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "resources/public/js/testable.js"
                                   :main clojurescript_learning_test.runner
                                   :optimizations :none}}]})

