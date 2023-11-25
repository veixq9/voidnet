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
   (swap! state assoc :clock/interval (js/setInterval #(tap> f) (* 1e3 interval-in-secs))))
 ]

[:misc
 (defn timeout []
   (let [seconds-elapsed (r/atom 0)]
     (fn []
       (js/setTimeout #(swap! seconds-elapsed inc) 1000)
       [:div
        "Seconds Elapsed: " @seconds-elapsed])))]

[:edn->hiccup]
[:node-tree]

