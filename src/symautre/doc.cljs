(ns symautre.doc
  (:require [symautre.tools.core :as t :refer [--> uuid timestamp-unix]]))

(defn doc
  ([]
   {
    :id (uuid)
    :timestamp.unix (timestamp-unix)
    :timestamp (t/timestamp) 

    :content ""

    :author nil
    })

  ([m]
   (merge (doc) m)))


(comment

  (doc {
        :meta/id "author-0ef50a62-503b-4c16-ab68-f94c8ff65ab1"
        :doc/id "23020e4a-f3d6-4275-9be3-6b67d003ad4f"
        :id [:author "0ef50a62-503b-4c16-ab68-f94c8ff65ab1"]

        :author/id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1"
        :author/email "johnny@foo.bar"
        :author.public-key ""})

  {:author.id "johnny", :meta "author declaration", :content "", :timestamp.unix 1700647158359, :author.public-key "", :author nil, :id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1", :author.email "johnny@foo.bar", :timestamp #inst "2023-11-22T09:59:18.359-00:00"}

  {:author.id "johnny", :meta "author declaration", :content "", :timestamp.unix 1700646706787, :author.public-key "", :author "johnnys public key or some id", :id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1", :author.email "johnny@foo.bar", :timestamp #inst "2023-11-22T09:51:46.787-00:00"})






