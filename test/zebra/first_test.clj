(ns zebra.first-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(deftest first-test
  (let [solver (new-mpsolver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeIntVar solver 0.0 1.0 "x")
        y (.makeIntVar solver 0.0 2.0 "y")
        objective (.objective solver)]
    (.setMaximization objective)
    (.solve solver)
    (print-solution solver [x y])
    (is (= 1.0 (.solutionValue x)))
    (is (= 2.0 (.solutionValue y)))))

