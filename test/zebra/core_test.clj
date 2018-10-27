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

