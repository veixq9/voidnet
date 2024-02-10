(ns symautre.doc
  (:require [symautre.tools.core :as t :refer [--> uuid timestamp-unix]]
            [reagent.core :as r]
            symautre.local-storage
            [cljs-http.client ]

            [clojure.core.async :as a]))

(defn doc
  ([]
   {
    :id (uuid)
    :type :document
    :timestamp.unix (timestamp-unix)
    :timestamp (.toString (t/timestamp))


    ;; :content ""
    :ipfs/id ""
    ;; :author nil
    :body #{}
    })

  ([m]
   (merge (doc) m)))

(def return doc)

(defn button
  [state doc-ratom & more]
  (fn[state doc-ratom & more]
    (into [:button.w3-cell.w3-button] more)))

(defn- view
  [document_]
  (fn [document_]
    (let [{:keys [id title body timestamp] :as document_} document_]
      [:div {:id id :key id}
       [:h4.w3-bold title]
       [:span {:style {:font-style :italic}} (subs (str timestamp) 3 24)]
       
       [:div {:style {:white-space :pre-line}}
        (cond
          (string? body)
          [:p body]

          (and (vector? body) (not (keyword? (first body))))
          (for [x body]
            [:p {:key (t/uuid)} x])

          :default
          body
          )]])))

;;; public key address
;;; local storage
(defn- edit
  [doc_ mode]
  (r/with-let [transient-state (r/atom (pr-str (:body @doc_)))]
    (fn [doc_ mode]
      [:div

       [:textarea {:rows 10 :style {:width "100%" :color "white" :background-color "black" }
                   :value @transient-state
                   :on-change #(reset! transient-state (-> % .-target .-value))}]

       [:button {:on-click #(tap> (fn[s] (do (swap! doc_ assoc :body (cljs.reader/read-string @transient-state))
                                             (swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit))))))}
        "confirm"]
       
       [:button {:on-click #(tap> (fn[s] (swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))))}
        "cancel"]
       ])))

(defn- super-edit
  [doc_ mode]
  (r/with-let [transient-state (r/atom (pr-str @doc_))]
    (fn [doc_ mode]
      [:div

       [:textarea {:rows 10 :style { :width "100%" :height "100%" :color "white" :background-color "black" }
                   :value @transient-state
                   :on-change #(reset! transient-state (-> % .-target .-value))}]

       [:button {:on-click #(tap> (fn[s] (do (reset! doc_ (cljs.reader/read-string @transient-state))
                                             (swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit))))))}
        "confirm"]
       
       [:button {:on-click #(tap> (fn[s] (swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))))}
        "cancel"]
       ])))

#_(defn document-old
    [state id]
    (fn [state id]
      (r/with-let [doc-ratom (r/cursor state [id])
                   mode (r/atom :view)
                   display (r/atom false)
                   style (r/atom {})
                   class (r/atom "")                  ]
        (println "rendering document")
        [:div {:key id
               ;; :class @class
               :style @style
               :on-mouse-enter #(do (println "hover")
                                    (swap! display not))
               :on-mouse-leave #(do (println "mouse out")
                                    (swap! display not))}
         (cond
           (= :edit @mode)
           [edit doc-ratom mode]

           :default
           [:div
            [view @doc-ratom]
            
            [(fn [display]
               [:div#buttons.w3-container.w3-cell-row.w3-tiny {:style {:visibility (if @display "visible" "hidden")}}
                [button state doc-ratom  {:id "edit" :on-click #(swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))} "✎"  ]
                [button state doc-ratom  {:id "fullscreen" :on-click #(do (swap! state assoc :modal id))} "⛶"]
                
                [button state doc-ratom
                 {
                  :id "ipfs-save"
                  :on-click
                  #(do (println "saving to ipfs: " @doc-ratom)
                       (a/go (println (a/<! (cljs-http.client/post "/api/v0/add" {:body @doc-ratom}))))
                       )


                  }
                 "ipfs!"]
                
                [button state doc-ratom
                 {
                  :id "save"
                  :on-click
                  #(do (println "persisting " @doc-ratom)
                       (symautre.local-storage/set-local! [id] @doc-ratom))

                  ;; (-> js/window.localStorage (.getItem :posts ))
                  }
                 "🖫"]

                
                [button state doc-ratom
                 {
                  :id "clipboard"
                  :on-click
                  #(do (js/navigator.clipboard.writeText @doc-ratom))}
                 "📋"]               

                [button state doc-ratom {
                                         :id "delete"

                                         :on-click
                                         #(tap> (fn[s] (do (println "deleting " @doc-ratom)
                                                           (symautre.local-storage/dissoc-local! id)
                                                           (swap! s dissoc id)
                                                           )))}
                 "⛝"]




                [button state doc-ratom
                 {
                  :id "pin"
                  ;; :tooltip "pin"

                  :on-click
                  #(tap> (fn[s] (do (println "pinning " @doc-ratom)
                                    (symautre.local-storage/assoc-local! :posts/pinned id)
                                    (swap! s assoc :posts/pinned id))))}
                 "🖈"]
                ]) display] ])])))

(defn buttons
  [state doc-ratom]
  (fn [state doc-ratom]
    (let [id (:id @doc-ratom)
          mode (r/cursor state [:ui :edit id])]
      [:div#buttons.w3-container.w3-cell-row.w3-xxlarge

       [button state doc-ratom  {:id "edit" :on-click #(swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))} "✎"  ]

       [button state doc-ratom  {:id "super-edit" :on-click #(swap! mode (fn[mode_] (if (= mode_ :super-edit) :view :super-edit)))} "✎+"  ]
       
       [button state doc-ratom  {:id "fullscreen" :on-click #(do (swap! state update-in [:modal] (fn[e] (if (nil? e) id nil))))} "⛶"] 
       
       [button state doc-ratom
        {
         :id "ipfs-save"
         :on-click
         #(do (println "saving to ipfs: " @doc-ratom)
              (a/go (println (a/<! (cljs-http.client/post "/api/v0/add" {:body @doc-ratom}))))
              )


         }
        "ipfs!"]
       
       #_[button state doc-ratom
          {
           :id "save"
           :on-click
           #(do (println "persisting " @doc-ratom)
                (symautre.local-storage/set-local! [id] @doc-ratom))

           ;; (-> js/window.localStorage (.getItem :posts ))
           }
          "🖫"]

       
       [button state doc-ratom
        {
         :id "clipboard"
         :on-click
         #(do (js/navigator.clipboard.writeText @doc-ratom))}
        "📋"]               

       [button state doc-ratom {
                                :id "delete"

                                :on-click
                                #(tap> (fn[s] (do (println "deleting " @doc-ratom)
                                                  (symautre.local-storage/dissoc-local! id)
                                                  (swap! s dissoc id)
                                                  )))}
        "🗑"]




       [button state doc-ratom
        {
         :id "pin"
         ;; :tooltip "pin"

         :on-click
         #(tap> (fn[s] (do (println "pinning " @doc-ratom)
                           (swap! s assoc :posts/pinned id))))}
        "🖈"]
       ])))

(defn document-old-2
  [state id]
  (fn [state id]
    (r/with-let [doc-ratom (r/cursor state [id])
                 mode (r/cursor state [:ui :edit id])]
      (println "rendering document " id)
      [:div.w3-container {:key id}
       (cond
         (= :edit @mode)
         [edit doc-ratom mode]

         (= :super-edit @mode)
         [super-edit doc-ratom mode]

         :default
         [:div
          [view @doc-ratom]])
       [:div {:style {:float :bottom}} [buttons state doc-ratom]]])))

(defn document-old-3
  [state id]
  (r/with-let [doc-ratom (r/cursor state [id])
               mode (r/cursor state [:ui :edit id])
               selected (r/cursor state [:selected])]
    (fn [state id]
      (println "rendering document " id)
      [:div.w3-container {:key id
                          :style (merge {:min-height "100px"}
                                        (if (= id (:id @selected))
                                          {
                                           :border-right "3px solid #f44336"
                                           }
                                          {}))
                          :on-click #(do
                                       (swap! selected assoc :id id :controls [buttons state doc-ratom]))}
       (cond
         (= :edit @mode)
         [edit doc-ratom mode]

         (= :super-edit @mode)
         [super-edit doc-ratom mode]

         :default
         [:div
          [view @doc-ratom]])
       ])))

(defn document
  [state id]
  (r/with-let [doc-ratom (r/cursor state [:docs id])
               mode (r/cursor state [:ui :edit id])
               selected (r/cursor state [:selected])]
    (fn [state id]
      (println "rendering document id" id)
      ;; (println "rendering document " @doc-ratom)
      [:div.w3-container {:key id
                          :style (merge {:min-height "100px"}
                                        (if (= id (:id @selected))
                                          {
                                           :border-right "3px solid #f44336"
                                           }
                                          {}))
                          :on-click #(do
                                       (swap! selected assoc :id id :controls [buttons state doc-ratom]))}
       (cond
         (= :edit @mode)
         [edit doc-ratom mode]

         (= :super-edit @mode)
         [super-edit doc-ratom mode]

         :default
         [:div
          [view @doc-ratom]])
       ])))

(defn document-point
  [state id & more]
  (r/with-let [doc-ratom (r/cursor state [:docs id])
               mode (r/cursor state [:ui :edit id])
               selected (r/cursor state [:selected])]
    (fn [state id]
      (println "rendering document id" id)
      ;; (println "rendering document " @doc-ratom)
      [:div.w3-container.w3-margin.w3-border {:key id
                                              :style (merge {
                                                             ;; :overflow-x :break-word
                                                             :overflow-wrap :break-word
                                                             :overflow-y :hidden
                                                             :text-overflow-y :ellipsis
                                                             :width "200px"
                                                             :height "200px"
                                                             ;; :height "20%"
                                                             ;; :max-height "300px"
                                                             ;; :min-height "200px"
                                                             ;; :max-width "300px"
                                                             ;; :min-width "200px"
                                                             :float :left

                                                             }
                                                            (if (= id (:id @selected))
                                                              {:background-color "rgba(255,0,0,0.1)"}
                                                              #_{:border-color "#8b0000"
                                                                 :border-right-style "solid"
                                                                 :border-side :right}
                                                              {}))
                                              :on-click #(do
                                                           (swap! selected assoc :id id :controls [buttons state doc-ratom]))}
       (into
        (cond
          (= :edit @mode)
          [edit doc-ratom mode]

          (= :super-edit @mode)
          [super-edit doc-ratom mode]

          :default
          [:div
           [view @doc-ratom]
           #_[(fn [document_]
                (let [{:keys [id title body timestamp] :as document_} document_]
                  [:div {:id id :key id}
                   [:p (str body)]
                   ]))  @doc-ratom]])
        more
        )
       ])))

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
          [:button.w3-button.w3-border-white.w3-border {:on-click #(swap! mode (fn[mode_] (if (= mode_ :edit) :view :edit)))} "edit" ]
          [:button.w3-button.w3-border-white.w3-border
           {:on-click
            #()}
           "copy"]]]
        )
      ))





(comment

  (def foo (atom nil))
  (a/go
    (reset! foo (a/<! (cljs-http.client/get "/posts/posts2.edn" ))))
  (def foo (atom nil))
  (a/go
    (reset! foo (a/<! (cljs-http.client/get "/posts/posts2.edn" ))))
  (def foo (atom nil))
  (a/go
    (reset! foo (a/<! (cljs-http.client/get "/posts/posts2.edn" ))))
  foo
  (-> @foo :body)

  [{:timestamp #inst "2023-11-24T19:31:15.317-00:00", :timestamp.unix 1700854275317, :title "Data vs Morph", :media [:iframe {:src "https://www.youtube.com/embed/p19PzA5HauY", :title "YouTube video player", :frameborder "0", :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share", :allowfullscreen true}], :body ["to my knowledge all these systems implicitly or explicitly rely on an ordered sequence of actions. \nnow writing a transformation that converts these actionLogs to each other will buy you any database you want." "the thought process did not incorporate an intermediary datastructure such as JSON or EDN. so it was mostly HTTP POST & PUT requests directly mapping to storage places in tables.\n\nContent-Type: application/x-www-form-urlencoded" "with Algebra, there are multiple Voids:\nQualified Voids (Null fields) which are necessarily morphological nothings\nthere be exposure to Isomorphic indistinguishability\n(abstract walls which are ultimately the column names)\n\nwhile with pure data, types are merely symbolic links." "moving away from Tables is to go against Types & Law enforcement which scale clunkily with platonic data. \nAlgebra's simplicity is walled so: \nMatter Data |-> Algebra Morph\ngoing backwards means:\nAlgebra Morph -> Matter [Data Data]" "the dataAtom model of relationalDatabases doesn't make sense to me. since you usually get your data in document form anyway, it requires a funny conversion from document form to SQL insert form where you take each keyvalue in a document and put the value under the correct column!" "so types should be no different than a foreign key, and therefore also data.\none may need those Algebraic sophistries for other purposes such as reification of abstract computers and memory cells. but that's more a physicist's or psychoanalyst's problem."]}]

  
  
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

(comment
  (doc)
  {:id "aed21d96-ddea-4352-ba33-a5f89298dcb7", :type :document, :timestamp.unix 1703390743821, :timestamp "2023-12-24T04:05:43.821Z", :content "", :ipfs/id ""}
  
  
  )
