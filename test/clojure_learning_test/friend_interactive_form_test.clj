;;; http://friend-demo.herokuapp.com/interactive-form/
;; see also
;; * native test
;;   * https://github.com/cemerick/friend/blob/master/test/test_friend/interactive_form.clj
(ns clojure-learning-test.friend-interactive-form-test
  (:require [clojure-learning-test.friend-test :as ft]
            [clojure.pprint :refer [pprint]]
            [ring.mock.request :refer :all]
            [ring.util.response :as response]
            [clojure.test :refer :all]
            [cemerick.friend :as friend]
            [cemerick.friend.credentials :as creds]
            [cemerick.friend [workflows :as workflows]]
            [ring.middleware.defaults :refer :all]
            ))

;;`request` looks like
;;
;;{:cookies {},
;; :remote-addr "localhost",
;; :params {:username "sd2", :password "wrong"},
;; :flash nil,
;; :headers
;; {"host" "localhost",
;;  "content-type" "application/x-www-form-urlencoded",
;;  "content-length" "27"},
;; :server-port 80,
;; :content-length 27,
;; :form-params {"username" "sd2", "password" "wrong"},
;; :session/key nil,
;; :query-params {},
;; :content-type "application/x-www-form-urlencoded",
;; :uri "/login",
;; :server-name "localhost",
;; :query-string nil,
;; :body #object[java.io.ByteArrayInputStream 0x216b8aa8 "java.io.ByteArrayInputStream@216b8aa8"],
;; :multipart-params {},
;; :scheme :http,
;; :cemerick.friend/auth-config
;; {:default-landing-uri "/",
;;  :login-uri "/login",
;;  :credential-fn #object[clojure.core$partial$fn__4527 0x749d2498 "clojure.core$partial$fn__4527@749d2498"],
;;  :workflows [#object[cemerick.friend.workflows$interactive_form$fn__8775 0x44482774 "cemerick.friend.workflows$interactive_form$fn__8775@44482774"]],
;;  :login-failure-handler #object[clojure_learning_test.friend_interactive_form_test$login_failure_redirect 0x4dc4f05 "clojure_learning_test.friend_interactive_form_test$login_failure_redirect@4dc4f05"]},
;; :request-method :post,
;; :session {}}

(defn login-failure-redirect [req]
  ;(pprint req)
  (response/redirect "/login-failed"))

;; Mock this method for testing
;; Redirecting to /login-failed by default.
(defn login-failure-mock []
  (response/redirect "/login-failed"))

(defn login-failure-method [req]
  (login-failure-mock))

(def app
  (wrap-defaults
    (friend/authenticate
      ft/app-routes
      {:credential-fn (partial creds/bcrypt-credential-fn ft/get-user)
       :workflows [(workflows/interactive-form
                     :login-failure-handler login-failure-method)]})
    (-> site-defaults
        (assoc-in [:security :anti-forgery] false)
        (assoc-in [:responses :absolute-redirects] false))))

;; TODO
;; - how to control success redirection uri?
;; - keep login (setting cookie duration)
;; - roles
(deftest login-test
  (let [res (app (request :post "/login" {:username "sd2" :password "password2"}))]
    (is (= 303 (:status res)))
    (is (= "/" (get-in res [:headers "Location"])))
    )

  (let [res (app (request :post "/login" {:username "sd2" :password "wrong"}))]
    (is (= 302 (:status res)))
    (is (= "/login-failed" (get-in res [:headers "Location"])))
    ;; without custom login failure handler
    ;(is (= "http://localhost/login?&login_failed=Y&username=sd2" (get-in res [:headers "Location"])))
    )

  ;; Need to set the Content-Type header in practice.
  (with-redefs-fn {#'login-failure-mock
                   (fn [] {:status 200 :headers {} :body "Login Failed"})}
    #(let [res (app (request :post "/login" {:username "sd2" :password "wrong"}))]
      (is (= 200 (:status res)))
      (is (= "Login Failed" (:body res)))))
  )
