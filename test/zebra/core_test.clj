(ns zebra.core-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(deftest firstsolver-test
  (let [solver (new-mpsolver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeNumVar solver 0.0 1.0 "x")
        y (.makeNumVar solver 0.0 2.0 "y")
        objective (.objective solver)]
    (.setMaximization objective)
    (.solve solver)
    (is (= 1.0 (.solutionValue x)))
    (is (= 2.0 (.solutionValue y)))))

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
    (println "cost: " (.value cost))
    (is (= 90 (.value cost)))))

(deftest linearexample-test
  (let [solver (new-mpsolver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeNumVar solver 0.0 infinity "x")
        y (.makeNumVar solver 0.0 infinity "y")
        objective (.objective solver)]

    (doto objective
      (.setCoefficient x 3)
      (.setCoefficient y 4)
      (.setMaximization))

    (doto
     (.makeConstraint solver -infinity 14.0) ; x + 2y <= 14
      (.setCoefficient x 1)
      (.setCoefficient y 2))
    (doto
     (.makeConstraint solver 0.0 infinity) ; 3x - y >= 0
      (.setCoefficient x 3)
      (.setCoefficient y -1))
    (doto
     (.makeConstraint solver -infinity 2.0) ; x - y <= 2
      (.setCoefficient x 1)
      (.setCoefficient y -1))

    (.solve solver)

    (is (=== (.solutionValue x) 6))
    (is (=== (.solutionValue y) 4))
    (is (=== (.value (.objective solver)) 34))))