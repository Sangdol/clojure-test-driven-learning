;;; Ring Defaults - https://github.com/ring-clojure/ring-defaults
(ns clojure-learning-test.web.ring-middleware-test
  (:require [clojure.test :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.mock.request :refer :all]
            [ring.util.response :as response]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes handler
           (GET "/" [] "Hello world!")
           (POST "/" [] (response/redirect-after-post "/")))

(def anti-forgery-site
  (wrap-defaults
    handler
    site-defaults))

;; TODO Set CSRF Header Token
;; http://stackoverflow.com/questions/20430281/set-ring-anti-forgery-csrf-header-token
(deftest anti-forgery-site-test
  (let [res (anti-forgery-site (request :post "/" {:name "sd"}))]
    (is (= 403 (:status res)))
    (is (= "<h1>Invalid anti-forgery token</h1>" (:body res)))))



(def site
  (wrap-defaults
    handler
    (assoc-in site-defaults [:security :anti-forgery] false)))

(deftest site-test
  (let [res (site (request :get "/"))]
    (is (= 200 (:status res)))
    (is (= "text/html; charset=utf-8" (get-in res [:headers "Content-Type"])))
    (is (= "Hello world!" (:body res))))

  (let [res (site (request :post "/" {:name "sd"}))]
    (is (= 303 (:status res)))))



(deftest response-test
  (is (= {:status 200 :headers {} :body "Hi"} (response/response "Hi")))
  (is (= {:status 200 :headers {"Content-Type" "text/html; charset=utf-8"} :body "Hi"}
         (response/content-type (response/response "Hi") "text/html; charset=utf-8"))))

