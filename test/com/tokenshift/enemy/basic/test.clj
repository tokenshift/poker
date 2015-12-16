(ns com.tokenshift.enemy.basic.test
  (:use clojure.test
        com.tokenshift.enemy.basic
        ring.mock.request))

(deftest test-basic-credentials
  (let [request (request :get "/")]
    (is (nil? (basic-credentials request))))
  (let [request (-> (request :get "/")
                    (header "Authorization" "SomethingElse QWxhZGRpbjpvcGVuIHNlc2FtZQ=="))]
    (is (nil? (basic-credentials request))))
  (let [request (-> (request :get "/")
                    (header "Authorization" "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=="))
        [username password] (basic-credentials request)]
    (is (= "Aladdin" username))
    (is (= "open sesame" password))))

(deftest test-basic-auth-strategy
  (let [app (fn [req] "THE RESPONSE")
        app' (basic-auth-strategy app "Aladdin" "open sesame")]
    (let [response (app' {})]
      (is (= 401 (:status response)))
      (is (not (nil? (get-in response [:headers "WWW-Authenticate"]))))
      (is (re-matches #"^Basic\s*" (get-in response [:headers "WWW-Authenticate"]))))
    (let [request (-> (request :get "/")
                      (header "Authorization" "SomethingElse QWxhZGRpbjpvcGVuIHNlc2FtZQ=="))
          response (app' request)]
      (is (= 401 (:status response)))
      (is (not (nil? (get-in response [:headers "WWW-Authenticate"]))))
      (is (re-matches #"^Basic\s*" (get-in response [:headers "WWW-Authenticate"]))))
    (let [request (-> (request :get "/")
                      (header "Authorization" "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=="))
          response (app' request)]
      (is (= "THE RESPONSE" response)))))
