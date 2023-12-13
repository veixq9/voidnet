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
            symautre.local-storage
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
  #_(let [posts (sort-by :timestamp.unix > posts)]
      (-> js/window.localStorage (.setItem :posts posts)))
  
  #_(let [posts (sort-by :timestamp.unix > posts)]
      (swap! state assoc-in [:doc-ids] (map :id posts))
      (doseq [post posts]
        (swap! state assoc-in [(:id post)] post)))

  #_(let [posts (sort-by :timestamp.unix > (cljs.reader/read-string (-> js/window.localStorage (.getItem :posts))))]
      (swap! state assoc-in [:doc-ids] (map :id posts))
      (doseq [post posts]
        (swap! state assoc-in [(:id post)] post)))

  (let [posts (sort-by (comp :timestamp.unix val) > (symautre.local-storage/get-local))]
    ;; (swap! state assoc-in [:doc-ids] (keys posts))
    (doseq [[id post] posts]
      (swap! state assoc-in [id] post)))

  

  (rd/render [body state]
             (js/document.getElementById "main-content")
             
             ))

(comment
  (symautre.local-storage/clear!)
  (doseq [post (sort-by :timestamp.unix > posts)]
    (symautre.local-storage/set-local! [(:id post)] post))
  
  (count (symautre.local-storage/get-local))
  
  (doseq [post (sort-by :timestamp.unix > (cljs.reader/read-string (-> js/window.localStorage (.getItem :posts))))]
    (-> js/window.localStorage (.setItem (:id post) post)))
  
  (symautre.local-storage/get :posts )
  
  (-> js/window.localStorage (.getItem :posts))

  (js/Directory )

  (println (js/console.dir js/window.localStorage.))

  (.log js/console js/window.localStorage)
  )





