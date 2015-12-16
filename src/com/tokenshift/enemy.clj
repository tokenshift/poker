(ns com.tokenshift.enemy
  "A simple authentication framework for Ring apps."
  (:use [ring.middleware.basic-authentication]))

;; Strategy Middleware
;;
;; Example use:
;; (-> app
;;     (wrap-strategies "auth"
;;       "basic"  (basic-auth-strategy "username" "password")
;;       "google" (google-strategy "google-oauth2-client-app-id"
;;                                   "google-oauth2-client-secret")))

(defn with-strategies
  "Passes the request to one of several strategies, based on the strategy name."
  [req strategies]
  nil)

(defn wrap-strategies
  "Wraps a Ring app with several provided authentication strategies at the
  given base path and names."
  [app base-path strategy-name strategy & more]
  (let [strategies (apply hash-map strategy-name strategy more)]
    (fn [req]
      (or (with-strategies req strategies)
          (app req)))))