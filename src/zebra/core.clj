(ns zebra.core
  (:import [clojure.lang RT])
  (:import [com.google.ortools.linearsolver MPSolver$OptimizationProblemType MPSolver]))

(clojure.lang.RT/loadLibrary  "jniortools")

(defn new-solver [pname problem-type]
  (MPSolver.
   pname
   (MPSolver$OptimizationProblemType/valueOf problem-type)))

(defn -main [& args]
  (let [solver (new-solver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeNumVar solver 0.0 1.0 "x")
        y (.makeNumVar solver 0.0 2.0 "y")
        objective (.objective solver)]
    (.setMaximization objective)
    (.solve solver)
    (println "Solution:")
    (println "x=" (.solutionValue x))
    (println "y=" (.solutionValue x))))
