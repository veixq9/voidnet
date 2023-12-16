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
            ajax.core
            )
  ;; (:import [force-graph$ForceGraph])

  #_(:require-macros [symautre.tools.core :as t])
  )


(enable-console-print!)


(defn load-data!
  [state]
  (let [posts-map (reduce (fn [a b] (assoc a (:id b) b)) {} posts)]

    ;; local storage
    ;; (symautre.local-storage/init!)
    (let [posts (sort-by :timestamp.unix > posts)]
      #_(doseq [p (merge posts (symautre.local-storage/get-local))]
          (symautre.local-storage/set-local! [(:id p)] p)))

    (swap! state merge posts-map (symautre.local-storage/get-local))
    #_(doseq [[id post] posts]
        (swap! state assoc-in [id] post))
    #_(ajax.core/GET "posts.edn" {
                                  ;; :content-type "text/edn"
                                  :handler #(swap! state assoc :posts.edn %)
                                  })
    ))

(defn
  ^:dev/after-load
  init []
  (println "initalizing")
  (def state (r/atom {}))
  (def event-log (r/atom nil))
  (swap! state assoc :system/event-chan (a/chan))
  (add-tap #(do (swap! event-log conj %) (% state)))


  (t/init-clock! state #(swap! state update :clock/counter inc) 0.01)

  
  (load-data! state)  

  (rd/render [body state]
             (js/document.getElementById "main-content")
             
             ))

(comment



  (cljs.reader/read-string (:posts.edn @state))
  
  (type (js->clj ))

  (-> (ajax.core/GET "posts.edn" {:content-type :edn}) .-body)

  (cljs.reader/read-string (pr-str (symautre.local-storage/get-local)))

  (keys @state)
  
  (-> (js/document.getElementById "file-input")
      (.-files)
      (aget 0)
      .text
      (.then #(do (println %)
                  (swap! state assoc :foo (cljs.reader/read-string %))
                  )))
  
  (-> (js/document.getElementById "file-input")
      (.-files)
      (aget 0)
      .text
      (.then #(do (println (cljs.reader/read-string %))
                  #_(swap! state assoc :bar %
                           :foo (cljs.reader/read-string %))
                  )))

  (cljs.reader/read-string (:foo @state))
  (cljs.reader/read-string (:foo @state))
  (cljs.tools.reader.edn/read-string (:foo @state))
  (cljs.tools.reader.edn/read-string (js->clj (:foo @state)))
  (cljs.reader/read-string (js->clj (:foo @state)))
  (cljs.reader/read-string (:bar @state))
  (cljs.reader/read-string (type (js->clj (:bar @state))))
  (cljs.tools.reader/read-string (js->clj (:bar @state)))

  (:bar @state)
  (:foo @state)
  
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





