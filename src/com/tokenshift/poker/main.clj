(ns com.tokenshift.poker.main
  (:require [cheshire.core :as json]
            [com.tokenshift.enemy.amazon :refer [amazon-oauth2-strategy]]
            [com.tokenshift.enemy.basic :refer [basic-auth-strategy]]
            [com.tokenshift.enemy.google :refer [google-oauth2-strategy]]
            [com.tokenshift.poker.config :as config]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class :main true))

(def handler
  (-> (fn [req] {:status 200 :body (json/generate-string (:auth req))})
      (google-oauth2-strategy config/google-oauth2-client-id
                              config/google-oauth2-client-secret
                              config/google-oauth2-redirect-uri)))
      ; (amazon-oauth2-strategy config/amazon-oauth2-client-id
      ;                         config/amazon-oauth2-client-secret
      ;                         config/amazon-oauth2-redirect-uri)))

(defn start!
  []
  (run-jetty handler {:port 3000}))

(defn -main [] (start!))