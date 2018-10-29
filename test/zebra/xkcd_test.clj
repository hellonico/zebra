(ns zebra.xkcd-test
  (:require [clojure.test :refer :all]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn xkcd [arr total min max]
  (let [iarr (int-array arr)
        solver (new-constraintsolver "xkcd")
        x (.makeIntVarArray solver (count iarr) min max "iarr")
        db (.makePhase solver x CHOOSE_FIRST_UNBOUND ASSIGN_MIN_VALUE)]
    (.addConstraint solver
                    (.makeEquality solver
                                   (.var (.makeScalProd solver x iarr))
                                   total))
    (.newSearch solver db)
    ; (print-n-array-solutions solver x nil) 
    (let [sol (solutions solver x)]
      (print-statistics solver)
      (.endSearch solver)
      sol)))

(deftest prices-with-xkcd
  (is (=
       [[1 0 0 2 0 1] [7 0 0 0 0 0]]
       (xkcd [215 275 335 355 420 580] 1505 0 10))))

(deftest prices-with-xkcd-2
  (is (=
  [[0 0 0 0 2 2] [0 3 1 0 2 0] [1 1 2 0 2 0] [3 0 0 1 1 1]]
  (xkcd [215 275 335 355 420 580] 2000 0 3))))