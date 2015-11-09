(ns maxent.main
  (:require maxent.core maxent.maxent :reload))
  ;;(:import [maxent.maxent Maxent]))


(def test-sample (take 10 (maxent.core/lazy-file-lines "/home/matt/Documents/clojure/MaxEnt/resources/train.txt")))

;;(println (count (nth test-sample 37)))
(def temp (map #(maxent.core/drop-lastv
                 (maxent.core/tokenize %1)) test-sample))
(println (take 1 temp))
;;(def sents (maxent.core/make-sentences temp))
;;(print (take 1 sents))
;;(println (maxent.core/transform-sentences 2 (take 1 sents)))


;;the actual features
;;(def f (maxent.core/assemble-features feature-transforms))
;;(println f)

(def outcome-list ["CC" "CD" "DT" "EX" "FW" "IN" "JJ" "JJR" "JJS" "LS" "MD" "NN" "NNS" "NNP" "NNPS" "PDT" "POS" "PRP" "PRP$" "RB" "RBR" "RBS" "RP" "SYM" "TO" "UH" "VB" "VBD" "VBG" "VBN" "VBP" "VBZ" "WDT" "WP" "WP$" "WRB"])

;;(def outcome-list ["NN"])

;;(def model (maxent.maxent/Maxent. maxent.core/feat-names {}))
;;(maxent.maxent/train model 0.001 3 :pos0 outcome-list f)
;;(def model (maxent.maxent/train 0.001 2 :pos0 outcome-list f))
;;(println  model)



