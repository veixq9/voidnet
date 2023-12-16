(ns symautre.local-storage
  (:require [cljs.reader])
  ;; (:refer-clojure [cljs.core :exclude [get ]])
  )


(def root-key :clojure)

(defn clear!
  ([]
   (-> js/window.localStorage .clear)))

(defn get-local
  ([]
   (cljs.reader/read-string (-> js/window.localStorage (.getItem :clojure)))
   )
  ([k & ks]
   (cljs.core/get-in (get-local) (concat [k] ks))))

(defn set-local!
  ([]
   (-> js/window.localStorage
       (.setItem :clojure {})))
  ([v]
   (-> js/window.localStorage
       (.setItem :clojure v)))
  ([ks v]
   (assert (vector? ks))
   (-> js/window.localStorage
       (.setItem :clojure (assoc-in (get-local) ks v)))))

(defn assoc-local!
  ([k v]
   (-> js/window.localStorage
       (.setItem :clojure (assoc-in (get-local) [k] v)))))

(defn init!
  []
  (when (= 'null (get-local))
    (set-local!)))

(defn dissoc-local!
  ([k]
   (-> js/window.localStorage
       (.setItem :clojure (dissoc (get-local) k)))))

#_(defn swap-local!
  [v]
  (set-local! (pr-str v)))

(comment
  (-> js/window.localStorage
      .clear)

  (cljs.reader/read-string (pr-str @state))
  (cljs.reader/read-string (-> js/window.localStorage (.getItem :clojure)))
  (get-local)
  (set-local! [:bar :hello] "yoo")

  (set-local! "boeafin" {
                         
                         :id "boeafin"
                         :type :document



                         :content ""

                         :author nil})
  (get :posts)
  (get)
  (get :foo)
  (set [:foo :damn] "bar"))
