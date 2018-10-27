(ns zebra.allinternal-test
  (:import [com.google.ortools.constraintsolver Solver])
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn solver-for-n [n]
  (println ">> solver for n:" n)
  (let [solver (new-constraintsolver "AllInterval")
        x  (.makeIntVarArray solver n 0 (dec n) "x")
        diffs (.makeIntVarArray solver (dec n) 1 (dec n) "diffs")
        db (.makePhase solver x Solver/CHOOSE_FIRST_UNBOUND Solver/ASSIGN_MIN_VALUE)]

    ; all x should be different
    (.addConstraint solver (.makeAllDifferent solver x))
    ; all diffs should be different
    (.addConstraint solver (.makeAllDifferent solver diffs))

    (dotimes [k (dec n)]
      (.addConstraint solver
        ; difference between x and x+1 is equal to first diff
        ; x    : 0 1 
        ; diff : 1
        ; because difference between 0 and 1 in the x values, is equal to absolute difference of 1
        (.makeEquality solver (nth diffs k) (.var (.makeAbs solver (.makeDifference solver (nth x (inc k)) (nth x k)))))))

    ; the first x is lower than last x
    (.addConstraint solver (.makeLess solver (nth x 0) (nth x (dec n))))
    ; first diff is lower than second diffs
    (.addConstraint solver (.makeLess solver (nth diffs 0) (nth diffs 1)))

    (.newSearch solver db)
    ; (while (.nextSolution solver)
    ;   (println "x:")
    ;   (println (clojure.string/join ", " (map #(.value %) x)))
    ;   (println "diffs:")
    ;   (println (clojure.string/join ", " (map #(.value %) diffs))))

    (.endSearch solver)
    (print-statistics solver)))

(deftest hello
  (solver-for-n 3)
  (solver-for-n 5)
  (solver-for-n 12))