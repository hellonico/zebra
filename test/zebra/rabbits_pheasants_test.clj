(ns zebra.rabbits-pheasants-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn rabbits-and-pheasants [heads legs]
  (let [solver (new-constraintsolver "rabbits" false)
        rabbits (.makeIntVar solver 0 100 "rabbits")
        pheasants (.makeIntVar solver 0 100 "pheasants")
        db (.makePhase solver rabbits pheasants CHOOSE_FIRST_UNBOUND ASSIGN_MIN_VALUE)]
  
    (.addConstraint solver (.makeEquality solver (.makeSum solver rabbits pheasants) heads))
    (.addConstraint solver
                    (.makeEquality solver
                                   (.makeSum solver
                                             (.makeProd solver rabbits 4)
                                             (.makeProd solver pheasants 2))
                                   legs))
    (.newSearch solver db)
  
    (if (.nextSolution solver)
      (do
        (println (.name rabbits) ">" (.value rabbits))
        (println (.name pheasants) ">" (.value pheasants)))
      (println "no solution"))
    
    
    (let [result [(.value rabbits) (.value pheasants)]]
      (.endSearch solver)         
      result)))

(deftest rabbits-and-pheasants-testing
  (is (= [8 12] (rabbits-and-pheasants 20 56))))
