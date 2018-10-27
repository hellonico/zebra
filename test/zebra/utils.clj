(ns zebra.utils)


(defn difference ^double [^double x ^double y]
  (/ (Math/abs (- x y))
     (max (Math/abs x) (Math/abs y))))
(defn close? [tolerance x y]
  (< (difference x y) tolerance))
(def === (partial close? 0.001))


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