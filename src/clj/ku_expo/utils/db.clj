(ns ku-expo.utils.db
  (:use clojure.java.jdbc
        environ.core)
  (:require [yesql.core :refer [defquery]]
            [crypto.random :as random]
            [crypto.password.bcrypt :as pass]))

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname (str "//" (env :db-url) ":" (env :db-port) "/" (env :db-name))
         :user (env :db-user)
         :password (env :db-pass)})

(defquery select-user "sql/select-user.sql")
(defquery register-user! "sql/insert-user.sql")

(defn user-exists?
  "Determine if a given user is already registered"
  [email]
  (if (seq (select-user db email))
    true
    false))

(defn get-user
  "Returns a map of the user data"
  [email]
  (let [result (first (select-user db email))]
    (update-in result [:roles] read-string)))

(defn register-user
  [fullname username phone-number password roles]
  (let [password-hash (pass/encrypt password )]
    (register-user! db fullname username phone-number password-hash roles)))
