(ns com.tokenshift.poker.config
  "Configuration values for the Poker web app."
  (:require [clojure.string :as s]))

(defn require!
  "Helper function to require an environment variable be present."
  [var-name]
  (or (System/getenv var-name)
      (throw (IllegalStateException. (str "Missing $" var-name)))))

(defn as-int!
  "Helper function to parse an environment variable as an integer."
  [var-name dflt]
  (let [value (or (System/getenv var-name) dflt)]
    (cond (nil? value)
          (throw (IllegalStateException. (str "Missing $" var-name)))

          (integer? value)
          value

          (re-matches #"\d+" value)
          (Integer. value)

          :else
          (throw (IllegalArgumentException. (format "$%s (\"%s\") is not an integer." var-name value))))))

(def amazon-oauth2-client-id (require! "AMAZON_OAUTH2_CLIENT_ID"))
(def amazon-oauth2-client-secret (require! "AMAZON_OAUTH2_CLIENT_SECRET"))
(def amazon-oauth2-redirect-uri (require! "AMAZON_OAUTH2_REDIRECT_URI"))

(def google-oauth2-client-id (require! "GOOGLE_OAUTH2_CLIENT_ID"))
(def google-oauth2-client-secret (require! "GOOGLE_OAUTH2_CLIENT_SECRET"))
(def google-oauth2-redirect-uri (require! "GOOGLE_OAUTH2_REDIRECT_URI"))

(def port (as-int! "PORT" 3000))