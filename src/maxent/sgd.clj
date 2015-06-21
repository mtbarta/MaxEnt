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

(defn log-loss
  "log loss!"
  [actual prediction]
  ;;need loss(P(c|w) / P(w), actual)
  (if (> (* prediction actual) 18)
    (java.lang.Math/exp -z)
    (if (< (*prediction actual) -18)
      (-z)
      (java.lang.Math/log (+ 1 (java.lang.Math/exp -z))))))

(defn dlog-loss
  "derivative of the log loss."
  [actual prediction]
   (if (> (* prediction actual) 18)
    (* (java.lang.Math/exp -(*prediction actual)) -actual)
    (if (< (*prediction actual) -18)
      (-actual)
      (/ -actual (+ (java.lang.Math/exp (* prediction actual)) 1)))))
