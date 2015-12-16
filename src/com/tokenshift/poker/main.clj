(ns com.tokenshift.poker.main
  (:require [com.tokenshift.enemy.basic :refer [basic-auth-strategy]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class :main true))

(def handler
  (-> (fn [req] {:status 200 :body "This is a test"})
      (basic-auth-strategy "testuser" "testpassword")))

(defn start!
  []
  (run-jetty handler {:port 3000}))

(defn -main [] (start!))