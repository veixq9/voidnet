(ns symautre.local-storage
  ;; (:refer-clojure [cljs.core :exclude [get ]])
  )


(def root-key :clojure)

(defn clear!
  ([]
   (-> js/window.localStorage .clear)))

(defn get-local
  ([]
   (cljs.reader/read-string (-> js/window.localStorage (.getItem :clojure))))
  ([k & ks]
   (cljs.core/get-in (get-local) (concat [k] ks))))

(defn set-local!
  ([ks v]
   (assert (vector? ks))
   (-> js/window.localStorage
       (.setItem :clojure (assoc-in (get-local) ks v)))))

(defn dissoc-local!
  ([k]
   (-> js/window.localStorage
       (.setItem :clojure (dissoc (get-local) k)))))

(comment
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
