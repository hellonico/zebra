(ns zebra.linear-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn -solve-with-solver [_type]
  (println ">>" _type)
  (let [solver (new-mpsolver "IntegerProgrammingExample" _type)
        x1 (.makeIntVar solver 0.0 infinity "x1")
        x2 (.makeIntVar solver 0.0 infinity "x2")
        objective (.objective solver)]

    (doto objective ; Minimize x1 + 2 * x2
      (.setCoefficient x1 1)
      (.setCoefficient x2 2))

    (doto
     (.makeConstraint solver 17 infinity) ; 2 * x2 + 3 * x1 >= 17.
      (.setCoefficient x1 3)
      (.setCoefficient x2 2))

    (let [status (.solve solver)]
      (print-solution solver [x1 x2]))))

(deftest integerprogrammingexample
  (-solve-with-solver "CBC_MIXED_INTEGER_PROGRAMMING")
  (-solve-with-solver "GLOP_LINEAR_PROGRAMMING")
;   (-solve-with-solver "SCIP_MIXED_INTEGER_PROGRAMMING")  ; missing ?
)


(deftest linearexample-test
  (let [solver (new-mpsolver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeIntVar solver 0.0 infinity "x")
        y (.makeIntVar solver 0.0 infinity "y")
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
    (print-solution solver [x y])

    (is (=== (.solutionValue x) 6))
    (is (=== (.solutionValue y) 4))
    (is (=== (.value (.objective solver)) 34))))