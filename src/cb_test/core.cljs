(ns cb-test.core
  (:require [cljs.core.async :refer [put! chan <! >!]]
            [reagent.core :as r]
            [reagent.dom :as rd]
            [testdouble.cljs.csv :as csv]
            [tonysort.core :as ts])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defonce mystate (r/atom 0))

(def extract
  (map #(-> % .-target .-result)))

(def upload (chan 1))
(def file-read (chan 1 extract))

(defn file-import [e]
  (let [file (aget e 0)]
    (put! upload file)))

(defn cb-line [data]
  (zipmap [:name :matchname :chromosome :start :end :cm :matching-snps] data))

(defn to-int [key m]
  (assoc m key (int (key m))))

(defn to-int-recur [m]
  (loop [keys [:chromosome :start :end :cm :matching-snps]
         res m]
    (if-let [k (first keys)]
      (recur (rest keys) (assoc res k (int (k res))))
      res)))

(defn to-int-map [m]
  (-> (cb-line m)
      (to-int-recur)))

(defn cb-map [data]
  (map to-int-map data))

(defn sort-chromosome [chromosome size data]
  (let [selection (->> (filter #(= (:chromosome %) chromosome) data)
                      (filter #(>= (:cm %) size)))]
    (ts/sort-and-stack :start :end selection)))

(defn printer [data]
  (if (= 0 data)
    (str "No data!")
    (str (sort-chromosome 10 15 data))))

(defn mydiv []
  (let [s @mystate]
    [:div "Import file"
     [:input {:id "file-input" :type "file"
              :on-change #(file-import (-> % .-target .-files))}]
     [:div (printer s)]]))

(defn mounter []
  (rd/render [mydiv]
             (js/document.getElementById "root")))

(mounter)

(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload)]
    (set! (.-onload reader) #(put! file-read %))
    (.readAsText reader file)
    (recur)))

(go-loop []
  (reset! mystate (cb-map (rest (csv/read-csv (<! file-read)))))
  (recur))

;; (ts/sort-and-stack :a :b [{:a 2 :b 3} {:a 1 :b 4}])


