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
    (double (* (java.lang.Math/exp (* prediction actual)) (- 0 actual)))
    (if (< (* prediction actual) -18)
      (double (- 0 actual))
      (double (/ (- 0 actual) (+ (java.lang.Math/exp (* prediction actual)) 1))))))

(defn update-weights
  "return a map updated from a single vector."
  [eta predfunc weights coll]
  (let [y (first coll)
        feats (second coll)
        predictions  (predfunc weights feats)]
    ;;y is a string. to calculate the loss, we can compare y to the keys in weights.
     (reduce #(let [fxy (if (= (name (key %2)) y) 1 0)
                 update (* (- 0 eta) (dlog-loss fxy (val %2)))
                 feat-loc (partition 2 (interleave (repeat  (key %2)) feats))]
             (maxent.core/update-each %1 feat-loc (fnil + update)
                                      ))weights predictions)))
    
    ;;(reduce
    ;; #(
       ;;i need to update every feature vector for every output label, right? is my gradient wrong?
    ;;   (let [fxy (if ((fnil = ) (name (first %1)) y) 1 0)
    ;;         prediction (predfunc %1 %2)
    ;;         update (* (- 0 eta) (dlog-loss 1 prediction))]
    ;;    (update-in weights (seq [(keyword y) (second %2)]) (fnil + update)))) weights feats)))


(defn iter-sgd
  "iteration of sgd"
  [weights ^double eta coll predfunc]
   (reduce
    (partial update-weights eta predfunc)
    weights
    coll)
  )

(defn train-sgd
  "trains a maxent model using sgd. coll is a pair of (y feats)"
  [weights eta iter coll predfunc]
  
  (loop [i 0
         wt weights
         shuffle (if (= i 0) coll (shuffle coll))]
    (if (< i iter)
      (recur (inc i) (iter-sgd wt eta coll predfunc))
      wt
    )))
