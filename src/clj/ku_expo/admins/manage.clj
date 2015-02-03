(ns ku-expo.admins.manage
  (:require [ring.util.response :refer [response resource-response]]
            [ring.util.json-response :refer [json-response]]
            [cemerick.friend :as friend]
            [ku-expo.utils.db :as db]))

(defn manage-admin
  [req]
  (resource-response "admin.html" {:root "public/html"}))

(defn get-logistics-summary
  [req]
  (json-response
    (db/get-logistics-summary)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 
;; Competitions Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-competitions
  []
  (json-response (db/get-competitions)))
