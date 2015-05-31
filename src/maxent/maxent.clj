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
   (println (interleave new-keys words))))

(defn interact-keys
  "multiply keys together. Result is combined on a separator"
  [coll sep & keys]
  (->>
   (find-key keys coll)
   (clojure.string/join sep)))

  
(defn assemble-features
  "create features from the transformed data"
  [& features]
  ;grab :pos and :text from each seq, combine :pos for another feature.
  )
  
(def test-sample (take 2 (lazy-file-lines "/home/matt/Documents/Projects/clojure/MaxEnt/resources/train.txt")))

;;(println (trail 3 (vectors-to-maps [:text :pos :chunk] (tokenize test-sample))))
;;(println (transform-data 3 [:text :pos :chunk] test-sample))
(def features (transform-data 3 [:text :pos :chunk] test-sample))
(map #(get-feat-by-key :pos %) features)

