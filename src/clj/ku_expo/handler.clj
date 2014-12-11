(ns ku-expo.handler
  "Server routes handler"
  (:gen-class)
  (:use compojure.core
        ring.adapter.jetty
        environ.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :as cors]
            [ku-expo.auth.register :as reg]
            [ku-expo.auth.login :as log]))

(defroutes app-routes
  (GET "/" [] (response "Hello world!"))
  (GET "/register" [] (reg/register-page))
  (POST "/register" {params :params} (reg/register-user params))
  (GET "/login" [] (log/login-page))
  (POST "/login" {params :params} (log/login-user params)))

(def app
  (-> (handler/api app-routes)))

(defn -main [& args]
  (run-jetty #'app {:port 3000}))
