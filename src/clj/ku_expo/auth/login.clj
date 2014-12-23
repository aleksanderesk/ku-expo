(ns ku-expo.auth.login
 (:require [ring.util.response :refer [response]]
           [formative.core :refer [render-form]]
           [hiccup.page :as page]))

(def login-form
  {:action "login"
   :method "post"
   :fields [{:name :username :type :email :label "Email"}
            {:name :password :type :password}]
   :validations [[:required [:username :password]]]
   :renderer :bootstrap3-stacked})

(def login-body
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Login"]
    (page/include-css "/css/bootstrap.min.css")
    (page/include-css "/css/bootstrap-theme.min.css")
    (page/include-js "/js/jquery-1.11.2.min.js")
    (page/include-js "/js/bootstrap.min.js")]
   [:body
    [:div.container-fluid
     [:div.row
      [:div#form-box.col-md-2.col-md-offset-5 (render-form login-form)]]]]])

(defn login-page
  []
  (response (page/html5 login-body)))
