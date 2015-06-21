(in-ns 'maxent.core)
;;; sgd training for maxent.


(defn train-sgd
  "trains a maxent model using sgd"
  [coll]
  )

(defn iter-sgd
  "iteration of sgd"
  [coll]
  (let [observed]))

(defn exp
  "exponentation function for e^n"
  [n]
  (reduce * (repeat n 2.71828)))

(defn log-loss
  "log loss!"
  [prediction actual]
  ;;need loss(P(c|w) / P(w), actual)
  (if (> (* prediction actual) 18)
    (exp -z)
    (if (< (*prediction actual) -18)
      (-z)
      (java.lang.Math/log (+ 1 (exp -z))))))

(defn dlog-loss
  "derivative of the log loss."
  [prediction actual]
   (if (> (* prediction actual) 18)
    (* (exp -(*prediction actual)) -actual)
    (if (< (*prediction actual) -18)
      (-actual)
      (/ -actual (+ (exp (* prediction actual)) 1)))))
