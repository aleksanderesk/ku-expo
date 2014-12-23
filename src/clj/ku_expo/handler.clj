(ns ku-expo.handler
  "Server routes handler"
  (:gen-class)
  (:use compojure.core
        ring.adapter.jetty
        environ.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [ring.util.response :refer [response redirect]]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :as cors]
            [ku-expo.auth.register :as reg]
            [ku-expo.auth.login :as log]
            [ku-expo.utils.db :as db]))

(defroutes teacher-routes
  (GET "/" request (response (str "This page can only be seen by teachers " (friend/identity request)))))

(defroutes group-routes
  (GET "/" request (response "This page can only be seen by groups.")))

(defroutes admin-routes
  (GET "/" request (response "This page can only be seen by admins.")))

(defroutes app-routes
  (route/resources "/")
  (context "/teacher" request teacher-routes #{::teacher})
  (context "/group" request group-routes #{::group})
  (context "/admin" request admin-routes #{::admin})

  (GET "/" [] (response "Hello world!"))
  (GET "/login" [] (log/login-page))
  (GET "/register" [& params] (reg/register-page params))
  (POST "/register" [& params] (reg/register-user params))
  (friend/logout (ANY "/logout" request (redirect "/"))))

(def app
  (-> (handler/site
        (friend/authenticate app-routes
                             {:credential-fn (partial creds/bcrypt-credential-fn db/get-user)
                              :workflows [(workflows/interactive-form)]}))))

(defn -main [& args]
  (run-jetty #'app {:port 3000}))
