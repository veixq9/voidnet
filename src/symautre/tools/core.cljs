(ns symautre.tools.core
  (:refer-clojure :exclude [uuid])
  (:require ["uuid" :as uuidjs]
            [reagent.core :as r]
            #_[cljs.core :excluding [uuid]]))

(defn uuid
  []
  ((-> uuidjs .-v4)))

[:cell
 (defmacro cell
   ([body]
    `(quote (cell ~body)))
   ([body & more]
    `(quote (cell ~body ~@more))))

 (defmacro uncell
   [body]
   (cons 'do (rest body)))]

(comment
  (cell (+ 1 2))
  )

[:combinators
 (defn >>>
   [& fs]
   (fn [x]
     (loop [f fs
            a x]
       (if (empty? fs)
         (recur (rest fs) (f x)))))
   
   (apply comp (reverse fs)))

 (defn -->
   ([x]
    x)
   ([x & body]
    ((apply >>> body) x)))

 (do
   (assert (= (--> 3) 3))
   (assert (= (--> 2 inc) 3))
   (assert (= (--> 4 inc inc (partial * 2) str) "12")))]

[:code-string
 (defmacro clojure
   [code]
   (let [sym (gensym)]
     `(defn ~sym [fn#] (fn# (pr-str ~code)))))


 ]

[:time
 (defn timestamp-unix
   []
   (js/Date.now))

 (defn timestamp
   "for unix use 'timestamp-unix'"
   []
   (new js/Date))

 (defn init-clock!
   [state f interval-in-secs]
   (println "starting clock")
   (swap! state assoc :clock/interval (js/setInterval #(tap> f) (* 1e3 interval-in-secs))))
 ]

[:misc
 (defn timeout []
   (let [seconds-elapsed (r/atom 0)]
     (fn []
       (js/setTimeout #(swap! seconds-elapsed inc) 1000)
       [:div
        "Seconds Elapsed: " @seconds-elapsed])))]

[:edn->hiccup

 (defn edn->hiccup
   [edn]
   [:div.w3-container
    (cond
      (map? edn)
      [:div
       [:span.edn-brackets  "{"]
       (into [:div {:key (uuid)}]
             (for [[k v] edn]
               [:div.w3-container.w3-row {:key (uuid)}
                [:div.w3-cell {:min-width "10%" :clear :right :color "lightgreen" }  (edn->hiccup k)]
                [:div.w3-cell {:style {:color "lightgrey"} } (edn->hiccup v)]]))
       [:span.edn-brackets  "}"]
       ]

      ;; (list? edn)
      ;; [:pre (pr-str edn)]
      #_[:pre (with-out-str (cljs.pprint/pprint edn))]
      
      (sequential? edn)
      [:div.w3-row
       (for [x edn]
         [:div.w3-container.w3-cell-col {:key (uuid)} (edn->hiccup x)])]

      (instance? cljs.core/Keyword edn)
      [:p.w3-left.edn-kw (pr-str edn) #_(clojure.string/replace (pr-str edn) #_(name edn) "-" " ")]

      (and (string? edn) (some? (re-matches #"http.*" edn)))
      [:a {:href edn} [:p edn]]

      (string? edn)
      [:p.w3-left (pr-str edn) ]

      (cljs.core/boolean? edn)
      [:p (str edn)]

      (nil? edn)
      [:p "nil"]

      :default
      [:p.w3-left (pr-str edn)])]
   )


 (defn collapsible
   [k v]
   (fn [k v]
     (r/with-let [open? (r/atom true)]
       [:div.w3-container.w3-row {:key (uuid)}
        [:div.w3-cell:active {;;:class (if @open? "w3-button")
                              :on-click #(swap! open? not)}
         #_[:span {:class (if @open? "w3-border"  ;;  "↓" "→"
                              ) } ]
         [:span.edn-brackets k]]

        [:div.w3-cell {:style {:display (if @open? "block" "none")}}
         (if (map? v)
           (into [:div]
                 (for [[k' v'] v]
                   [collapsible k' v']))
           [:div (pr-str v)])]])))
 ]


(comment
  
  
  [:node-tree]
  (defn node-tree
    [edn]
    (cond
      (map? edn)
      {:root (reduce (fn [acc [k v]] (assoc acc k v))
                     {}
                     (for [[k v] edn]
                       [k (node-tree v)]))}

      (vector? edn)
      nil

      :default
      edn
      ))

  (node-tree {:foo "bar" :num [1 2 3]
              :deep {:y 0 :z {:poo "good"
                              :pepe "meme"
                              :nice {:kill 1000}}
                     :w "shawarma"
                     }}))

