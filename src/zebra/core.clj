(ns zebra.core
  (:import [com.google.ortools.linearsolver MPSolver$OptimizationProblemType MPSolver]))

(org.scijava.nativelib.NativeLoader/loadLibrary "jniortools" nil)

(defn new-solver [pname problem-type]
  (MPSolver.
   pname
   (MPSolver$OptimizationProblemType/valueOf problem-type)))
