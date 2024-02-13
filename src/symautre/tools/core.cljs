;;; rename to index?! or what?
(ns symautre.tools.core
  (:refer-clojure :exclude [uuid subs])
  (:require ["uuid" :as uuidjs]
            [reagent.core :as r]
            #_[cljs.core :excluding [uuid]]))



(defn uuid
  []
  ((-> uuidjs .-v4)))

(def $ partial)

[:cell
 ;; does not work in cljs
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
   ([]
    (-> (new js/Date ) .toISOString))
   ([unix-timestamp]
    (-> (new js/Date unix-timestamp) .toISOString)))

 (def now timestamp)

 

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


;; ================= inspection ========================================
(defn dir
  [x]
  (js/console.dir x))

(defn str->json
  [string_]
  (-> js/JSON (.parse string_)))

(defn json->str
  [json_]
  (-> js/JSON (.stringify json_)))

;; ================= encode decode crypto ========================================
(defn encode
  "String -> Uint8Array"
  [s]
  (-> (new js/TextEncoder) (.encode  s)))

(defn decode
  "Uint8Array -> String"
  [uintarray]
  (-> (new js/TextDecoder) (.decode uintarray)))



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
                     :w "far"
                     }}))


;; ================= comfy & readable ========================================
(def ++ inc)
(def -- dec)

;; ================= string ========================================
(defn remove-white-space
  [string_]
  (clojure.string/replace string_ #"[\ \t\n]" ""))

;; ================= range stuff: inclusive range,  ======================================
(def rangev
  (>>> range vec))

(assert (= (rangev 2 10 3) (into [] (range 2 9 3)) [2 5 8]))

(defn range+
  "end inclusive range"
  ([]
   (range))
  ([end]
   (range (inc end)))
  ([start end]
   (range start (inc end)))
  ([start end step]
   (range start (inc end) step)))

(assert (and
         (= (take 10 (range+)) (take 10 (range)))
         (= (range+ 0) '(0))
         (= (range+ 1) '(0 1))
         (= (range+ 3) '(0 1 2 3))
         (= (range+ 2 3) '(2 3))
         (= (range+ 99 99) '(99))
         (= (range+ 99 99) (range 99 100))
         (= (range+ 1000 1006 5) (range 1000 1006 5))
         (= (range+ 1000 1006 2) (concat (range 1000 1006 2) '(1006)))))

(def rangev+
  (>>> range+ vec))

(assert (= (rangev+ 2 10 2) (conj (into [] (range 2 10 2)) 10) [2 4 6 8 10]))


(defn subs
  ([s start_]
   (subs s start_ -1))
  ([s start_ end_]
   (let [length (count s)
         convert-negative #(if (neg? %)
                             (+ length % 1)
                             %)
         start (convert-negative start_)
         end (convert-negative end_)]

     (clojure.core/subs s start end))))

(let [s "fuck you bobby!"]

  (assert (= s (subs s 0 -1)))
  (assert (= s (subs s 0)))
  (assert (= (subs s -3 -1) (subs s (- (count s) 2))  (subs s -3))))


;; ================= find fixpoint ========================================
(defn iterate-fix
  "applies f until application becomes idempotent"
  [f x]
  (reduce (fn [b a] (cond (= b a) (reduced b) :default a))  (iterate f x)))


;; ================= html ========================================
#_(defn html->hiccup
    [string_]
    (-> (hickory.core/as-hiccup
         (hickory.core/parse
          string_))
        first nnext next first nnext first))


