(ns symautre.ipfs
  (:refer-clojure :exclude [cat])
  (:require
   [clojure.data.json]
   [org.httpkit.client :as http]))

(defn show-config []
  @(http/get "http://localhost:5001/api/v0/config/show"))

(defn cat
  [x]
  (-> @(http/post "http://localhost:5001/api/v0/cat" {:query-params {:arg  x}})
      :body))

(defn add
  [content]

  (-> @(http/post "http://localhost:5001/api/v0/add"
                  {:multipart [{
                                :name "some name"
                                :content content}]})
      :body
      (clojure.data.json/read-str )
      (get "Name")
      ))

(comment
  (assert (= (-> (http/post "http://localhost:3030/api/v0/add" {:body "my stuffxxe 222"})
                 deref
                 :body
                 slurp)
             "QmWeJd6MsS48FRZC3WiX9fXNPPcBAr6NCybsH12AY3oVKk")))





