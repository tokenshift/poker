(defproject poker "0.0.1"
  :description "Agile/scrum planning poker web app."
  :url "http://github.com/tokenshift/poker"
  :license {:name "The MIT License (MIT)"
            :url "https://raw.githubusercontent.com/tokenshift/poker/master/LICENSE"}
  :dependencies [[cheshire "5.5.0"]
                 [clj-base64 "0.0.2"]
                 [clj-http "2.0.0"]
                 [org.clojure/clojure "1.7.0"]
                 [ring "1.4.0"]
                 [ring/ring-mock "0.3.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler com.tokenshift.poker.main/handler
         :auto-reload? true
         :auto-refresh? true}
  :main com.tokenshift.poker.main)
