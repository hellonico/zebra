(ns zebra.constraints-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :as pp]
            [zebra.utils :refer :all]
            [zebra.core :refer :all]))

(defn qr-search [solver db obj vars]
  (.newSearch solver db obj)
  (let [answers
        (loop [sol [] has-more (.nextSolution solver)]
          (if (not has-more)
            sol
            (recur (conj sol
                         (apply conj (map (fn [v]
                                       (if (not (.isArray (class v))) {(keyword (.name v)) (.value v)}
                                           {(keyword (.name (nth v 0))) (into [] (map #(.value %) v))})) vars)))
                   (.nextSolution solver))))]
    
    (print-statistics solver)
    (.endSearch solver)
    answers))

(deftest constraint-solver-test
  (let [solver (new-constraintsolver "Diet")
        n 4
        limits (long-array [500   ; calories
                            6     ; chocolate
                            10    ; sugar 
                            8])   ; fat

        price  (int-array [50 20 30 80])
        calories (int-array [400 200 150 500])
        chocolate (int-array [3 2 0 0])
        sugar (int-array [2 2 4 4])
        fat (int-array [2 4 1 5])

        x (.makeIntVarArray solver n 0 100 "x")
        cost (.var (.makeScalProd solver x price))
        obj (.makeMinimize solver cost 1)
        db (.makePhase solver x CHOOSE_PATH ASSIGN_MIN_VALUE)]

    (.setName cost "cost")

    (doto solver
      (.addConstraint (.makeScalProdGreaterOrEqual solver x calories (nth limits 0)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x chocolate (nth limits 1)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x sugar (nth limits 2)))
      (.addConstraint (.makeScalProdGreaterOrEqual solver x fat (nth limits 3))))

    (let [answers (qr-search solver db obj [cost x] )]
      ; price = 3x30 + 30 = 90 
      ; calories : 3 x 200 + 1 x 150 >= 500
      ; chocolate : 3 x 2 + 0 x 1 >= 6
      ; sugar : 3 x 2 + 1 x 1 >= 10
      ; far : 3 x 4 + 1 x 1 >= 8
      (is (= {:cost 90 :x0 [0 3 1 0]} (-> answers first)))
      (println answers))

    ))