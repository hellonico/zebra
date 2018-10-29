(ns zebra.utils)

;
; TESTING
;
(defn difference ^double [^double x ^double y]
  (/ (Math/abs (- x y))
     (max (Math/abs x) (Math/abs y))))
(defn close? [tolerance x y]
  (< (difference x y) tolerance))
(def === (partial close? 0.001))

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
         (dotimes [i (count q)]
           (print (.value (nth q i)) " "))
         (println ""))))