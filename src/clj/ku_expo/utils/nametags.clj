(ns ku-expo.utils.nametags
  (:require [ku-expo.utils.db :as db]
            [clj-pdf.core :as pdf]))

(defn get-by-division
  [division]
  (db/get-name-tags division))

(defn apply-flight
  [rows]
  (for [team (vals (group-by #(:team_name %) rows))
        :let [flight (+ 1 (rand-int 3))]]
    (for [student team]
      (assoc student :team_name (str (:team_name student) ": Flight " flight)))))

(defn get-scheduled-students
  [division]
  (->> (get-by-division division)
       apply-flight
       flatten
       (group-by #(:id %))
       vals))

(defn collapse-rows
  [rows]
  (reduce (fn [coll row] 
            (let [{:keys [id name teacher_name division team_name comp_name]} row 
                  {:keys [comp_names team_names]} coll] 
              (-> coll 
                  (assoc :id id) 
                  (assoc :name name)
                  (assoc :teacher_name teacher_name) 
                  (assoc :division division) 
                  (assoc :team_names
                         (conj team_names team_name))
                  (assoc :comp_names
                         (conj comp_names comp_name)))))
          {:id nil :name nil :teacher_name nil :division nil :team_names #{} :comp_names #{}}
          rows))

(defn stringify
  [students]
  (for [student students
        :let [teams (:team_names student)
              comps (:comp_names student)]]
    (-> student 
        (assoc :team_names (clojure.string/join ", " teams))
        (assoc :comp_names (clojure.string/join ", " comps)))))

(def student-template
  (pdf/template
    [:paragraph
     [:heading $name]
     [:chunk {:style :bold} "Sponsor: "] $teacher_name "\n"
     [:chunk {:style :bold} "Division: "] $division "\n"
     [:chunk {:style :bold} "Teams: "] $team_names "\n"
     [:chunk {:style :bold} "Competitions: "] $comp_names
     [:spacer]]))

(def listing-template
(pdf/template
[:paragraph
     [:chunk {:style :bold} "Student: "] $name "\n"
     [:chunk {:style :bold} "Sponsor: "] $teacher_name "\n"
     [:chunk {:style :bold} "Division: "] $division "\n"
     [:chunk {:style :bold} "Teams: "] $team_names "\n" 
     [:chunk {:style :bold} "Competitions: "] $comp_names
     [:spacer]]))

(defn get-listings
[division]
(for [[teacher group] (sort-by #(key %) (group-by #(:teacher_name %) (stringify (map collapse-rows (get-scheduled-students division)))))]
[[:table
{:num-cols 2
:width 100
:widths [48 48]
:border false
:cell-border false}
(for [student (listing-template (sort-by #(:name %) group))]
[:cell {:align :left} student])]
[:pagebreak]]))

(defn make-listings
[division name]
(let [tables (get-listings division)] (pdf/pdf (conj tables (first tables)) name)))

(defn get-tables
[division]
(for [group (partition-all 8 (student-template (sort-by :teacher_name (stringify (map collapse-rows (get-scheduled-students division))))))]
       [[:table 
        {:num-cols 2
        :width 100
        :widths [48 48]
        :border false
        :cell-border false}
       (for [student group]
         [:cell {:align :left} student])]
       [:pagebreak]]))


(defn make-nametags
  [division name]
(let [tables (get-tables division)]
  (pdf/pdf (conj tables (first tables))
    name)))
