(ns rum-sample.word-assoc.word-reader
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def game-state
  {:source-words-file "data/1k-words.txt"
   :word-count 25
   :p1-num-words 7
   :p2-num-words 6
   :state
   (atom
    {:game-words []
     :p1-words []
     :p2-words []
     :p1-guessed-words []
     :p2-guessed-words []
     :guesser-hint {}
     })})

(defn get-file-words [file-name]
  (-> file-name
      slurp
      str/split-lines))

(defn get-n-random-words [words count]
  (reduce
   (fn [acc itm]
     (conj acc (rand-nth (seq words))))
   #{} 
   (range count)))

(defn get-game-words []
  (-> game-state
      :source-words-file
      get-file-words
      (get-n-random-words (-> game-state :word-count))
      (into #{})))

(defn init-game []
  (let [state (:state game-state)
        game-words (get-game-words)
        p1-word-count (-> game-state :p1-num-words)
        p1-words (get-n-random-words game-words p1-word-count)
        remaining-game-words (set/difference game-words p1-words)
        p2-word-count (-> game-state :p2-num-words)
        p2-words (get-n-random-words remaining-game-words p2-word-count)]
    (reset!
     state
     (-> @state
         (assoc :game-words game-words)
         (assoc :p1-words p1-words)
         (assoc :p2-words p2-words)))))

(comment
  (init-game)

  )
