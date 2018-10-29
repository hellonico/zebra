(ns zebra.nqueens-test
  (:import [com.google.ortools.constraintsolver Solver])
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn n-queen
  ([n] (n-queen n nil))
  ([n _num]
   (let [solver (new-constraintsolver "solver")
         q (.makeIntVarArray  solver n 0 (dec n) "q")
         db (.makePhase solver q Solver/CHOOSE_MIN_SIZE_LOWEST_MAX Solver/ASSIGN_CENTER_VALUE)
         s (atom 0)]
     (.addConstraint solver (.makeAllDifferent solver q))

     (dotimes [i n]
       (dotimes [j i]
         (.addConstraint solver
                         (.makeNonEquality solver
                                           (.var (.makeSum solver (nth q i) i))
                                           (.var (.makeSum solver (nth q j) j))))
         (.addConstraint solver
                         (.makeNonEquality solver
                                           (.var (.makeSum solver (nth q i) (* -1 i)))
                                           (.var (.makeSum solver (nth q j) (* -1 j)))))))

     (.newSearch solver db)

     (while (and (.nextSolution solver) (or (nil? _num) (< @s _num)))
       (swap! s inc)
       (dotimes [i n]
         (print (.value (nth q i)) " "))
       (println ""))

     (.endSearch solver))))

(deftest n-queen-5
  (n-queen 5))
