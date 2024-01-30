(ns symautre.voidnet
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [react]
            [react-dom]
            [clojure.core.async :as a]
            cljs.reader

            cljs.js
            [symautre.tools.core :as t]
            ;; [ipfs-core]


            [taipei-404.html :refer [html->hiccup]]
            [force-graph :as fg :refer [ForceGraph]]
            [cljs-http.client :as http]
            ;; [react-force-graph]
            ;; [react-force-graph-2d :refer [ForceGraph2D]]
            [symautre.ui-body :refer [body]]
            symautre.local-storage
            ajax.core

            [symautre.doc :as doc]
            symautre.voidnet.posts)
  ;; (:import [force-graph$ForceGraph])

  #_(:require-macros [symautre.tools.core :as t])
  )

(enable-console-print!)

(defn load-data!
  [state]
  (println "loading data")
  (a/go (let [local-storage-data_ (symautre.local-storage/get-local)
              local-storage-data (if (= 'null local-storage-data_) nil local-storage-data_)

              posts (let [res (a/<! (cljs-http.client/get "/posts/posts.edn"))]
                      (println res)
                      (if (:success res)
                        (:body res)
                        []))
              posts2 (let [res (a/<! (cljs-http.client/get "/posts/posts2.edn"))]
                       (println res)
                       (if (:success res)
                         (:body res)
                         []))

              
              posts-github (let [res (a/<! (cljs-http.client/get "/voidnet/resources/public/voidnet/posts/posts.edn"))]
                             (if (:success res)
                               (cljs.reader/read-string (:body res))
                               []))

              posts2-github (let [res (a/<! (cljs-http.client/get "/voidnet/resources/public/voidnet/posts/posts2.edn"))]
                              (if (:success res)
                                (cljs.reader/read-string (:body res))
                                []))

              posts3 symautre.voidnet.posts/data

              all-posts (concat []
                                posts-github posts2-github
                                posts
                                posts2
                                posts3
                                )
              _ (println all-posts)
              _ (println (type posts-github))
              _ (println (type posts2-github))
              posts-map (reduce (fn [a b] (assoc a (:id b) b)) {} all-posts)]
          

          ;; (swap! state merge posts-map local-storage-data)
          (swap! state assoc :raw-data all-posts)
          (swap! state assoc :docs posts-map)
          (swap! state assoc :posts/pinned "df4cba34-6922-4aae-90ff-521f7886d3c9") ;; rename to docs/pinned
          (println "loading data done!")
          #_(doseq [[id post] posts]
              (swap! state assoc-in [id] post))
          #_(ajax.core/GET "posts.edn" {
                                        ;; :content-type "text/edn"
                                        :handler #(swap! state assoc :posts.edn %)
                                        })

          (let [res (a/<! (cljs-http.client/get "https://vcq88ts.github.io/voidnet/resources/public/voidnet/index.html"))]
            (println "loading " "https://vcq88ts.github.io/voidnet/resources/public/voidnet/index.html")
            (println res)
            (if (:success res)
              (do (println "foreign call successful") (:body res))
              []))
          )))

(comment

  (:docs @state)
  (a/go (println (a/<! (cljs-http.client/get "/voidnet/resources/public/voidnet/posts/posts2.edn" ))))
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


  ;; (t/init-clock! state #(swap! state update :clock/counter inc) 0.01)


  (load-data! state)

  (do (println "rendering view")
      (rd/render [body state]
                 (js/document.getElementById "main-content")))

  #_(let [pinned-id @(r/cursor state [:posts/pinned])
          get-doc-ids (fn [] (println "yo") (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state))))))]
      @(r/track!  get-doc-ids))

  )

(comment

  (a/go (let [res (a/<! (cljs-http.client/get "/posts/posts2.edn"))]
          (println res)
          (if (:success res)
            (println (:body res))
            [])))  

  (let [x (doc/doc)]
    (swap! state assoc-in [:docs (:id x)] x))

  (:foo @state)
  
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





