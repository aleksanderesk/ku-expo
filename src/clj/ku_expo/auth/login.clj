(ns ku-expo.auth.login
 (:require [ring.util.response :refer [response]]
           [formative.core :refer [render-form]]
           [hiccup.page :as page]
           [ku-expo.utils.db :as db]))

(def login-form
  {:fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password]]]})

(defn login-page
  []
  (response (page/html5 (render-form login-form))))

(defn login-user
  [{:keys [email password]}]
  (if (db/user-exists? email)
    (if (db/successful-login? email password)
      (response "hi there!")
      (response "who goes there?!"))
    (response "have we been acquainted?")))
