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


    :content ""
    :ipfs/id ""
    ;; :author nil
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
                           (symautre.local-storage/assoc-local! :posts/pinned id)
                           (swap! s assoc :posts/pinned ))))}
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

(defn document
  [state id]
  (r/with-let [doc-ratom (r/cursor state [id])
               mode (r/cursor state [:ui :edit id])
               selected (r/cursor state [:selected])]
    (fn [state id]
      (println "rendering document " id)
      [:div.w3-container {:key id
                          :style (if (= id (:id @selected))
                                   {
                                    :border-right "3px solid #f44336"
                                    }
                                   {})
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
  
  )
