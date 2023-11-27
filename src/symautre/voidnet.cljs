(ns symautre.voidnet
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [react]
            [react-dom]
            [clojure.core.async :as a]
            cljs.reader

            [symautre.tools.core :as t]
            [symautre.content-data :refer [posts]]
            ;; [ipfs-core]


            [taipei-404.html :refer [html->hiccup]]
            [force-graph :as fg :refer [ForceGraph]]
            [cljs-http.client :as http]
            ;; [react-force-graph]
            ;; [react-force-graph-2d :refer [ForceGraph2D]]
            [symautre.ui-body :refer [body]]
            )
  ;; (:import [force-graph$ForceGraph])
  #_(:require-macros [symautre.tools.core :as t])
  )

(enable-console-print!)
(defn
  ^:dev/after-load
  init []
  (println "initalizing")
  (def state (r/atom {}))
  (def event-log (r/atom nil))
  (swap! state assoc :system/event-chan (a/chan))
  (add-tap #(do (swap! event-log conj %) (% state)))


  (t/init-clock! state #(swap! state update :clock/counter inc) 0.01)
  (swap! state assoc :data posts)
  (rd/render [body state]
             (js/document.getElementById "main-content")
             
             ))





