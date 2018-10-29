(ns zebra.core
  (:import [com.google.ortools.algorithms KnapsackSolver KnapsackSolver$SolverType]
           [com.google.ortools.constraintsolver IntVar Solver]
            [com.google.ortools.linearsolver MPSolver$OptimizationProblemType MPSolver]))

(def loaded (atom false))
(defn load-natives []
  (let [os (.toLowerCase (apply str (take 3 (get (System/getProperties) "os.name"))))]
    (if (or (= "lin" os) (= "mac" os))
      (do
        (org.scijava.nativelib.NativeLoader/loadLibrary "cvrptw_lib" nil)
        (org.scijava.nativelib.NativeLoader/loadLibrary "ortools" nil)
        (org.scijava.nativelib.NativeLoader/loadLibrary "jniortools" nil))
      (org.scijava.nativelib.NativeLoader/loadLibrary "jniortools" nil)))
  (reset! loaded true))
(if (not @loaded) (load-natives))

(defn new-mpsolver [pname problem-type]
  (MPSolver.
   pname
   (MPSolver$OptimizationProblemType/valueOf problem-type)))

(defn new-constraintsolver [_name]
  (com.google.ortools.constraintsolver.Solver. _name))

(defn new-mincostflow[]
  (com.google.ortools.graph.MinCostFlow.))

(defn new-knapsack[]
  (KnapsackSolver.
   KnapsackSolver$SolverType/KNAPSACK_MULTIDIMENSION_BRANCH_AND_BOUND_SOLVER "test"))

(defn new-maxflow[]
  (com.google.ortools.graph.MaxFlow.))

; (defn new-intvar[]
;   (new com.google.ortools.constraintsolver.IntVar))

(defn new-arrayintvar[n]
  (make-array com.google.ortools.constraintsolver.IntVar n))

(def infinity (MPSolver/infinity))
(def -infinity (* -1 infinity))
