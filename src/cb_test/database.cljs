(ns cb-test.database
  (:require [reagent.core :as r]))

(defonce mystate (r/atom {}))

(defonce selected-user-a (r/atom nil))

(defonce selecter-user-b (r/atom nil))

(defonce selected-chromosome (r/atom 1))

(def chromolength36 [247249719, 242951149, 199501827, 191273063, 180857866, 170899992, 158821424, 146274826, 140273252, 135374737, 134452384, 132349534, 114142980, 106368585, 100338915, 88827254, 78774742, 76117153, 63811651, 62435964, 46944323, 49691432, 154913754])

(defn chromo-scale [chromosome]
  (get chromolength36 (dec chromosome)))
