(ns symautre.voidnet
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [react]
            [react-dom]
            [clojure.core.async :as a]
            cljs.reader

            cljs.js
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
  (a/go (let [

              local-storage-data_ (symautre.local-storage/get-local)
              local-storage-data (if (= 'null local-storage-data_) nil local-storage-data_)

              pf (:body (a/<! (cljs-http.client/get "/posts/posts.edn" )))
              pf2 (:body (a/<! (cljs-http.client/get "/posts/posts2.edn" )))

              
              posts-map (reduce (fn [a b] (assoc a (:id b) b)) {} (concat posts pf pf2))
              ]
          (println pf)

          ;; (swap! state merge posts-map local-storage-data)
          (swap! state merge posts-map )
          (swap! state assoc :posts/pinned "df4cba34-6922-4aae-90ff-521f7886d3c9")
          (println "loading data done!")
          #_(doseq [[id post] posts]
              (swap! state assoc-in [id] post))
          #_(ajax.core/GET "posts.edn" {
                                        ;; :content-type "text/edn"
                                        :handler #(swap! state assoc :posts.edn %)
                                        })
          )))





(comment
  (:body (get @state "aed21d96-ddea-4352-ba33-a5f89298dcb7"))


  
  
  (cljs.js/eval
   (cljs.env/default-compiler-env)
   (quote (:body (get @state "aed21d96-ddea-4352-ba33-a5f89298dcb7")))


   {:eval cljs.js/js-eval
    ;; :load (partial boot/load compile-state-ref)
    }
   :value)
  
  (cljs.js/eval-str
   (cljs.env/default-compiler-env)
   ;; (pr-str (:body (get @state "aed21d96-ddea-4352-ba33-a5f89298dcb7")))
   (pr-str '(cljs.core/+ 1 2))
   "[test]"
   {:eval cljs.js/js-eval
    ;; :load (partial boot/load compile-state-ref)
    }
   print)

  
  
  (cljs.js/eval (cljs.env/default-compiler-env) (:body (get @state "aed21d96-ddea-4352-ba33-a5f89298dcb7")) println)

  (cljs.js/eval-str (cljs.env/default-compiler-env) (pr-str (:body (get @state "aed21d96-ddea-4352-ba33-a5f89298dcb7"))) println)
  (keys @state)
  (first @state)
  )

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
  (keys @state)
  (:selected @state)
  (:control @state)

  (get @state nil)
  (:slider @state)
  (http/get "")
  (a/go
    (reset! foo (a/<! (cljs-http.client/get "/posts/posts2.edn" ))))
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





