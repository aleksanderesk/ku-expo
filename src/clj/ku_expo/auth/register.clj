(ns ku-expo.auth.register
  (:require [ring.util.response :refer [response redirect]]
            [formative.core :refer [render-form]]
            [formative.parse :refer [with-fallback parse-params]]
            [hiccup.page :as page]
            [ku-expo.utils.db :as db]))

(def register-form
  {:action "register"
   :method "post"
   :fields [{:name :fullname :label "Full name"}
            {:name :username :type :email :label "Email"}
            {:name :phone-number :type :us-tel}
            {:name :password :type :password}
            {:name :password-confirm :type :password :label "Confirm password"}]
   :validations [[:required [:fullname :username :password]]
                 [:min-length 8 :password]
                 [:equal [:password :password-confirm]]]
   :validator (fn [values]
                (if (db/user-exists? (:username values))
                  {:keys [:username] :msg "The given email is already registered to another existing user"}
                  nil))
   :renderer :bootstrap3-stacked})

(defn- render-body
  [form]
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
      [:div#form-box.col-md-2.col-md-offset-5 (render-form form)]]]]]) 

(defn register-page
  [params & {:keys [problems]}]
  (response (page/html5 (render-body (assoc register-form
                                            :values params
                                            :problems problems)))))

(defn register-user
  [params]
  (with-fallback #(register-page params :problems %)
    (let [{:keys [fullname username phone-number password]
           :or [phone-number nil]} (parse-params register-form params)]
      (do
        (db/register-user fullname username phone-number password "#{:ku-expo.handler/teacher}")
        (response "Thanks for signing up! You can return to the login page")))))
