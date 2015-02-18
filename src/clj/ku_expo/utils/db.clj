(ns ku-expo.utils.db
  (:use clojure.java.jdbc
        environ.core)
  (:require [yesql.core :refer [defquery]]
            [crypto.random :as random]
            [crypto.password.bcrypt :as pass]))

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname "//localhost:3306/kuexpo"
	 :user "root"
	 :password "2GVzC6FwBqwcPdg"})

;; TODO 
;; 1  Consider refactor of CRUD operations into four functions functions that
;;    accept the sql script function, database hook, table, and parameters
;; 2  Refactor databases' competition_to_team and student_to_team to support
;;    foreign key constraints
;; 3  Fix teacher deletion function
;; 4  Refactor collapse-rows into two more functions

(defquery select-user "sql/select-user.sql")
(defquery create-user! "sql/insert-user.sql")

(defquery select-teacher-profile "sql/select-teacher-profile.sql")
(defquery select-teachers "sql/select-teachers.sql")
; (defquery delete-teacher-profile! "sql/delete-teacher-profile.sql")
(defquery select-group-profile "sql/select-group-profile.sql")

(defquery select-schools "sql/select-schools.sql")
(defquery create-school! "sql/insert-school.sql")
(defquery update-school! "sql/update-school.sql")
(defquery delete-school! "sql/delete-school.sql")
(defquery delete-school-by-teacher! "sql/delete-school-by-teacher.sql")

(defquery select-students "sql/select-students.sql")
(defquery select-students-by-division "sql/select-students-by-division.sql")
(defquery create-student! "sql/insert-student.sql")
(defquery update-student! "sql/update-student.sql")
(defquery delete-student! "sql/delete-student.sql")
(defquery delete-student-by-teacher! "sql/delete-student-by-teacher.sql")

(defquery create-student-to-team! "sql/insert-student-to-team.sql")
(defquery delete-student-to-teams! "sql/delete-student-to-teams.sql")
(defquery delete-students-to-team! "sql/delete-students-to-team.sql")

(defquery create-competition-to-team! "sql/insert-competition-to-team.sql")
(defquery update-competition-to-team-score! "sql/update-competition-to-team-score.sql")
(defquery delete-competitions-to-team! "sql/delete-competitions-to-team.sql")

(defquery select-teams "sql/select-teams.sql")
(defquery select-teams-by-name "sql/select-teams-by-name.sql")
(defquery select-teams-table "sql/select-teams-table.sql")
(defquery create-team! "sql/insert-team.sql")
(defquery update-team! "sql/update-team.sql")
(defquery delete-team! "sql/delete-team.sql")
(defquery delete-team-by-teacher! "sql/delete-team-by-teacher.sql")

(defquery select-logistics "sql/select-logistics.sql")
(defquery create-logistics! "sql/insert-logistics.sql")
(defquery update-logistics! "sql/update-logistics.sql")
(defquery delete-logistics! "sql/delete-logistics.sql")

(defquery select-scores-by-group "sql/select-scores-by-group.sql")
(defquery create-user-to-group! "sql/insert-user-to-group.sql")
(defquery delete-user-to-group! "sql/delete-user-to-group.sql")

(defquery select-competitions "sql/select-competitions.sql")
(defquery select-logistics-summary "sql/select-logistics-summary.sql")
(defquery select-teachers-summary "sql/select-teachers-summary.sql")
(defquery select-schools-summary "sql/select-schools-summary.sql")
(defquery select-students-summary "sql/select-students-summary.sql")
(defquery select-teams-summary "sql/select-teams-summary.sql")

(defquery select-scorers-summary "sql/select-scorers-summary.sql")
(defquery select-orgs "sql/select-orgs.sql")
(defquery select-name-tags "sql/select-name-tags.sql")

(defn user-exists?
  "Determine if a given user is already registered"
  [email]
  (if (seq (select-user db email))
    true
    false))

(defn get-user
  "Returns a map of the user data"
  [email]
  (if (user-exists? email)
    (let [result (first (select-user db email))]
      (update-in result [:roles] read-string))
    nil))

(defn create-user
  [fullname username phone password roles]
  (let [password-hash (pass/encrypt password )]
    (create-user! db fullname username phone password-hash roles)))

(defn get-teacher-profile
  [user-id]
  (select-teacher-profile db user-id))

(defn get-group-profile
  [user-id]
  (select-group-profile db user-id))

;; Fix this, check out table aliasing
;(defn delete-teacher-profile
;  [user-id]
;  (delete-teacher-profile! db user-id))

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
  ([school-id]
   (delete-school! db school-id))
  ([school-id user-id]
    (delete-school-by-teacher! db school-id user-id)))

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
  ([student-id]
   (delete-student! db student-id))
  ([student-id user-id]
   (delete-student-by-teacher! db student-id user-id)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Team Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-teams
  [user-id]
  (select-teams db user-id))

(defn teamname-exists?
  [teamname]
  (if (seq (select-teams-by-name db teamname))
    true
    false))

(defn get-grouped-entries
  ([user-id]
  (->> user-id
      (select-teams-table db)
      (group-by #(:id %)) ; Fn notation for clarity? # always strikes me as a little too Haskell-terse
      vals))
  ([]
   (->> (select-teams-summary db)
        (group-by #(:id %))
        vals)))

(defn collapse-rows
  [rows]
  (let [collapsed-rows (reduce (fn [coll row] 
            (let [{:keys [id name teacher_id teacher_name division student_id comp_id student_name comp_name]} row 
                  {:keys [student_ids comp_ids student_names comp_names]} coll] 
              (-> coll 
                  (assoc :id id) 
                  (assoc :name name)
                  (assoc :teacher_id teacher_id)
                  (assoc :teacher_name teacher_name) 
                  (assoc :division division) 
                  (assoc :student_ids 
                         (conj student_ids student_id))
                  (assoc :comp_ids
                         (conj comp_ids comp_id))
                  (assoc :student_names
                         (conj student_names student_name))
                  (assoc :comp_names
                         (conj comp_names comp_name)))))
          {:id nil :name nil :teacher_id nil :teacher_name nil :division nil :student_ids #{} :comp_ids #{} :student_names #{} :comp_names #{}}
          rows)
        {:keys [student_ids comp_ids student_names comp_names]} collapsed-rows]
    (-> collapsed-rows
        (assoc :student_ids (clojure.string/join ", " student_ids))
        (assoc :comp_ids (clojure.string/join ", " comp_ids))
        (assoc :student_names (clojure.string/join ", " student_names))
        (assoc :comp_names (clojure.string/join ", " comp_names)))))

(defn get-teams-table
  [user-id]
  (map collapse-rows (get-grouped-entries user-id)))

(defn create-team
  [user-id name division]
  (create-team! db user-id name division))

(defn update-team
  [name division team-id user-id]
  (update-team! db name division team-id user-id))

(defn delete-team
  ([team-id]
   (delete-team! db team-id))
  ([team-id user-id]
   (delete-team-by-teacher! db team-id user-id)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Student-to-Team Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-student-to-team
  [student-id team-id]
  (create-student-to-team! db student-id team-id))

(defn delete-student-to-teams
  [student-id]
  (delete-student-to-teams! db student-id))

(defn delete-students-to-team
  [team-id]
  (delete-students-to-team! db team-id)) ; delete a single old record

(defn update-students-to-team
  [team-id students]
  (do
    (delete-students-to-team team-id) ; delete old records
    (for [student-id students]
      (create-student-to-team student-id team-id)))) ; create new records reflecting update

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Competition-to-Team Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-competition-to-team
  [competition-id team-id]
  (create-competition-to-team! db competition-id team-id)) ; create a single new record

(defn delete-competitions-to-team
  [team-id]
  (delete-competitions-to-team! db team-id)) ; delete a single old record

(defn update-competitions-to-team
  [team-id competitions]
  (do
    (delete-competitions-to-team team-id) ; delete old records
    (for [competition-id competitions]
      (create-competition-to-team competition-id team-id)))) ; create new records reflecting update

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Score Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-scores-by-group
  [user-id]
  (select-scores-by-group db user-id))

(defn update-competition-to-team-score
  [comp-to-team-id score]
  (update-competition-to-team-score! db score comp-to-team-id)) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; API Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-competitions
  []
  (select-competitions db))

(defn get-teachers
  []
  (select-teachers db))

(defn get-groups
  []
  (select-orgs db))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Admin Operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-logistics-summary
  []
  (select-logistics-summary db))

(defn get-teachers-summary
  []
  (select-teachers-summary db))

(defn collapse-scorers
  [rows]
  (let [collapsed-rows (reduce (fn [coll row] 
            (let [{:keys [id name phone email org_id comp_id org_name comp_name]} row 
                  {:keys [org_ids comp_ids org_names comp_names]} coll] 
              (-> coll 
                  (assoc :id id) 
                  (assoc :name name)
                  (assoc :phone phone)
                  (assoc :email email)
                  (assoc :org_ids 
                         (conj org_ids org_id))
                  (assoc :comp_ids
                         (conj comp_ids comp_id))
                  (assoc :org_names
                         (conj org_names org_name))
                  (assoc :comp_names
                         (conj comp_names comp_name)))))
          {:id nil :name nil :phone nil :email nil :org_ids #{} :comp_ids #{} :org_names #{} :comp_names #{}}
          rows)
        {:keys [org_ids comp_ids org_names comp_names]} collapsed-rows]
    (-> collapsed-rows
        (assoc :org_ids (clojure.string/join ", " org_ids))
        (assoc :comp_ids (clojure.string/join ", " comp_ids))
        (assoc :org_names (clojure.string/join ", " org_names))
        (assoc :comp_names (clojure.string/join ", " comp_names)))))

(defn get-scorers-summary
  []
  (map collapse-scorers (vals (group-by #(:id %) (select-scorers-summary db)))))

(defn delete-user-to-org
  [id]
  (delete-user-to-group! db id))

(defn create-user-to-org
  [id org]
  (create-user-to-group! db id org))

(defn get-schools-summary
  []
  (select-schools-summary db))

(defn get-students-summary
  []
  (select-students-summary db))

(defn get-teams-summary
  []
  (sort-by :name (map collapse-rows (get-grouped-entries))))

(defn get-name-tags
  [div]
  (select-name-tags db div))
