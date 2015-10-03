

(defn beam-search 
  "Search the highest scoring states first until goal is reached,
  but never consider more than beam-width states at a time."
  [start goal-p successors cost-fn beam-width]
  (tree-search (list start) goal-p successors
               (fn [coll]
                 (let [sorted (sort-by cost-fn > coll)]
                   (if (> beam-width (count sorted))
                     sorted
                     (subseq sorted 0 beam-width))))))

(defn tree-search
  "find a state that satisfies goal-p. Start with states, and
  search according to successors and combiner."
  [states goal-p successors combiner]
  (if (nil? states)
    nil
    (tree-search
     (combiner
      (successors (first states))
      (rest states))
     goal-p successors combiner)))

