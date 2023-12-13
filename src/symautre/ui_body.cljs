(ns symautre.ui-body
  (:require
   [reagent.core :as r]
   [symautre.tools.core :as t]
   [symautre.doc :refer [ doc document2]]
   [symautre.local-storage]
   ))

#_(defn content
    [state]
    (let [data (r/cursor state [:data])]
      (fn [state]
        (into [:div]
              (interpose [:div [:br ][:hr]]
                         (for [document_ @data]
                           [document document_]))))))

#_(defn content
    [state]
    (fn [state]
      (let [doc-ids (r/cursor state [:doc-ids])]
        (into [:div]
              (interpose [:div [:br] [:hr]]
                         (for [id @doc-ids]
                           [document2 state id]))))))

(defn content
  [state doc-ids & props]
  (println "generating content first time")
  (let [x (r/cursor state [:tab])]
    (fn [state doc-ids  & props]
      ;; (println "rendering content")
      (into [:div {:display @x}]
            (interpose [:div [:br] [:hr]]
                       (for [id doc-ids]
                         [:div {:key id}
                          [document2 state id]]))))))

#_(defn content
    [state docs]
    (println "generating content first time")
    (fn [state docs]
      (println "rendering content")
      (into [:div]
            (interpose [:div [:br] [:hr]]
                       (for [doc docs]
                         [:div {:key (:id doc)}
                          [document2 state id]])))))

(def links
  [{:url "https://www.tumblr.com/blog/arrowsfrom"
    :title "tumblr"}
   
   {:url
    "https://twitter.com/veixq9"
    :title "twitter"}
   
   {:title "soundcloud" :url "https://soundcloud.com/veixq9"}

   {:title "deviantart"
    :url "https://www.deviantart.com/likebad"}
   

   {:title "mastodon" :url "https://mastodon.social/@veixq9"}
   
   {:title 
    "github" :url "https://github.com/veixq9"}
   ])

#_(defn indexes []
    (fn []
      [:div.w3-container
       #_[:h1.w3-h1 "Exit"]

       [:br]
       (into [:div.w3-container]
             (for [{:keys [url title]} links]
               [:div
                [:a {:key url
                     ;;:src url
                     :href url} title]
                [:br]
                [:br]]))]))


(defn post-title
  [p]
  (fn [p]
    [:a.w3-conatiner { :href (str "#" (:title p))}
     [:div.w3-h2
      [:span (:title p)]

      [:span "   "]
      [:span {:style {:font-style :italic}} (subs (str (:timestamp p)) 3 24)]]]))

#_(defn post-titles
    [state]
    (r/with-let [doc-ids (r/cursor state [:doc-ids])]
      (fn [state]
        (println "rerendering post titles")
        (into [:div]
              (for [p (map #(get @state %) @doc-ids)]
                [:div {:key (:id p)}
                 [post-title p]]
                )))))

#_(defn post-titles
    [state posts]
    (fn [state]
      (println "rerendering post titles")
      (into [:div]
            (for [t titles]
              [:div {:key (:id p)}
               [post-title t]]
              ))))

(defn new-doc
  [state]
  (fn [state]
    [:button.w3-btn.w3-border-white.w3-border.w3-round
     {:on-click
      #(let [new-doc (doc)
             id (:id new-doc)]
         (swap! state
                (fn [state_]
                  (-> state_
                      ;; (update :doc-ids (fn [xs] (cons id xs)))
                      (assoc id new-doc)))))} [:span "new"]]))



(defn save-file [filename t s]
  (if js/Blob
    (let [b (js/Blob. #js [s] #js {:type t})]
      (if js/window.navigator.msSaveBlob
        (js/window.navigator.msSaveBlob b filename)
        (let [link (js/document.createElement  "a")]
          (aset link "download" filename)
          (if js/window.webkitURL
            (aset link "href" (js/window.webkitURL.createObjectURL b))
            (do
              (aset link "href" (js/window.URL.createObjectURL b))
              (aset link "onclick" (fn destroy-clicked [e]
                                     (.removeChild (.-body js/document) (.-target e))))
              (aset link "style" "display" "none")
              (.appendChild (.-body js/document) link)))
          (.click link))))
    (println "Browser does not support Blob")))


#_(defn btn-upload-image []
    ;; TODO: update XXXX with your api key
    (let [client (.init js/filestack "XXXX")]
      [:button
       {:on-click (fn []
                    (.then (.pick client (clj->js {:accept   "image/*"
                                                   :maxFiles 5}))
                           #(let [files-uploaded (->  % .-filesUploaded)
                                  file           (aget files-uploaded 0)
                                  file-url       (.-url file)]
                              (js/console.log "URL of file:" file-url))))}
       "Upload Image"]))

(defn upload-download-docs
  [state]
  (fn [state]
    [:div
     [:span "in"]
     [:input {:id "file-input" :type :file
              :on-change
              #(tap> (fn [state]
                       (-> (js/document.getElementById "file-input")
                           (.-files)
                           (aget 0)
                           .text
                           (.then (fn [this] (do #_(println %)
                                                 (doseq [[k v] (cljs.reader/read-string this)]
                                                   (symautre.local-storage/set-local! [k] v))
                                                 (swap! state merge (symautre.local-storage/get-local))
                                                 ))))))} ]
     [:p "out"]
     [:button.w3-btn {:id "file-output"
                      :on-click #(save-file "myfile.edn" "text/edn" (pr-str (symautre.local-storage/get-local)))} "download"]
     #_[:button.w3-btn {:id "file-output" :on-click #(tap> (fn [state]
                                                             (println "downloading file")
                                                             (let [url (js/URL.createObjectURL (js/Blob. (clj->js (vector (str @state))) #js {:type "text/edn"}))]
                                                               (js/window.navigator.msSaveBlob url)
                                                               )))} "download"]]
    )
  )

(defn tab-item
  [state id]
  (fn [state id]
    (let [this (r/current-component)]
      (println "hello2 " this)
      [:button.w3-button.w3-border.w3-cell
       {
        ;; :style {:width "100%"}
        :on-click #(tap> (fn[ss] (swap! ss assoc :tab id))
                         
                         ;; (println (-> this (r/ )))
                         ;; (set! (-> this (r/props ) .-style .-display ) "none")
                         )}
       id])))

(defn tab
  [s]
  (let [
        displayables [:foo :bar :baz]
        display (r/atom :content)]
    (fn [s]
      (println "hello "  (r/current-component))
      (into [:div.w3-cell-row.w3-middle {;; :class "w3-bar w3-black"

                                         :style {:width "100%"}}]
            (for [x [:new :content :misc]]
              [tab-item s x]
              )))))


(defn settings
  [state]
  (let [dropdown-hide? (r/atom false)
        settings-state (r/cursor state [:settings])]
    (fn [state]
      [:div
       [:h1.w3-h1 "settings"]

       [new-doc state]
       
       [:div.w3-container
        [upload-download-docs state]]
       [:p "vermutbar!"]
       [:p "io.move"]
       [:p [:span {:style {:color "red"}} "ש "] "color index mode selector " ]
       
       [:div#settings.view [:button.w3-btn.w3-border-white.w3-border.w3-round
                            {:style {:width "100%"}
                             :on-click #(swap! dropdown-hide? not)}
                            
                            [:span.w3-align-left.w3-left {:style {}}  (str (if @dropdown-hide? "⮞"  "⮟" ) "  "  "view")]]
        [:div.w3-container {:class (if @dropdown-hide? :w3-hide :w3-show)}

         [:button.w3-btn {:on-click (fn [] (tap> (swap! settings-state assoc :view :port-money)))}
          [:input {:style {} :type :radio :checked (= :port-money (:view @settings-state))}]
          [:span.w3-margin "port money"]]
         [:br]
         [:button.w3-btn {:on-click (fn [] (tap> (swap! settings-state assoc :view :scroll)))}
          [:input {:style {} :type :radio :checked (= :scroll (:view @settings-state))}]
          [:span.w3-margin "scroll"]]]]

       (r/with-let [dropdown-hide? (r/atom false)]
         [:div#settings.mode
          [:button.w3-btn.w3-border-white.w3-border.w3-round
           {:style {:width "100%"}
            :on-click #(swap! dropdown-hide? not)}
           
           [:span.w3-align-left.w3-left {:style {}}  (str (if @dropdown-hide? "⮞"  "⮟" ) "  "  "mode")]]
          [:div.w3-container {:class (if @dropdown-hide? :w3-hide :w3-show)}
           [:button.w3-btn {:on-click (fn [] (tap> (swap! settings-state assoc :mode :port-money)))}
            [:input {:style {} :type :radio :checked (= :port-money (:mode @settings-state))}]
            [:span.w3-margin "_"]]
           
           [:br]
           [:button.w3-btn {:on-click (fn [] (tap> (swap! settings-state assoc :mode :mode)))}
            [:input {:style {} :type :radio :checked (= :scroll (:mode @settings-state))}]
            [:span.w3-margin "_"]]]])
       ])))

(defn body
  [state]
  (fn [state]
    [:div.w3-container #_{:style { :font-size "200%"}}
     
     ;; [:a {:href "/posts.edn"} "posts"]
     [:div.w3-row {:id "top"}
      [:h1.w3-right "wallet"]
      [:h1.w3-center.w3-border [:a {:href "#top" :style {:text-decoration "none"}} "voidnet:://VCN88TS"]]]

     [:div.w3-row {:style {:width "100%"}}
      [tab state]
      ]
     
     [:div#left.w3-cell-row
      [:div.w3-cell.w3-container.w3-left {:style {:width "100%"}}
       [settings state]

       #_[indexes]
       #_[:div.w3-bar
          [:h1.w3-h1 "settings"]
          [:h1.w3-bar-item "foo"]
          [:button.w3-button "foo"]
          ]]

      [:div#mid.w3-container.w3-cell {:style {:min-width "50%" :max-width "30px"}}
       ;; [content state (get-in @state [:doc-ids])]

       [content state (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state))))]
       ;; [content state (filter #(= :document (:type %)) (vals @state))]
       ]
      
      [:div#right.w3-cell.w3-container.w3-border-left {:style {:width "20%" :max-width "30px"}}
       [:h1.w3-h1 "voidnet://"]
       [:p "[placeholder]"]

       [:div
        [:h1.w3-h1 "pinned"]
        [:p "[placeholder]"]
        ]

       [:div
        [:h1.w3-h1 "n!structions"]
        [:p "fork site"]
        [:p "add own namespace"]

        ]
       
       [:p "[placeholder]"]
       
       [:h1.w3-h1 "posts"]
       (let [posts (sort-by :timestamp.unix > (vals (filter #(= (:type (val %)) :document)  @state )))]
         (into [:div]
               (for [p posts]
                 [post-title p])))

       [:h1.w3-h1 "trollbox"]
       [:p "[topic placeholder]"]]]

     [:br]
     [:br]
     [:br]
     [:br]
     [:br]
     [:hr]
     [:div.w3-container.w3-display-bottom-right
      [:div.w3-cell-row.w3-container {:style {:justify-content :center :display :flex}}
       (into [:div]
             (for [{:keys [url title timestamp]} links]
               [:div.w3-cell.w3-container
                [:a {:key url
                     ;;:src url
                     :href url} title]

                ]))]]
     ]))


