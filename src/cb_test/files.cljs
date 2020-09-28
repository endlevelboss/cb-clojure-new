(ns cb-test.files
  (:require [cljs.core.async :refer [put! chan <! >!]]
            [cb-test.database :as db]
            [testdouble.cljs.csv :as csv])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def extract
  (map #(-> % .-target .-result)))

(def upload (chan 1))
(def file-read (chan 1 extract))

(defn file-import [e]
  (let [file (aget e 0)]
    (put! upload file)))

(defn cb-line [data]
  (zipmap [:name :matchname :chromosome :start :end :cm :matching-snps] data))

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

(defn update-state [data]
  (let [k (:name (first data))
        s @db/mystate]
    ;; (println k)
    (->> (assoc s k data)
         (reset! db/mystate))
    (reset! db/selected-user-a k)
    (reset! db/selecter-user-b k)))

(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload)]
    (set! (.-onload reader) #(put! file-read %))
    (.readAsText reader file)
    (recur)))

(go-loop []
  (update-state (cb-map (rest (csv/read-csv (<! file-read)))))
  (recur))


