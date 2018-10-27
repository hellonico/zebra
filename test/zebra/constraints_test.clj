(ns zebra.constraints-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(deftest constraint-solver-test
  (let [n 4
        price  (int-array [50 20 30 80])
        limits (long-array [500  6 10 8])
        calories (int-array [400 200 150 500])
        chocolate (int-array [3 2 0 0])
        sugar (int-array [2 2 4 4])
        fat (int-array [2 4 1 5])
        solver (new-constraintsolver "Diet")
        x (.makeIntVarArray solver n 0 100 "x")
        cost (.var (.makeScalProd solver x price))
        obj (.makeMinimize solver cost 1)
        db
        (.makePhase solver x
                    com.google.ortools.constraintsolver.Solver/CHOOSE_PATH
                    com.google.ortools.constraintsolver.Solver/ASSIGN_MIN_VALUE)]
    (doto solver
      (.addConstraint (.makeScalProdGreaterOrEqual solver x calories (nth limits 0)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x chocolate (nth limits 1)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x sugar (nth limits 2)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x fat (nth limits 3))))

    (.newSearch solver db obj)
    (.nextSolution solver)
    
    (is (= 90 (.value cost)))))