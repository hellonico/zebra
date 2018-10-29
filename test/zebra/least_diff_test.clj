(ns zebra.least-diff-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn least-diff []
  (let [base 10
        solver (new-constraintsolver "leastdiff")
        all
        (into-array
         (map
          #(.makeIntVar solver 0 (dec base) %)
          (str/split "abcdefghij" #"")))
        coeffs (int-array [10000 1000 100 10 1])
        abcde (into-array (take 5 all))
        x (.var
           (.makeScalProd solver
                          abcde
                          coeffs))
        fghij (into-array (take-last 5 all))
        y (.var
           (.makeScalProd solver
                          fghij
                          coeffs))
        diff (-> solver
                 (.makeDifference x y)
                 (.var))
        obj (.makeMinimize solver diff 1)
        db (.makePhase solver all CHOOSE_PATH ASSIGN_MIN_VALUE)]

    (.addConstraint solver (.makeGreater solver (first abcde) 0)) ; a > 0
    (.addConstraint solver (.makeGreater solver (first fghij) 0)) ; f > 0
    (.addConstraint solver (.makeAllDifferent solver all))
    (.newSearch solver db obj)
    (print-statistics solver)
    (loop [i 0 hasNext (.nextSolution solver)]
      (if (not hasNext)
        (do (.endSearch solver) i)
        (recur (inc i) (.nextSolution solver))))))

(deftest least-diff-testing
  (is (= 49 (least-diff))))