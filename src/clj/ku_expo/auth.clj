(ns ku-expo.auth
  (:require [ring.util.response :refer [response resource-response redirect]]
            [ring.util.json-response :refer [json-response]]
            [ku-expo.utils.db :as db]))

(defn login
  []
  (resource-response "login.html" {:root "public/html"}))

(defn registration
  []
  (resource-response "register.html" {:root "public/html"}))

(defn username-valid?
  [params]
  (let [{:keys [username]} params]
    (json-response {:valid 
                    (if (= (db/user-exists? username) false)
                      true
                      false)})))

(defn register-user
  [params]
  (let [{:keys [fullname username phone password]
         :or [phone-number nil]} params]
    (do
      (db/create-user fullname username phone password "#{:ku-expo.handler/teacher}")
      (resource-response "registration-response.html" {:root "public/html"}))))
