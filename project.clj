(defproject zebra "0.1.1-SNAPSHOT"
  :description "Poor Man's Wrapper for Google's ortools"
  :url "https://github.com/hellonico/zebra"
  :main zebra.core
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy" "vendredi"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :license {:name "Apache License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["vendredi" {:url "https://repository.hellonico.info/repository/hellonico/" :creds :gpg}]]
  :dependencies [[com.google/ortools "6.7.4981"]
                 [org.hellonico/ortools-native "6.7.4981"]
                 [org.slf4j/slf4j-simple "1.8.0-beta2"]
                 [com.google.protobuf/protobuf-java "3.6.1"] 
                 [org.scijava/native-lib-loader "2.3.1"]
                 [org.clojure/clojure "1.9.0"]])