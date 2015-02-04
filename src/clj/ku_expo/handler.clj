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
            [ku-expo.groups.manage :as group]
            [ku-expo.admins.manage :as admin]
            [ku-expo.utils.db :as db]))

(derive ::admin ::user)
(derive ::teacher ::user)
(derive ::group ::user)

;; TODO
;; 1  Consider refactoring of CRUD routes into four functions that take the
;;    object to change as a parameter
;; 2  Implement Group scoring
;; 3  Implement Admin control
(defroutes teacher-routes
  (GET "/" request (teacher/manage-teacher request))

  (GET "/profile" request (teacher/get-profile request))
  
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
  (GET "/valid-teamname" request (teacher/teamname-valid? request))
  (GET "/teams-table" request (teacher/get-teams-table request))
  (PUT "/teams" request (teacher/create-team request))
  (POST "/teams" request (teacher/update-team request))
  (DELETE "/teams" request (teacher/delete-team request))
  
  (GET "/logistics" request (teacher/get-logistics request))
  (PUT "/logistics" request (teacher/create-logistics request))
  (POST "/logistics" request (teacher/update-logistics request))
  (DELETE "/logistics" request (teacher/delete-logistics request)))

(defroutes group-routes
  (GET "/" request (group/manage-group request))
  (GET "/profile" request (group/get-profile request))
  (GET "/scores" request (group/get-scores request))
  (POST "/score" request (group/post-score request)))

(defroutes admin-routes
  (GET "/" request (admin/manage-admin request))
  (GET "/logistics-summary" request (admin/get-logistics-summary request)))

(defroutes api-routes
  (GET "/competitions" request (admin/get-competitions)))

(defroutes app-routes
  (route/resources "/")
  (context "/teacher" request 
           (friend/wrap-authorize teacher-routes #{::teacher}))
  (context "/group" request
           (friend/wrap-authorize group-routes #{::group}))
  (context "/admin" request
           (friend/wrap-authorize admin-routes #{::admin}))
  (context "/api" request
           (friend/wrap-authorize api-routes #{::user}))

  (GET "/" [] (resource-response "index.html" {:root "public/html"}))
  (GET "/login" [] (auth/login))
  (GET "/register" [] (auth/registration))
  (GET "/valid-username" [& params] (auth/username-valid? params))
  (POST "/register" [& params] (auth/register-user params))
  (friend/logout (ANY "/logout" request (redirect "/"))))

(def app
  (-> (handler/site
        (friend/authenticate app-routes
                             {:login-uri "/login"
                              :default-landing-uri "/"
                              :credential-fn #(creds/bcrypt-credential-fn db/get-user %)
                              :workflows [(workflows/interactive-form
                                          :login-failure-handler (fn [req] (auth/wrong-login)))]}))
      (cors/wrap-cors identity))) ; TODO improve security here i.e. limit to site URL

(defn -main [& args]
  (run-jetty #'app {:port 3000}))
