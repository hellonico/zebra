(defproject zebra "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main zebra.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ; :jvm-opts [~(str "-Djava.library.path=lib/"  (.toLowerCase (apply str (take 3 (get (System/getProperties) "os.name")))))]
  :repositories [["vendredi" {:url "https://repository.hellonico.info/repository/hellonico/" :creds :gpg}]]
  :dependencies [[com.google/ortools "6.7.4981"]
                 [org.hellonico/ortools-native "6.7.4981"]
                 [org.slf4j/slf4j-simple "1.8.0-beta2"]
                 [com.google.protobuf/protobuf-java "3.6.1"] 
                 [org.scijava/native-lib-loader "2.3.1"]
                 [org.clojure/clojure "1.9.0"]])

