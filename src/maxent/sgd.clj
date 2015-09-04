(ns maxent.sgd
  (:require maxent.core))
;;; sgd training for maxent.

(defn log-loss
  "log loss!"
  [actual prediction]
  ;;need loss(P(c|w) / P(w), actual)
  (let [z (* actual prediction)]
  (if (> (* prediction actual) 18)
    (java.lang.Math/exp (- 0 z))
    (if (< (* prediction actual) -18)
      (- 0 z)
      (java.lang.Math/log (+ 1 (java.lang.Math/exp (- 0 z))))))))

(defn dlog-loss
  "derivative of the log loss."
  [^long actual ^double  prediction]
   (if (> (* prediction actual) 18)
    (* (java.lang.Math/exp (* prediction actual)) (- 0 actual))
    (if (< (* prediction actual) -18)
      (- 0 actual)
      (/ (- 0 actual) (+ (java.lang.Math/exp (* prediction actual)) 1)))))

(defn update-weights
  "return a map updated from a single vector."
  [eta weights v]
  (let [y (maxent.maxent/response-index (first (:pos0 v)))
        features (filter #(not= (first %) :pos0) v)
        prediction (maxent.maxent/predict-word weights features)
        update (* (- 0 eta) (dlog-loss y prediction))]
    (maxent.core/update-each weights (maxent.core/feat-names features) (fnil + update))))

(defn iter-sgd
  "iteration of sgd"
  [weights ^double eta coll]
  (reduce ;;(partial update-weights eta) weights coll)
   ;;#(let [y (maxent.maxent/response-index (:pos0 %2))
   ;;       features (filter :pos0 %2)]
   ;;   (->>
   ;;    (maxent.maxent/predict-word %1 %2)
   ;;    (dlog-loss y)
   ;;    (* (- 0 eta))
   ;;    (partial +)
   ;;    (update-in weights (maxent.core/feat-names features))
   ;;))
   (partial update-weights eta)
   weights
   coll)
  )

(defn train-sgd
  "trains a maxent model using sgd"
  [weights ^double eta ^long iter coll]
  (loop [i 0
         wt weights]
    (if (< i iter)
      (recur (inc i) (iter-sgd wt eta coll))
      wt
    )))
