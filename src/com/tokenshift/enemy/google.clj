(ns com.tokenshift.enemy.google
  "Strategies for authenticating using Google login."
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clj-oauth2.core :refer [get-tokens request-auth-url]]
            [clj-oauth2.google]
            [ring.util.response :refer [redirect]]))

(def user-profile-scope "https://www.googleapis.com/auth/plus.me")
(def user-profile-url "https://www.googleapis.com/plus/v1/people/me")

(defn get-user-profile
  [access-token]
  (-> (http/get user-profile-url
                { :headers {"Authorization" (str "Bearer " access-token)}
                  :accept :json
                  :as :json })
      :body))

(defn get-access-token
  [google-config authorization-code]
  (when-let [tokens (get-tokens google-config authorization-code)]
    (:access_token tokens)))

(defn google-oauth2-strategy
  [handler client-id client-secret redirect-uri]
  (let [config (assoc clj-oauth2.google/config
                      :redirect-uri redirect-uri
                      :client-id client-id
                      :client-secret client-secret
                      :all-scopes {:me user-profile-scope}
                      :scopes [:me])]
    (fn [req]
      (if-let [code (get-in req [:query-params "code"])]
        (if-let [token (get-access-token config code)]
          (if-let [profile (get-user-profile token)]
            (handler (assoc req :auth profile))
            nil) ; TODO: What to do when the profile couldn't be fetched?
          nil) ; TODO: What to do when we didn't get an access token?
        (redirect (request-auth-url config))))))