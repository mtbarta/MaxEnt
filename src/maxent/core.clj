(ns maxent.core
  (:require [clojure.string :as str])
  (:gen-class :main true))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn tokenize-string
  "tokenize a string"
  [criteria string]
  (clojure.string/split string (re-pattern criteria)))
  
(defn tokenize
  "tokenize a collection of strings"
  ([criteria coll]
   (map #(tokenize-string criteria %) coll))
  ([coll]
   (map #(tokenize-string #" " %) coll)))

(defn doc-freq
  "get the document frequency of a string input"
  [string]
  (frequencies string))

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

(defn lazy-file-lines
  "open a file and make it a lazy sequence."
  [filename]
  (letfn [(helper [rdr]
            (lazy-seq
             (if-let [line (.readLine rdr)]
               (cons line (helper rdr))
               (do (.close rdr) nil))))]
    (helper (clojure.java.io/reader filename))))

(def select-values
  (comp vals select-keys))

(defn find-key
  "select-keys wrapper"
  [key coll]
  (let [i (if (= vector (type key)) key [key])]
  (select-values coll i)))

(defn probability
  "return the marginal probability from a list of maps based on a key"
  ([key value map-coll]
   (let [newmap (group-by #(select-values % key) map-coll)]

     (/ (count(get newmap value))
        (reduce +
                (map count (vals newmap))))
     )
   ))

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
