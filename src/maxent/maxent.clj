(defn dot-product
  "return the dot product"
  [x y]
  (->>
   (interleave x y)
   (partition 2 2)
   (map #(apply * %))
   (reduce +)))

(defn lazy-file-lines
  "open a file and make it a lazy sequence."
  [filename]
  (letfn [(helper [rdr]
            (lazy-seq
             (if-let [line (.readLine rdr)]
               (cons line (helper rdr))
               (do (.close rdr) nil))))]
    (helper (clojure.java.io/reader filename))))

(defn to-map
  "take a a line from a file and make it a map."
  [lines]
  (map
  #(zipmap [:text :pos :chunk] (clojure.string/split (apply str %) #" "))lines)
  )  

(defn window
  "create windows around the target word using a shingling pattern."
  [size filelines]
  (partition size 1 [] filelines))

;;still doesn't work
(defn create-features
  "creates the features and tags from the datafile."
  [filename windowsize  & features]
  (let [data (->>
        (lazy-file-lines filename)
        (window windowsize))]
  #(map vec
       (apply concat
              (vals select-keys % [:pos :text])) data))
  ;;condense each sequence
)

   ;; windows are seqs of seqs of lines from the file.
;;(map #(apply

;;(println (zipmap [:one :two :three] (clojure.string/split (apply str(take 1(lazy-file-lines "/home/matt/Documents/Projects/clojure/summarizer/resources/train.txt")))#" ")))

;;(println (to-map(lazy-file-lines "/home/matt/Documents/Projects/clojure/summarizer/resources/train.txt")))
;;(use 'clojure.walk)
;;(println (take 2 (window 3(to-map(lazy-file-lines "/home/matt/Documents/Projects/clojure/MaxEnt/resources/train.txt")))))
(defn inner-seq-filter
  [seq-in-seq]
  (println seq-in-seq)
  (let [reduced-map (map #(select-keys % [:pos :text]) seq-in-seq)]
    (vec (reduce concat (map #(vec (concat (vals %))) reduced-map)))))
  
(def test (take 2 (window 3 (to-map(lazy-file-lines "/home/matt/Documents/Projects/clojure/MaxEnt/resources/train.txt")))))
;;(map #(apply println %) test)
(map inner-seq-filter test)
;;(create-features "/home/matt/Documents/Projects/clojure/MaxEnt/resources/train.txt" 2)

