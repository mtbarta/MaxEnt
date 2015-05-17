
(ns summarizer.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn tokenize
  "tokenize a sentence"
  [a]
  (str/split a #" "))
;; (frequencies( tokenize "hello world"))


