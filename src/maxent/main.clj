(ns maxent.main
  (:import [maxent.maxent Maxent]))


(def test-sample (take 2 (maxent.core/lazy-file-lines "/home/matt/Documents/clojure/MaxEnt/resources/train.txt")))

(println test-sample)
;;test data assembly
(def feature-transforms (maxent.core/transform-data 4 [:text :pos :chunk] test-sample))
(println feature-transforms)
;;the keys associated with features
;;(def features-labels  (remove #(= % :pos0) (keys (first (assemble-features feature-transforms)))))

;;the actual features
(def f (maxent.core/assemble-features feature-transforms))
(println f)

;;(def outcome-list ["CC" "CD" "DT" "EX" "FW" "IN" "JJ" "JJR" "JJS" "LS" "MD" "NN" "NNS" "NNP" "NNPS" "PDT" "POS" "PRP" "PRP$" "RB" "RBR" "RBS" "RP" "SYM" "TO" "UH" "VB" "VBD" "VBG" "VBN" "VBP" "VBZ" "WDT" "WP" "WP$" "WRB"])

(def outcome-list ["NN"])

(def model (maxent.maxent/Maxent. maxent.core/feat-names {}))
(maxent.maxent/train model 0.001 1 :pos0 outcome-list f)
;;(def model (maxent.maxent/train 0.001 2 :pos0 outcome-list f))
;;(println  model)



