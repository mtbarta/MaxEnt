(ns maxent.maxent
  (:require maxent.core maxent.sgd maxent.search :reload))

;;helpful gist:
;;https://gist.github.com/Integralist/6ba8b3effc03aa47ab93

(defn- check-weights
  "adds all outcomes as keys to an empty weight map"
  [weights]
  (if (empty? weights)
    {}
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

(defn sort-paths
  [& paths]
  (let [my-map (apply conj {} paths)]
  (into (sorted-map-by (fn [key1 key2] (compare (key2 my-map) (key1 my-map)))) my-map))
  )

(defn assemble-new-features
  "creates sentence-features for predict-this-word"
  [prev-preds word-index  sentence-seq]
  (if (empty? prev-preds)
    sentence-seq
    (map #(update-in sentence-seq [word-index] conj %1) prev-preds)))

(defn word-prediction-sequence
  "sentence-features are ((Confidence NN)(is JJ)..) sequences with each possible previous prediction already mapped."
  [weights transform word-index sentence-features]
  (let [feature-maps (map transform sentence-features)]
    (zipmap
     (map #(nth word-index %1) feature-maps)
     (map #(predict-probas weights %1)
          (map #(nth word-index %1) feature-maps))
  )))

(defprotocol Model
  "defines how a maxent model should be constructed."
  (train [this eta iter feats y]
    "train the model on a collection of data.")
  (predict [this size sentence]
    "predict the labels of a new data point using a beam search."))
;;volatile-mutable variables are stored in main memory, unsynchronized-mutable stored in thread-local.
(deftype Maxent [weights]
  Model
  (train
    [this eta iter feats y]
    (->>
    (let [pairs (partition 2 (interleave y feats))
          weight-map (check-weights weights)]
      (maxent.sgd/train-sgd weight-map eta iter pairs maxent.maxent/predict-probas))
     (assoc weights (merge weights))))

  (predict
    [this size sentence]
    (let [paths {}]
      ;;(maxent.search/beam-search )
      ;;(maxent.search/beam-search )
      ;;for previous predictions
      ;; create new hashmaps
      ;; get predictions on current word - map over hashmaps
      ;; rank
      ;; get highest
      ;; save predictions as previous
     )))




