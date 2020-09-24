(ns cb-test.gui
  (:require [tonysort.core :as ts]
            [cb-test.files :as f]
            [cb-test.database :as db]))

(def top-buffer 90)
(def left-buffer 20)
(def left-scale 2400)
(def box-height 20)
(def box-buffer 3)
(def row-height (+ box-height box-buffer))

(defn sort-chromosome [chromosome size data]
  (let [selection (->> (filter #(= (:chromosome %) chromosome) data)
                      (filter #(>= (:cm %) size)))]
    (ts/sort-and-stack :start :end selection)))

(defn chromo-box [data chromosome]
  (let [scale (db/chromo-scale chromosome)
        top (+ (* (:adjust data) row-height) top-buffer)
        left (+ (* (/ (:start data) scale) left-scale) left-buffer)
        width (* (/ (- (:end data) (:start data)) scale) left-scale)
        height box-height]
    [:div {:style {:background-color "lightgreen"
                   :border-style "solid"
                   :border-color "black"
                   :border-width "1px"
                   :position "absolute" :overflow "hidden"
                   :top top :left left :width width :height height}}
     ;; [:span {:class "tooltiptext"} "tooltiptext"]
     (:matchname data)]))

(defn chromo-boxes [chromosome size data]
  (let [sorted (sort-chromosome chromosome size data)]
    [:div
     (for [s sorted]
       ^{:key (str (:matchname s) (:start s))}[chromo-box s chromosome])]))

(defn printer [data]
  (let [chromo @db/selected-chromosome]
    [:div
     (if (= 0 data)
       (str "No data!")
       (chromo-boxes chromo 5 data))]))

(defn myselector []
  [:select {:name "chromosomes" :id "chromosomes"
            :on-change #(reset! db/selected-chromosome (int (-> % .-target .-value)))}
   [:option {:value 1} "1"]
   [:option {:value 2} "2"]
   [:option {:value 3} "3"]
   [:option {:value 4} "4"]
   [:option {:value 5} "5"]
   [:option {:value 6} "6"]
   [:option {:value 7} "7"]
   [:option {:value 8} "8"]
   [:option {:value 9} "9"]
   [:option {:value 10} "10"]
   [:option {:value 11} "11"]
   [:option {:value 12} "12"]
   [:option {:value 13} "13"]
   [:option {:value 14} "14"]
   [:option {:value 15} "15"]
   [:option {:value 16} "16"]
   [:option {:value 17} "17"]
   [:option {:value 18} "18"]
   [:option {:value 19} "19"]
   [:option {:value 20} "20"]
   [:option {:value 21} "21"]
   [:option {:value 22} "22"]
   ;; [:option {:value 23} "X"]
   ])

(defn mydiv []
  (let [s @db/mystate]
    [:div "Import file"
     [:input {:id "file-input" :type "file"
              :on-change #(f/file-import (-> % .-target .-files))}]
     [myselector]
     [printer s]]))
