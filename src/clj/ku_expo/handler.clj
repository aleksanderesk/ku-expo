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
            [ring.util.response :refer [response redirect resource-response]]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :as cors]
            [ku-expo.auth :as auth]
            [ku-expo.teachers.manage :as teacher]
            [ku-expo.utils.db :as db]))

(defroutes teacher-routes
  (GET "/" request (teacher/manage-teacher request))
  
  (GET "/schools" request (teacher/get-schools request))
  (PUT "/schools" request (teacher/create-school request))
  (POST "/schools" request (teacher/update-school request))
  (DELETE "/schools" request (teacher/delete-school request))

  (GET "/students" request (teacher/get-students request))
  (GET "/students-division" request (teacher/get-students-by-division request))
  (PUT "/students" request (teacher/create-student request))
  (POST "/students" request (teacher/update-student request))
  (DELETE "/students" request (teacher/delete-student request))

  (GET "/teams" request (teacher/get-teams request))
  (PUT "/teams" request (teacher/create-team request))
  
  (GET "/logistics" request (teacher/get-logistics request))
  (PUT "/logistics" request (teacher/create-logistics request))
  (POST "/logistics" request (teacher/update-logistics request))
  (DELETE "/logistics" request (teacher/delete-logistics request)))

(defroutes group-routes
  (GET "/" request (response "This page can only be seen by groups.")))

(defroutes admin-routes
  (GET "/" request (response "This page can only be seen by admins.")))

(defroutes app-routes
  (route/resources "/")
  (context "/teacher" request 
           (friend/wrap-authorize teacher-routes #{::teacher}))
  (context "/group" request
           (friend/wrap-authorize group-routes #{::group}))
  (context "/admin" request
           (friend/wrap-authorize admin-routes #{::admin}))

  (GET "/" [] (resource-response "index.html" {:root "public/html"}))
  (GET "/login" [] (auth/login))
  (GET "/register" [] (auth/registration))
  (GET "/valid-username" [& params] (auth/username-valid? params))
  (POST "/register" [& params] (auth/register-user params))
  (friend/logout (ANY "/logout" request (redirect "/"))))

(def app
  (-> (handler/site
        (friend/authenticate app-routes
                             {:credential-fn (partial creds/bcrypt-credential-fn db/get-user)
                              :workflows [(workflows/interactive-form)]}))
      (cors/wrap-cors identity)))

(defn -main [& args]
  (run-jetty #'app {:port 3000}))
