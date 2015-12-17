(ns com.tokenshift.enemy.google
  "Strategies for authenticating using Google login."
  (:require [com.tokenshift.enemy.oauth2 :as oauth2]))

(def base-config
  { :authorization-url "https://accounts.google.com/o/oauth2/auth"
    :token-url "https://accounts.google.com/o/oauth2/token"
    :profile-url "https://www.googleapis.com/plus/v1/people/me"
    :scopes ["https://www.googleapis.com/auth/plus.me"] })

(defn google-oauth2-strategy
  [handler client-id client-secret redirect-uri]
  (let [config (assoc base-config
                      :redirect-uri redirect-uri
                      :client-id client-id
                      :client-secret client-secret)]
    (oauth2/oauth2-strategy handler config)))