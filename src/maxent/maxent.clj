(ns maxent.maxent
  (require [maxent.core :refer :all]))

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

(defn create-features
  "creates the features and tags from the datafile."
  [filename windowsize  & features]
  (let [data (->>
              (lazy-file-lines filename)
              (to-map)
              (trail windowsize))]
    (map inner-seq-filter data))
  ;;condense each sequence
)

(defn transform-data
  [window-size keys coll]
  (->>
   (tokenize coll)
   (vectors-to-maps keys)
   (trail window-size)))
   

  
(def test-sample (take 2 (lazy-file-lines "/home/matt/Documents/Projects/clojure/MaxEnt/resources/train.txt")))

;;(println (trail 3 (vectors-to-maps [:text :pos :chunk] (tokenize test-sample))))
(println (transform-data 3 [:text :pos :chunk] test-sample))
