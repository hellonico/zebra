(ns zebra.core
  (:import [com.google.ortools.algorithms KnapsackSolver KnapsackSolver$SolverType]
           [com.google.ortools.constraintsolver ConstraintSolverParameters IntVar Solver]
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

(defn- make-parameters [traceSearch]
  (-> (ConstraintSolverParameters/newBuilder)
      (.mergeFrom (Solver/defaultSolverParameters))
      (.setTraceSearch traceSearch)
      (.build)))

(defn new-constraintsolver 
([_name]
  (Solver. _name))
([_name _trace]
  (Solver. _name (make-parameters _trace))))

(defn new-mincostflow[]
  (com.google.ortools.graph.MinCostFlow.))

(defn new-knapsack[]
  (KnapsackSolver.
   KnapsackSolver$SolverType/KNAPSACK_MULTIDIMENSION_BRANCH_AND_BOUND_SOLVER "test"))

(defn new-maxflow[]
  (com.google.ortools.graph.MaxFlow.))

;
; CONVENIENT
;
(def infinity (MPSolver/infinity))
(def -infinity (* -1 infinity))

(def CHOOSE_FIRST_UNBOUND Solver/CHOOSE_FIRST_UNBOUND)
(def ASSIGN_MIN_VALUE Solver/ASSIGN_MIN_VALUE)
(def CHOOSE_MIN_SIZE_LOWEST_MAX Solver/CHOOSE_MIN_SIZE_LOWEST_MAX)
(def ASSIGN_CENTER_VALUE Solver/ASSIGN_CENTER_VALUE)

(defn new-arrayintvar 
  [n]
  (make-array com.google.ortools.constraintsolver.IntVar n))

; make same for num



;
; PRINTING
;

(defn print-maxflow-arc [max-flow i]
  (println
   "From source "
   (.getTail max-flow i)
   " to target "
   (.getHead max-flow i)
   ": "
   (.getFlow max-flow i)
   "/"
   (.getCapacity max-flow i)))

(defn print-statistics [solver]
  (println "Solutions: " (.solutions solver))
  (println "Failures: " (.failures solver))
  (println "Branches: " (.branches solver))
  (println "Wall time: " (.wallTime solver) " ms"))

(defn print-solution [solver values]
  (println "Wall time: " (.wallTime solver) " ms")
  (println "Optimal objective value = " (.value (.objective solver)))
  (doseq [value values]
    (println  (.name value) "=> "  (.solutionValue value))))

(defn print-n-array-solutions [solver q _num]
  (let [s (atom 0)]
    (while (and (.nextSolution solver) (or (nil? _num) (< @s _num)))
      (swap! s inc)
        ;  (dotimes [i (count q)]
        ;    (print (.value (nth q i)) " "))
      (println (clojure.string/join ", " (map #(.value %) q))))))

(defn solutions [solver q]
  (loop [sol [] hasMore (.nextSolution solver)]
       (if (not hasMore)
         sol
         (recur 
          (conj sol (into [] (map #(.value %) q)))
          (.nextSolution solver)))))
