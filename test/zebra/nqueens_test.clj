(ns zebra.nqueens-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn n-queen
  ([n] (n-queen n nil))
  ([n _num]
   (let [solver (new-constraintsolver "nqueens")
         q (.makeIntVarArray  solver n 0 (dec n) "q")
         db (.makePhase solver q CHOOSE_MIN_SIZE_LOWEST_MAX ASSIGN_CENTER_VALUE)]
     (.addConstraint solver
                     (.makeAllDifferent solver q))

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
     (print-n-array-solutions solver q _num)
     (.endSearch solver))))

(deftest n-queen-5
  (n-queen 5))
