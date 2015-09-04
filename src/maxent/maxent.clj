(ns maxent.maxent
  (:require maxent.core maxent.sgd))

(def responses [])

(defn predict
  "tag an entire sentence."
  [sentence]
 )

(defn response-index
  "hash out the response variable. 1,2,3..."
  [response]
  (when-not (contains? responses response)
    (conj responses response))
  (.indexOf responses response))

(defn predict-word
   "predict the tag for a word based on current weights."
  [weights features]
  (let [feats (maxent.core/feat-names features)]
  (reduce + (maxent.core/select-values weights feats)))
  )

(defn train
  "train the model"
  ([^double eta ^long iter coll]
  (maxent.sgd/train-sgd {} eta iter coll))
  ([^double eta ^long iter coll weights]
  (maxent.sgd/train-sgd weights eta iter coll))
  )
