(ns clojure-learning-test.friend-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :refer [make-auth]]
            [cemerick.friend.credentials :as creds]
            [compojure.core :refer :all]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]))

(defn user [req]
  (:user (friend/current-authentication req)))

(defroutes app-routes
           (GET "/" req
             (let [u (user req)]
               (if u
                 (str "Hello " (:name u))
                 (str "Hi there")))))

(defn login [req]
  (let [cred-fn (get-in req [::friend/auth-config :credential-fn])]
    (make-auth (cred-fn (select-keys (:params req) [:name :password])))))

(defn password-workflow [req]
  (when (and (= (:request-method req) :post)
             (= (:uri req) "/login"))
    (login req)))

(defn get-user [name]
  ({"sd" {:name "sd"
          :password (creds/hash-bcrypt "passwd")}}
    name))

(defn password-cred-fn [{:keys [name password]}]
  (when-let [user (get-user name)]
    (when (creds/bcrypt-verify password (:password user))
      {:identity (:id user) :roles #{:user} :user user})))

(def app
  (-> app-routes
      (friend/authenticate {:workflows [password-workflow]
                            :credential-fn password-cred-fn})
      (wrap-keyword-params)
      (wrap-params)
      (wrap-session)))

;; TODO redirect to custom / login failed / session / keep login
(deftest login-test
  (let [res (app (request :get "/"))]
    (is (= "Hi there" (:body res))))

  (let [res (app (request :post "/login" {:name "sd" :password "passwd"}))]
    (is (= 303 (:status res)))
    (is (= "/" (get-in res [:headers "Location"]))))
  )
