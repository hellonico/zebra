(ns zebra.flow-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(deftest maxcostflow-test
  (let [tails  [0 0 0 0 1 2 3 3 4]
        heads  [1 2 3 4 3 4 4 5 5]
        capacities [5 8 5 3 4 5 6 6 4]
        max-flow (new-maxflow)]
    (dotimes [i (count tails)]
      (.addArcWithCapacity max-flow (nth tails i) (nth heads i) (nth capacities i)))
    (let [status (.solve max-flow 0 5) nb-arcs (.getNumArcs max-flow)]
      (is (== 9 nb-arcs))
      (dotimes [i nb-arcs]
        (print-maxflow-arc max-flow i)))))


(deftest mincostflow-test
  (let [numSources 4 numTargets 4
        costs [[90 75 75 80]
               [35 85 55 65]
               [125 95 90 105]
               [45 110 95 115]]
        expectedCost 275
        minCostFlow (new-mincostflow)]

    (doseq [source (range numSources)]
      (doseq [target (range numTargets)]
        (.addArcWithCapacityAndUnitCost
         minCostFlow
         source
         (+ numSources target)
         1
         (nth (nth costs source) target))))

    (doseq [node (range numSources)]
      (.setNodeSupply minCostFlow node 1)
      (.setNodeSupply minCostFlow (+ numSources node) -1))

    (.solve minCostFlow)

    (println "total flow = "  (.getOptimalCost minCostFlow)  "/" expectedCost)

    (is (= expectedCost (.getOptimalCost minCostFlow)))

    (dotimes [i (.getNumArcs minCostFlow)]
      (if (> (.getFlow minCostFlow i) 0)
        (println "From source "  (.getTail minCostFlow i)
                 " to target " (.getHead minCostFlow i)  ": cost "
                 (.getUnitCost minCostFlow i))))))


