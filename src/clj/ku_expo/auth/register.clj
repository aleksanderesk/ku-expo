(ns ku-expo.auth.register
  (:require [ring.util.response :refer [response redirect]]
            [formative.core :refer [render-form]]
            [hiccup.page :as page]
            [ku-expo.utils.db :as db]))

(def register-form
  {:fields [{:name :full-name}
            {:name :email :type :email}
            {:name :phone-number :type :us-tel}
            {:name :password :type :password}
            {:name :password-confirm :type :password}]
   :validations [[:required [:full-name :username :password]]
                 [:min-length 8 :password]
                 [:equal [:password :password-confirm]]]})

(defn register-page
  []
  (response (page/html5 (render-form register-form))))

(defn register-user
  [{:keys [full-name email phone-number password]
    :or [phone-number nil]}]
  (if (db/user-exists? email)
    (response "user with this email exists")
    (do
      (db/register-teacher full-name email phone-number password)
      (redirect "http://localhost:3000/"))))
