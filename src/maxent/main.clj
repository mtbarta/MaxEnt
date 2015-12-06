(ns maxent.main
  (:require maxent.core maxent.maxent :reload))
  ;;(:import [maxent.maxent Maxent]))


(def test-sample (take 43 (maxent.core/lazy-file-lines "/home/matt/Documents/clojure/MaxEnt/resources/train.txt")))

(defn get-labels
  "get the POS tags for the data"
  [coll]
  (filter maxent.core/not-whitespace?
          (map #(nth
                 (maxent.core/tokenize-sent %1) 1) coll)))

(def transformed-input (map #(butlast
                 (maxent.core/tokenize-sent %1)) test-sample))

(def sents (maxent.core/make-sentences transformed-input))

(def trailed-data (maxent.core/transform-sentences 3 sents))
;;(println (last (first (first (take 1 trailed-data)))))
(println (take 1 trailed-data))

(def outcome-list ["CC" "CD" "DT" "EX" "FW" "IN" "JJ" "JJR" "JJS" "LS" "MD" "NN" "NNS" "NNP" "NNPS" "PDT" "POS" "PRP" "PRP$" "RB" "RBR" "RBS" "RP" "SYM" "TO" "UH" "VB" "VBD" "VBG" "VBN" "VBP" "VBZ" "WDT" "WP" "WP$" "WRB"])

;;(def outcome-list ["NN"])

;;(def model (maxent.maxent/Maxent. maxent.core/feat-names {}))
;;(maxent.maxent/train model 0.001 3 :pos0 outcome-list f)
;;(def model (maxent.maxent/train 0.001 2 :pos0 outcome-list f))
;;(println  model)



