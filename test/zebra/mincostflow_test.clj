(ns zebra.mincostflow-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(deftest mincostflow-test
  (let [numSources 4 numTargets 4
        costs [[90 75 75 80]
               [35 85 55 65]
               [125 95 90 105]
               [45 110 95 115]]
        expectedCost 275
        minCostFlow (com.google.ortools.graph.MinCostFlow.)]

    (doseq [source (range numSources)]
      (doseq [target (range numTargets)]
        (.addArcWithCapacityAndUnitCost minCostFlow source (+ numSources target) 1 (nth (nth costs source) target))))

    (doseq [node (range numSources)]
      (.setNodeSupply minCostFlow node 1)
      (.setNodeSupply minCostFlow (+ numSources node) -1))
    (.solve minCostFlow)

    (println "total flow = "  (.getOptimalCost minCostFlow)  "/" expectedCost)
    (is (= 275 (.getOptimalCost minCostFlow)))
    (dotimes [i (.getNumArcs minCostFlow)]
      (if (> (.getFlow minCostFlow i) 0)
        (println "From source "  (.getTail minCostFlow i)
                 " to target " (.getHead minCostFlow i)  ": cost "
                 (.getUnitCost minCostFlow i))))))