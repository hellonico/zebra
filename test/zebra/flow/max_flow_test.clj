(ns zebra.flow.max-flow-test
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

