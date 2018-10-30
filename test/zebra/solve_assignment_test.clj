(ns zebra.solve-assignment-test
  (:require [clojure.test :refer :all]
            [zebra.core :refer :all]))

"" "

  Set covering problem in Google CP Solver.

  This example is from the OPL example covering.mod
  '''
  Consider selecting workers to build a house. The construction of a
  house can be divided into a number of tasks, each requiring a number of
  skills (e.g., plumbing or masonry). A worker may or may not perform a
  task, depending on skills. In addition, each worker can be hired for a
  cost that also depends on his qualifications. The problem consists of
  selecting a set of workers to perform all the tasks, while minimizing the
  cost. This is known as a set-covering problem. The key idea in modeling
  a set-covering problem as an integer program is to associate a 0/1
  variable with each worker to represent whether the worker is hired.
  To make sure that all the tasks are performed, it is sufficient to
  choose at least one worker by task. This constraint can be expressed by a
  simple linear inequality.
  '''

  Solution from the OPL model (1-based)
  '''
  Optimal solution found with objective: 14
  crew= {23 25 26}
  '''

  Solution from this model (0-based):
  '''
  Total cost 14
  We should hire these workers:  22 24 25
  '''


  Compare with the following models:
  * Comet: http://hakank.org/comet/covering_opl.co
  * MiniZinc: http://hakank.org/minizinc/covering_opl.mzn
  * ECLiPSe: http://hakank.org/eclipse/covering_opl.ecl
  * Gecode: http://hakank.org/gecode/covering_opl.cpp
  * SICStus: http://hakank.org/sicstus/covering_opl.pl

  This model was created by Hakan Kjellerstrand (hakank@gmail.com)
  Also see my other Google CP Solver models:
  http://www.hakank.org/google_or_tools/
" ""

(defn solve-assignment [qualified cost]
  (let [num-tasks (count qualified)
        num-workers (count cost)
        solver (new-constraintsolver "work")
        hire (.makeIntVarArray solver num-workers 0 1 "workers")
        total-cost (.var (.makeScalProd solver hire (int-array cost)))
        objective (.makeMinimize solver total-cost 1)
        db (.makePhase solver hire CHOOSE_FIRST_UNBOUND ASSIGN_MIN_VALUE)]

    (.newSearch solver db objective)

    (dotimes [j num-tasks]
      (let [len (count (nth qualified j))
            tmp (new-arrayintvar len)]
        (dotimes [c len]
          (let [qj (nth hire (dec (nth (nth qualified j) c)))]
            (aset tmp c qj)))
        (.addConstraint solver (.makeGreaterOrEqual solver (.var (.makeSum solver tmp)) 1))))

    (let [seq-solution
          (loop [sols [] has-next (.nextSolution solver)]
            (if (not has-next)
              sols
              (recur (conj sols {:cost (.value total-cost)
                                 :workers (into [] (keep-indexed #(when (not (= 0 %2)) (inc %1)) (map #(.value %) hire)))})
                     (.nextSolution solver))))]
      (print-statistics solver)

      (.endSearch solver)
      seq-solution)))

(defn print-solutions [_solutions]
  (doseq [sol _solutions]
    (println "=> Solution <=")
    (println "Cost: " (:cost sol))
    (print "Hire: " (clojure.string/join ", " (:workers sol)) "\n")
    (println "=> END <=")))

;
; TESTING
; 

(def qualified
  " one line per task
   each task can be done by any of the workers of the task
   for example, the first task
   [1  9 19  22  25  28  31]
   can be done by any of the workers 1, 9, 19,  22,  25,  28,  31
  "
  [[1  9 19  22  25  28  31]
   [2 12 15 19 21 23 27 29 30 31 32]
   [3 10 19 24 26 30 32]
   [4 21 25 28 32]
   [5 11 16 22 23 27 31]
   [6 20 24 26 30 32]
   [7 12 17 25 30 31]
   [8 17 20 22 23]
   [9 13 14  26 29 30 31]
   [10 21 25 31 32]
   [14 15 18 23 24 27 30 32]
   [18 19 22 24 26 29 31]
   [11 20 25 28 30 32]
   [16 19 23 31]
   [9 18 26 28 31 32]])

(def cost
  "cost of each worker"
  [1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 3 3 3 3 4 4 4 4 5 5 5 6 6 6 7 8 9])

(deftest zebra.solve-assignment-test
  (let [solution (solve-assignment qualified cost)]
    ; (clojure.pprint/pprint seq-solution)
    (is (some #(=  {:cost 14, :workers [23 25 26]} %) solution))
    (print-solutions solution)))