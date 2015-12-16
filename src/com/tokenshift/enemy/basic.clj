(ns com.tokenshift.enemy.basic
  "HTTP Basic Auth authentication strategy."
  (:require [remvee.base64 :as b64]
            [ring.util.response :refer [get-header header response status]]))

(defn basic-credentials
  "Returns the username and password from the Authorization request header, or
  nil if it wasn't present."
  [req]
  (when-let [[_ username password] (some->> (get-header req "authorization")
                                            (re-find #"^Basic (.*)$")
                                            (last)
                                            (b64/decode-str)
                                            (re-matches #"^(.*):(.*)$"))]
    [username password]))

(defn basic-auth-strategy
  "Authenticates a user using HTTP Basic Auth. Takes a function that will be
  called with the provided username and password, and should return true if the
  credentials are valid; or a single username and password that must match the
  provided credentials."
  ([handler auth-fn]
    (fn [req]
      (if-let [[username password] (basic-credentials req)]
        (if (auth-fn username password)
          (handler (assoc req :auth {:username username}))
          (-> (response "Invalid username/password")
              (status 401)
              (header "WWW-Authenticate" "Basic")))
        (-> (response "Authentication Required")
            (status 401)
            (header "WWW-Authenticate" "Basic")))))
  ([handler username password]
    (basic-auth-strategy handler #(and (= username %1) (= password %2)))))