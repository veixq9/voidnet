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
  (println "loading data")
  (let [posts-map (reduce (fn [a b] (assoc a (:id b) b)) {} posts)

        local-storage-data_ (symautre.local-storage/get-local)
        local-storage-data (if (= 'null local-storage-data_) nil local-storage-data_)]

    (swap! state merge posts-map local-storage-data)
    (swap! state assoc :posts/pinned "df4cba34-6922-4aae-90ff-521f7886d3c9")
    (println "loading data done!")
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
  (add-tap #(do (swap! event-log conj %)
                (cond (map? %)
                      ((:fn %) state)

                      :default
                      (% state))))


  (t/init-clock! state #(swap! state update :clock/counter inc) 0.01)

  
  (load-data! state)  

  (println "rendering view")
  (rd/render [body state]
             (js/document.getElementById "main-content")
             
             ))

(comment
  {:id "df4cba34-6922-4aae-90ff-521f7886d3c9", :title "P%litics", :content "", :author nil, :body ["1/2 the population believes 1/3 of the population experiences panic attacks because the other 1/2 does not exist"], :type :document}
  (:slider @state)
  (-> (js/navigator.clipboard.writeText "foo") (.then #(println %)))

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





