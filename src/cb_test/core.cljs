(ns cb-test.core
  (:require [reagent.dom :as rd]
            [cb-test.gui :as gui]))


(defn mounter []
  (rd/render [gui/mydiv]
             (js/document.getElementById "root")))

(mounter)

