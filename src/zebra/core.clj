(ns zebra.core
  ; (:import [clojure.lang RT])
  (:import [com.google.ortools.linearsolver MPSolver$OptimizationProblemType MPSolver]))

; (org.scijava.nativelib.NativeLoader/loadLibrary "dimacs" nil)
; (org.scijava.nativelib.NativeLoader/loadLibrary "fap" nil)
(defn load-natives []
  (let [os (.toLowerCase (apply str (take 3 (get (System/getProperties) "os.name"))))]
    (if (or (= "lin" os) (= "mac" os))
      (do
        (org.scijava.nativelib.NativeLoader/loadLibrary "cvrptw_lib" nil)
        (org.scijava.nativelib.NativeLoader/loadLibrary "ortools" nil)
        (org.scijava.nativelib.NativeLoader/loadLibrary "jniortools" nil))
      (org.scijava.nativelib.NativeLoader/loadLibrary "jniortools" nil))))
(load-natives)


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
