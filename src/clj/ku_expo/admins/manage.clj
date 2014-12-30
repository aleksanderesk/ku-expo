(ns ku-expo.admins.manage
  (:require [ring.util.response :refer [response resource-response]]
            [ring.util.json-response :refer [json-response]]
            [cemerick.friend :as friend]
            [ku-expo.utils.db :as db]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 
;; Competitions Operations
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-competitions
  []
  (json-response (db/get-competitions)))
