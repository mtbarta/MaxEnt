(ns maxent.maxent
  (:require maxent.core maxent.sgd))

;;helpful gist:
;;https://gist.github.com/Integralist/6ba8b3effc03aa47ab93

(defn- check-weights
  "adds all outcomes as keys to an empty weight map"
  [outcome-list weights]
  (if (empty? weights)
    (zipmap (map keyword outcome-list) (repeat {}))
    weights))



(defn- logistic-func
  "the logistic function to get back a probability.
  we take in the value of the coefficients as a double. "
  [coefs]
  (let [exp (java.lang.Math/exp coefs)]
    (/ exp (+ 1 exp))))

  (defn- predict-probas
  "predict the scores across all results"
  [weights features]
  (let [coefs (map
               #(reduce
                 + (maxent.core/select-values
                    (second %) features)) weights)]
    (zipmap (keys weights) (map logistic-func coefs))))

(defn- predict-word
  "predict the tag for a word based on current weights."
  [weights features]
  (let [scores (predict-probas weights features)]
    (max-key val scores)))

(defn- sort-map-by-value
  [map]
  (into (sorted-map-by (fn [key1 key2] (compare (key2 map) (key1 map)))) map))

(defn- NLL-score
  "calculate the NLL to score predictions in the beam search."
  [& scores]
  (reduce +
          (map #(- 0
                   (java.lang.Math/log %1)) scores)))

(defprotocol Model
  "defines how a maxent model should be constructed."
  (train [this eta iter response-key outcome-list coll]
    "train the model on a collection of data.")
  (predict [this size sentence]
    "predict the labels of a new data point using a beam search."))
;;volatile-mutable variables are stored in main memory, unsynchronized-mutable stored in thread-local.
(deftype Maxent [transform  weights]
  Model
  (train
    [this eta iter response-key outcome-list coll]
    (->>
    (let [y  (map response-key coll)
          features (map #(maxent.core/filter-key response-key %) coll)
          feats (map #(maxent.core/feat-names %) features)
          pairs (partition 2 (interleave y feats))
          weight-map (check-weights outcome-list weights)]
      (maxent.sgd/train-sgd weight-map eta iter pairs maxent.maxent/predict-probas))
     (assoc weights (merge weights))))

  (predict
    [this size sentence]
    (let [feats-by-word (transform sentence)]
      (predict-probas weights)
      (sort-map-by-value)
      (take size)
      (keys))))
