(ns symautre.doc
  (:require [symautre.tools.core :as t :refer [--> uuid timestamp-unix]]
            [reagent.core :as r]
            symautre.local-storage
            ))

(defn doc
  ([]
   {
    :id (uuid)
    :type :document
    :timestamp.unix (timestamp-unix)
    :timestamp (.toString (t/timestamp))


    :content ""

    :author nil
    })

  ([m]
   (merge (doc) m)))

(def return doc)



(defn- view
  [document_]
  (fn [document_]
    (let [{:keys [title media body timestamp] :as document_} document_]
      [:div {:id title :key title}
       [:h1.w3-h1 title]
       [:span {:style {:font-style :italic}} (subs (str timestamp) 3 24)]
       
       [:div {:style {:white-space :pre-line}}
        (cond
          (string? body)
          [:p body]

          (and (vector? body) (not (keyword? (first body))))
          (for [x body]
            [:p {:key (t/uuid)} x])

          :default
          body)]])))

;;; public key address
;;; local storage
(defn- edit
  [doc_ mode]
  (r/with-let [transient-state (r/atom (pr-str (:body @doc_)))]
    (fn [doc_ mode]
      [:div

       [:textarea {:style {:width "100%" :color "white" :background-color "black" }
                   :value @transient-state
                   :on-change #(reset! transient-state (-> % .-target .-value))}]

       [:button {:on-click #(do (swap! doc_ assoc :body (cljs.reader/read-string @transient-state))
                                (swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit))))}
        "save!"]])))



(defn document2
  [state id]
  (let [doc-ratom (r/cursor state [id])
        mode (r/atom :view)]
    (fn [state id]
      ;; (println "rendering document")
      [:div {:key id}
       (cond
         (= :edit @mode)
         [edit doc-ratom mode]

         :default
         [view @doc-ratom]) 

       [:div
        [:btn.w3-button.w3-border-white.w3-border {:on-click #(swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))} "edit" ]
        [:btn.w3-button.w3-border-white.w3-border
         {:on-click
          #()}
         "copy"]


        [:btn.w3-button.w3-border-white.w3-border
         {
          :on-click
          #(do (println "persisting " @doc-ratom)
               (symautre.local-storage/set-local! [id] @doc-ratom))

          ;; (-> js/window.localStorage (.getItem :posts ))
          }
         "persist!"]

        [:btn.w3-button.w3-border-white.w3-border
         {
          :on-click
          #(tap> (fn[s] (do (println "deleting " @doc-ratom)
                            (symautre.local-storage/dissoc-local! id)
                            (swap! s dissoc id)
                            )))

          ;; (-> js/window.localStorage (.getItem :posts ))
          }
         "delete!"]
        ]]
      )
    ))

#_(defn document
    [document_]
    (let [doc-ratom (r/atom document_)
          mode (r/atom :view)]
      (fn [document_]
        [:div
         (cond
           (= :edit @mode)
           [edit doc-ratom ]

           :default
           [view @doc-ratom mode]) 

         [:div
          [:btn.w3-button.w3-border-white.w3-border {:on-click #(swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))} "edit" ]
          [:btn.w3-button.w3-border-white.w3-border
           {:on-click
            #()}
           "copy"]]]
        )
      ))



(comment
  (doc)

  (let [x (doc)]
    (symautre.local-storage/set-local! (:id x) x))

  
  (doc {
        :meta/id "author-0ef50a62-503b-4c16-ab68-f94c8ff65ab1"
        :doc/id "23020e4a-f3d6-4275-9be3-6b67d003ad4f"
        :id [:author "0ef50a62-503b-4c16-ab68-f94c8ff65ab1"]

        :author/id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1"
        :author/email "johnny@foo.bar"
        :author.public-key ""})

  {:author.id "johnny", :meta "author declaration", :content "", :timestamp.unix 1700647158359, :author.public-key "", :author nil, :id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1", :author.email "johnny@foo.bar", :timestamp #inst "2023-11-22T09:59:18.359-00:00"}

  {:author.id "johnny", :meta "author declaration", :content "", :timestamp.unix 1700646706787, :author.public-key "", :author "johnnys public key or some id", :id "0ef50a62-503b-4c16-ab68-f94c8ff65ab1", :author.email "johnny@foo.bar", :timestamp #inst "2023-11-22T09:51:46.787-00:00"})






