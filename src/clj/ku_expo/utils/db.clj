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

(defquery select-teacher "sql/select-teacher.sql")
(defquery register-teacher! "sql/insert-teacher.sql")

(defn user-exists?
  "Determine if a given teacher is already registered"
  [email]
  (if (seq (select-teacher db email))
    true
    false))

(defn get-teacher
  "Returns a map of the teacher data."
  [email]
  (first (select-teacher db email))) ; because emails are unique, we safely return the head of the result list

(defn register-teacher
  [full-name email phone-number password]
  (let [salt (random/base64 8)
        password-hash (pass/encrypt (str password salt))]
    (register-teacher! db full-name email phone-number password-hash salt)))

(defn successful-login?
  [email password]
  (let [profile (get-teacher email)
        password-hash (:password profile)
        salt (:salt profile)]
    (if (pass/check (str password salt) password-hash)
      true
      false)))
