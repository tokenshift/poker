(ns com.tokenshift.enemy.amazon
  "Strategies for authenticating using Login with Amazon."
  (:require [com.tokenshift.enemy.oauth2 :as oauth2]))

(def base-config
  { :authorization-url "https://www.amazon.com/ap/oa"
    :token-url "https://api.amazon.com/auth/o2/token"
    :profile-url "https://api.amazon.com/user/profile"
    :scopes ["profile"] })

(defn amazon-oauth2-strategy
  [handler client-id client-secret redirect-uri]
  (let [config (assoc base-config
                      :redirect-uri redirect-uri
                      :client-id client-id
                      :client-secret client-secret)]
    (oauth2/oauth2-strategy handler config)))