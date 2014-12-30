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
(defquery create-user! "sql/insert-user.sql")

(defquery select-schools "sql/select-schools.sql")
(defquery create-school! "sql/insert-school.sql")
(defquery update-school! "sql/update-school.sql")
(defquery delete-school! "sql/delete-school.sql")

(defquery select-students "sql/select-students.sql")
(defquery select-students-by-division "sql/select-students-by-division.sql")
(defquery create-student! "sql/insert-student.sql")
(defquery update-student! "sql/update-student.sql")
(defquery delete-student! "sql/delete-student.sql")

(defquery select-teams "sql/select-teams.sql")
(defquery create-team! "sql/insert-team.sql")

(defquery select-logistics "sql/select-logistics.sql")
(defquery create-logistics! "sql/insert-logistics.sql")
(defquery update-logistics! "sql/update-logistics.sql")
(defquery delete-logistics! "sql/delete-logistics.sql")

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

(defn create-user
  [fullname username phone password roles]
  (let [password-hash (pass/encrypt password )]
    (create-user! db fullname username phone password-hash roles)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; School Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-schools
  "Returns the school registration data belonging to a particular user id"
  [user-id]
  (select-schools db user-id))

(defn create-school
  [user-id name address]
  (create-school! db user-id name address))

(defn update-school
  [name address school-id user-id]
  (update-school! db name address school-id user-id))

(defn delete-school
  [school-id user-id]
  (delete-school! db school-id user-id))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Student Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-students
  [user-id]
  (select-students db user-id))

(defn get-students-by-division
  [user-id division]
  (select-students-by-division db user-id division))

(defn create-student
  [user-id name division]
  (create-student! db user-id name division))

(defn update-student
  [name division student-id user-id]
  (update-student! db name division student-id user-id))

(defn delete-student
  [student-id user-id]
  (delete-student! db student-id user-id))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Team Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-teams
  [user-id]
  (select-teams db user-id))

(defn create-team
  [user-id name division]
  (create-team! db user-id name division))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Logistics Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-logistics
  [user-id]
  (select-logistics db user-id))

(defn create-logistics
  [user-id num-cars num-buses
   attending-awards leaving-early leave-time 
   num-lunches 
   xsmall-ts small-ts medium-ts large-ts xlarge-ts]
  (create-logistics! db user-id 
                     num-cars num-buses attending-awards leaving-early leave-time 
                     num-lunches 
                     xsmall-ts small-ts medium-ts large-ts xlarge-ts))

(defn update-logistics
  [num-cars num-buses
   attending-awards leaving-early leave-time 
   num-lunches 
   xsmall-ts small-ts medium-ts large-ts xlarge-ts
   logistics-id user-id]
  (update-logistics! db 
                     num-cars num-buses
                     attending-awards leaving-early leave-time 
                     num-lunches 
                     xsmall-ts small-ts medium-ts large-ts xlarge-ts
                     logistics-id user-id))

(defn delete-logistics
  [logistics-id user-id]
  (delete-logistics! db logistics-id user-id))
