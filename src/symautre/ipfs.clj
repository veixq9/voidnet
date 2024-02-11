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
  [content-STRING]
  (-> @(http/post "http://localhost:5001/api/v0/add"
                  {
                   ;; :content-type "application/json"
                   :multipart [{
                                :name "some name"
                                :content content-STRING}]})
      :body
      (clojure.data.json/read-str )
      (get "Name")))

(comment
  ;; use MFS?
  (read-string (cat "QmT2iJPnAjN7pzJzXqFyTzJGUGzDi9wRbyNXJHDQFi95tp"))
  (read-string (cat "QmS7FDjMhn7QaV4xaZ3eHajA8ApmUwhqD6uAPyhFK83yrk"))
  (read-string (cat y))
  (read-string (cat x))
  (def x
    (-> (http/post "http://localhost:3030/api/v0/add"
                   {:body (pr-str
                           {:id "e838e784-6884-4b31-87ed-c82ffbfb8fc4",
                            :timestamp.unix 1707674610909,
                            :timestamp "2024-02-11T18:03:30.909372018Z",
                            :body #{},
                            :actor/public-key-ed25519
                            "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"})})
        deref
        :body
        slurp
        read-string
        ))
  (read-string (cat x))
  (def y
    (add 
     (pr-str {:id "441e519b-7ac3-4734-bc77-c9def105d57d",
              :timestamp 1707675092215,
              :body #{},
              :actor/public-key-ed25519
              "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"})))
  
  (assert (= (-> (http/post "http://localhost:3030/api/v0/add" {:body "my stuffxxe 222"})
                 deref
                 :body
                 slurp)
             "QmWeJd6MsS48FRZC3WiX9fXNPPcBAr6NCybsH12AY3oVKk"))




  (keys @x)
  (slurp (:body @x))
  (def z *2)
  (:status @x)
  (println @x)
  
  
  
  )





