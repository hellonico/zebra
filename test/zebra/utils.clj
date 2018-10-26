(ns zebra.utils)


(defn difference ^double [^double x ^double y]
  (/ (Math/abs (- x y))
     (max (Math/abs x) (Math/abs y))))
(defn close? [tolerance x y]
  (< (difference x y) tolerance))
(def === (partial close? 0.001))
