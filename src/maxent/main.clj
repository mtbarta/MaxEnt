
(def test-sample (take 2 (lazy-file-lines "/home/matt/Documents/clojure/MaxEnt/resources/train.txt")))

;;test data assembly
(def feature-transforms (transform-data 3 [:text :pos :chunk] test-sample))

;;the keys associated with features
;;(def features-labels  (remove #(= % :pos0) (keys (first (assemble-features feature-transforms)))))

;;the actual features
(def f (assemble-features feature-transforms))
;;(println (feat-names (first f)))
;;(let [k (keys (first f))
;;      v (vals (first f))]
;;  (map #(apply str (first %1) (second %1))(->>
;;                  (interleave k v)
;;                  (partition 2))))
;;(println (first f))

;;(println (count-features history-keys f))

;;(println f)

;; weights are a map of lists, one key for each outcome.
;;(def weights {})

;;return long 0. -- this is the actual.
;;(println (type (maxent.maxent/predict (filter :pos0 (take 1 f)))))

;;weights somewhere along the line has become a lazyseq
(def model (maxent.maxent/train 0.001 20 f))
(println  model)
