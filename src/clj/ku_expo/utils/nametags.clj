(ns ku-expo.utils.nametags
  (:require [ku-expo.utils.db :as db]
            [clj-pdf.core]))

(defn get-grouped-entries
  []
  (->> (db/get-name-tags)
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


