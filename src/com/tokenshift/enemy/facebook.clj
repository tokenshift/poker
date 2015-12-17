(ns com.tokenshift.enemy.facebook
  "Strategies for authenticating using Facebook."
  (:require [com.tokenshift.enemy.oauth2 :as oauth2]))

(def base-config
  { :authorization-url "https://www.facebook.com/dialog/oauth"
    :token-url "https://graph.facebook.com/v2.3/oauth/access_token"
    :profile-url "https://graph.facebook.com/me"
    :scopes ["public_profile"] })

(defn facebook-oauth2-strategy
  [handler client-id client-secret redirect-uri]
  (let [config (assoc base-config
                      :redirect-uri redirect-uri
                      :client-id client-id
                      :client-secret client-secret)]
    (oauth2/oauth2-strategy handler config)))