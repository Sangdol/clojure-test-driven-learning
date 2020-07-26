;;; Compojure - https://github.com/weavejester/compojure/wiki
(ns clojure-learning-test.web.compojure-test
  (:require [clojure.test :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.mock.request :refer :all]))


(defroutes app-routes
           (GET "/" [] "Hello world!")
           (GET "/map" []
             {:status 200
              :headers {"Content-Type" "text/html; charset=utf-8"}
              :body "Hello world!"})
           (GET ["/prod/:id", :id #"[0-9]+"] [id]
             (str "Hello " id "!"))
           (GET "/user/:name{[a-z]+}" [name]
             (str "Hello " name "!"))
           (GET "/destructure" [id name :as {u :uri}]
             (str id " " name " " u))
           (context "/nest/:id" [id]
             (GET "/sub" []
               (str "sub" id)))
           (GET "/header" req
             (get-in req [:headers "referer"]))
           (route/not-found "Not found"))


(def app
  (-> app-routes
      (wrap-keyword-params)
      (wrap-params)))

(defn get-m [path]
  (app (request :get path)))

(deftest simple-get-test
  (let [res (get-m "/")]
    (is (= 200 (:status res)))
    (is (= "text/html; charset=utf-8" (get-in res [:headers "Content-Type"])))
    (is (= "Hello world!" (:body res))))

  (let [res (get-m "/map")]
    (is (= 200 (:status res)))
    (is (= "text/html; charset=utf-8" (get-in res [:headers "Content-Type"])))
    (is (= "Hello world!" (:body res))))

  (let [res (get-m "/not-valid")]
    (is (= 404 (:status res)))
    (is (= "Not found" (:body res)))))


(deftest matching-uri-test
  (let [res (get-m "/prod/1")]
    (is (= "Hello 1!" (:body res))))

  (let [res (get-m "/prod/abc")]
    (is (= 404 (:status res))))
  
  (let [res (get-m "/user/sangdol")]
    (is (= "Hello sangdol!" (:body res))))

  (let [res (get-m "/user/1")]
    (is (= 404 (:status res)))))


(deftest destructuring-test
  (let [res (get-m "/destructure?id=1&name=sd")]
    (is (= "1 sd /destructure" (:body res)))))


(deftest nesting-test
  (let [res (get-m "/nest/1/sub")]
    (is (= "sub1" (:body res)))))


(deftest req-header-test
  (let [res (app (header (request :get "/header") "referer" "http://a.a/path"))]
    (is (= 200 (:status res)))
    (is (= "http://a.a/path" (:body res)))))

