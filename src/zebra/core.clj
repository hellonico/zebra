(ns zebra.core
  (:import [com.google.ortools.linearsolver MPSolver$OptimizationProblemType MPSolver]))

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

(def infinity (MPSolver/infinity))
(def -infinity (* -1 infinity))

(comment 
(def n 4)
(def price  (int-array [50 20 30 80]))
(def limits (long-array [500  6 10 8]))
(def calories (int-array [400 200 150 500]))
(def chocolate (int-array [3 2 0 0]))
(def sugar (int-array [2 2 4 4]))
(def far (int-array [2 4 1 5]))

(def solver (new-constraintsolver "Diet"))
(def x (.makeIntVarArray solver n 0 100 "x"))
(def cost (.var (.makeScalProd solver x price)))

(.addConstraint solver (.makeScalProdGreaterOrEqual solver x calories (nth limits 0)))
(.addConstraint solver (.makeScalProdGreaterOrEqual solver x chocolate (nth limits 1)))
(.addConstraint solver (.makeScalProdGreaterOrEqual solver x sugar (nth limits 2)))
(.addConstraint solver (.makeScalProdGreaterOrEqual solver x far (nth limits 3)))
(def obj (.makeMinimize solver cost 1))

(def db 
(.makePhase solver x com.google.ortools.constraintsolver.Solver/CHOOSE_PATH com.google.ortools.constraintsolver.Solver/ASSIGN_MIN_VALUE))
(.newSearch solver db obj)
(.nextSolution solver)

)