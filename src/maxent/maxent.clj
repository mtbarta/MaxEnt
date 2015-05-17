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
  "take a a line from a filewords and make it a map."
  [lines]
  (into {} (apply map vector [:text :pos :chunk] lines))
  ;;(map #(apply
  ;;       (->>
  ;;        ;;(clojure.string/split % #" ")
  ;;        (map vector [:text :pos :chunk])
  ;;        (into {})
  ;;        )) lines)
  )
(defn to-map-seq
  [seq]
  (map to-map seq))
  

(defn window
  "create windows around the target word using a shingling pattern."
  [size filelines]
  (partition size 1 [] filelines))

;;(defn create-features
;;  "creates the features and tags from the datafile."
;;  [filename windowsize  & features]
;;  (->>
;;   (lazy-file-lines filename)
;;   (window windowsize)))
   ;; windows are seqs of seqs of lines from the file.
   ;;(map #(apply

(println (to-map (seq ['(I am groot)])))

(println(take 2 (window 3(to-map-seq(lazy-file-lines "/home/matt/Documents/Projects/clojure/summarizer/resources/train.txt")))))
