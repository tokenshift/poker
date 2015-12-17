(ns com.tokenshift.enemy.oauth2
  "Generic OAuth2 authentication base strategy."
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as s]
            [ring.util.response :refer [redirect]])
  (:import [java.net URLEncoder]))

(defn get-authorization-url
  [{:keys [authorization-url client-id redirect-uri scopes] :as config}]
  (str authorization-url
    "?scope=" (URLEncoder/encode (s/join " " scopes))
    "&redirect_uri=" (URLEncoder/encode redirect-uri)
    "&response_type=code"
    "&client_id=" (URLEncoder/encode client-id)))

(defn get-access-token
  [authorization-code {:keys [token-url client-id client-secret redirect-uri] :as config}]
  (-> (http/post token-url
                 {:form-params {:code authorization-code
                                :client_id client-id
                                :client_secret client-secret
                                :redirect_uri redirect-uri
                                :grant_type "authorization_code"}
                  :accept :json
                  :as :json
                  :throw-entire-message? true})
      :body
      :access_token))

(defn get-user-profile
  [access-token {:keys [profile-url] :as config}]
  (-> (http/get profile-url
                { :headers {"Authorization" (str "Bearer " access-token)}
                  :accept :json
                  :as :json
                  :throw-entire-message? true })
      :body))

(defn oauth2-strategy
  [handler config]
  (fn [req]
    (if-let [code (get-in req [:query-params "code"])]
      (if-let [token (get-access-token code config)]
        (if-let [profile (get-user-profile token config)]
          (handler (assoc req :auth profile))
          nil) ; TODO: What to do when the profile couldn't be fetched?
        nil) ; TODO: What to do when we didn't get an access token?
      (redirect (get-authorization-url config)))))