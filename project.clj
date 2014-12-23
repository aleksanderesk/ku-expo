(defproject ku-expo "0.1.0-SNAPSHOT"
  :description "Registration/Scoring/Administration for KU Engineering Expo"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; REST dependencies
                 [ring "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [compojure "1.3.1"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [crypto-password "0.1.3"]
                 [com.cemerick/friend "0.2.1"]
                 ;; Database dependencies
                 [org.clojure/java.jdbc "0.3.3"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [yesql "0.4.0"]
                 ;; Clojurescript dependencies
                 [org.clojure/clojurescript "0.0-2411"]
                 [cljs-ajax "0.1.5"]
                 [formative "0.8.8"]
                 ;; Build dependencies
                 [clj-time "0.8.0"]
                 [environ "1.0.0"]]
  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.3"]
            [lein-environ "1.0.0"]]
  :source-paths ["src/clj"]
  :main ku-expo.handler
  :ring {:handler ku-expo.handler/app}
  :cljsbuild {:builds [{:source-paths ["src/cljs/ku_expo/login"]
                        :compiler {:output-to "resources/public/js/login/main.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]}
  :profiles {:dev {:env {:db-url "localhost"
                         :db-port 3306
                         :db-name "kuexpo"
                         :db-user "root"
                         :db-pass "password"}}})
