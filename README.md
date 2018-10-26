# zebra

# requirements

- jvm >9
- leiningen

# examples

```
(let [solver (new-solver "myprogram" "GLOP_LINEAR_PROGRAMMING")
        x (.makeNumVar solver 0.0 1.0 "x")
        y (.makeNumVar solver 0.0 2.0 "y")
        objective (.objective solver)]
    (.setMaximization objective)
    (.solve solver)
    (is (= 1.0 (.solutionValue x)))
    (is (= 2.0 (.solutionValue y))))
```

```
(let [solver (new-solver "myprogram" "GLOP_LINEAR_PROGRAMMING")
    x (.makeNumVar solver 0.0 infinity "x")
    y (.makeNumVar solver 0.0 infinity "y")
    objective (.objective solver)]
  (doto objective
      (.setCoefficient x 3)
      (.setCoefficient y 4)
      (.setMaximization))

  (doto
      (.makeConstraint solver -infinity 14.0) ; x + 2y <= 14
      (.setCoefficient x 1)
      (.setCoefficient y 2))
  (doto
      (.makeConstraint solver 0.0 infinity) ; 3x - y >= 0
      (.setCoefficient x 3)
      (.setCoefficient y -1))
  (doto
      (.makeConstraint solver -infinity 2.0) ; x - y <= 2
      (.setCoefficient x 1)
      (.setCoefficient y -1))

  (.solve solver)

  (is (=== (.solutionValue x) 6))
  (is (=== (.solutionValue y) 4))
  (is (=== (.value (.objective solver)) 34)))
```

A Clojure library designed to work with or-tools from google.
https://github.com/google/or-tools#installation

Copyright © 2018 hellonico

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.