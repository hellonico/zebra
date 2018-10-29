(ns zebra.knapsack-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn solve-knapsack [profits weights capacities]
  (let [solver (new-knapsack)]

    (println "Solving knapsack with " (count profits) " items")
    (.init solver profits weights capacities)
    (let [computedProfit (.solve solver)]
      ; (println "Optimal_Profit = "  computedProfit  "/" optimalProfit)
      ; move to print
      (doseq [i (filter #(.bestSolutionContains solver %) (range (count profits)))]
        (println "[" (nth profits i)  "/" (nth (first weights) i) "]"))

      computedProfit)))

(deftest knapsack
  (let [profits (long-array
                 [360 83 59 130 431 67 230 52 93
                  125 670 892 600 38 48 147 78 256
                  63 17 120 164 432 35 92 110 22
                  42 50 323 514 28 87 73 78 15
                  26 78 210 36 85 189 274 43 33
                  10 19 389 276 312])
        weights  (into-array [(long-array [7 0 30 22 80 94 11 81 70
                                           64 59 18 0 36 3 8 15 42
                                           9 0 42 47 52 32 26 48 55
                                           6 29 84 2 4 18 56 7 29
                                           93 44 71 3 86 66 31 65 0
                                           79 20 65 52 13])])
        capacities (long-array [850])
        optimalProfit 7534]
    (is (= (solve-knapsack profits weights capacities) optimalProfit))))