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
