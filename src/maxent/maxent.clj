(ns maxent.maxent
  (:require [maxent.core :refer :all]))

(defn dot-product
  "return the dot product"
  [x y]
  (->>
   (interleave x y)
   (partition 2 2)
   (map #(apply * %))
   (reduce +)))

(defn inner-seq-filter
  [seq-in-seq]
  (let [reduced-map (map #(select-keys % [:pos :text]) seq-in-seq)]
    (vec (reduce concat (map #(vec (concat (vals %))) reduced-map)))))


(defn transform-data
  [window-size keys coll]
  (->>
   (tokenize coll)
   (vectors-to-maps keys)
   (trail window-size)))

(defn get-feat-by-key
  "from our windowed data, grab the key, return a map"
  [key coll]
  (let [words (reverse (flatten (map #(find-key key %) coll)))
        new-keys (map #(keyword (clojure.string/join "" %)) (partition 2 (interleave (repeat (name key)) (iterate inc 0))))]
  (zipmap new-keys words) ))

(defn interact-keys
  "multiply keys together. Result is combined on a separator"
  [coll sep & keys]
  (->>
   (find-key keys coll)
   (clojure.string/join sep)))

  
(defn assemble-features
  "create features from the transformed data"
  [coll]
  ;grab :pos and :text from each seq, combine :pos for another feature.
  (let [pos (map #(get-feat-by-key :pos %) coll)
        text (map #(get-feat-by-key :text %)coll)]
    (map #(into {} %) (partition 2 (interleave pos text)))))
  
(def test-sample (take 15 (lazy-file-lines "/home/matt/Documents/clojure/MaxEnt/resources/train.txt")))


(def features (transform-data 15 [:text :pos :chunk] test-sample))
(def history-keys (remove :pos0 (keys features)))
(println (probability [:pos0 :pos1] '("DT" "JJ") (assemble-features features)))

;(println (assemble-features features))
;(println (count (get  (group-by #(select-values % [:pos0 :pos1]) (assemble-features features)) '("DT" "JJ"))))
