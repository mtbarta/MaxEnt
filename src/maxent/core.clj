(ns maxent.core
  (:require [clojure.string :as str]))

(defn dot-product
  "return the dot product"
  [x y]
  (->>
   (interleave x y)
   (partition 2 2)
   (map #(apply * %))
   (reduce +)))

(def select-values
  (comp vals select-keys))

(defn inner-seq-filter
  [seq-in-seq]
  (let [reduced-map (map #(select-keys % [:pos :text]) seq-in-seq)]
    (vec (reduce concat (map #(vec (concat (vals %))) reduced-map)))))

(defn find-key
  "select-keys wrapper"
  [key coll]
  (let [i (if (= vector (type key)) key [key])]
  (select-values coll i)))

(defn get-feat-by-key
  "from our windowed data, grab the key, return a map"
  [key coll]
  (let [words (reverse (flatten (map #(find-key key %) coll)))
        new-keys (map #(keyword (clojure.string/join "" %)) (partition 2 (interleave (repeat (name key)) (iterate inc 0))))]
    (zipmap new-keys words) ))

(defn filter-key
  "filter a key out of a sequence of maps"
  [key coll]
  (into {}
        (filter (fn [[k v]]
                  (not= key k)) coll)))

(defn assemble-features
  "create features from the transformed data"
  [coll]
  ;grab :pos and :text from each seq, combine :pos for another feature.
  (let [pos (map #(get-feat-by-key :pos %) coll)
        text (map #(get-feat-by-key :text %)coll)]
    (map #(into {} %) (partition 2 (interleave pos text)))))

(defn interact-keys
  "multiply keys together. Result is combined on a separator"
  [coll sep & keys]
  (->>
   (find-key keys coll)
   (clojure.string/join sep))) 

(defn to-map
  "take a line from a file and make it a map."
  [lines]
  (map
  #(zipmap [:text :pos :chunk] (clojure.string/split (apply str %) #" "))lines)
  )

(defn vectors-to-maps
  "zipmap wrapper"
  [keys values]
  (map
   #(zipmap keys %) values))

(defn remove-nil-keys
  "remove keys from a map where the value is nil."
  [map]
  (into {} (filter (comp not nil? val) map )))

(defn feat-names
  "join the location and word to create a feature name"
  [coll]
  (let [strings (map
                 #(str
                   (name (first %1)) "-" (second %1)) coll)]
    (map keyword strings)))

(defn update-vals [mp vals f]
  (map #(update-in % [%2] f) mp vals))

(defn update-each
  "Updates each keyword listed in ks on associative structure m using fn."
  [m ks fn]
  (reduce #(update-in %1 %2 fn) m ks))

(defn sum-counts
  "get the sum of counts over maps"
  [coll]
  (->>
   (partition 2 coll)
   (reduce #(merge-with + %))))

(defn trail
  "create a sliding window over the data where the word of interest is the last element."
  [size coll]
  (let [data (lazy-cat (repeat (- size 1) nil) coll)]
    (partition size 1 data)))

(defn zero-length?
  [x]
  (= 0 (count x)))

(defn has-length?
  [x]
  (not (zero-length? x)))

(defn whitespace?
  [x]
  (every? clojure.string/blank? x))

(defn not-whitespace?
  [x]
  (not (every? clojure.string/blank? x)))

(defn make-sentences
  "from a file with a word per line, create sentences. Uses partition-by twice to get rid of empty sequences from the first partition-by."
  [coll]
  (->>
   (partition-by zero-length? coll)
   ;;(filter #(= 1 (count %)))
   (filter not-whitespace?)))

(defn lazy-file-lines
  "open a file and make it a lazy sequence."
  [filename]
  (letfn [(helper [rdr]
            (lazy-seq
             (if-let [line (.readLine rdr)]
               (cons line (helper rdr))
               (do (.close rdr) nil))))]
    (helper (clojure.java.io/reader filename))))

(defn probability
  "return the marginal probability from a list of maps based on a key"
  ([key value map-coll]
   (let [newmap (group-by #(select-values % key) map-coll)]

     (/ (count(get newmap value))
        (reduce +
                (map count (vals newmap))))
     )
   ))

(defn transform-data
  [window-size keys coll]
  (->>
   (tokenize coll)
   (vectors-to-maps keys)
   (trail window-size)))

(defn cond-probability
  "return the marginal probability from a list of maps based on a key. P(A,B)/P(B)"
  ([key value key2 val2  map-coll]
   (let [newmap (group-by #(select-values % key2) map-coll)]
     
     (/ (count (get (group-by newmap key) value)) (count(get newmap val2)))
     )
   ))

(defn choices?
  "find the number of choices associated with a key"
  [key coll]
  (distinct (flatten (map (partial find-key key) coll))))

(defn count-features
  "count the number of total features"
  [kys coll]
  (map #(choices? % coll) kys))
