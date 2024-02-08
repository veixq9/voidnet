(ns symautre.ui-body
  (:require
   [reagent.core :as r]
   [symautre.tools.core :as t]
   [symautre.doc :refer [ doc document]]
   [symautre.local-storage]
   [symautre.data.sample-data]
   [symautre.web3 :refer [wallet]]
   [symautre.slider :refer [slider]]

   [clojure.core.async :as a]
   [symautre.doc :as doc]
   [reagent.dom :as rd]))

(declare upload-download-docs new-doc)

;;; could make it better by not rerendering on pin changes
#_(defn doc-scrolls
    [state]
    (println "init doc-scrolls")
    (fn [state]
      (r/with-let [docs (r/cursor state [:docs])
                   pinned-id @(r/cursor state [:posts/pinned])
                   ;; doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state)))))
                   doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (vals @docs))))
                   doc-ids (if pinned-id (cons pinned-id doc-ids_) doc-ids_)]
        (println @docs)
        (println "render doc-scrolls")
        (println doc-ids_)
        (println doc-ids)
        
        (into [:div]
              (when (not (empty doc-ids))
                (for [id doc-ids]
                  [:div.w3-container.w3-border-bottom {:key id}
                   (when (= id pinned-id) [:span.w3-right "🖈"])
                   [document state id]]))))))

#_(defn doc-scrolls
    [state]
    (println "init doc-scrolls")
    (fn [state]
      (r/with-let [docs (r/cursor state [:docs])
                   pinned-id_ (r/cursor state [:posts/pinned])]
        
        (let [
              pinned-id @pinned-id_
              doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (vals @docs))))
              doc-ids (if pinned-id (cons pinned-id doc-ids_) doc-ids_)]
          (println "render doc-scrolls")
          ;; (println @docs)
          ;; (println doc-ids)
          @docs
          (into [:div]
                
                (when (not (empty doc-ids))
                  (for [id doc-ids]
                    [:div.w3-container.w3-border-bottom {:key id}
                     (when (= id pinned-id) [:span.w3-right "🖈"])
                     [document state id]])))))))

(defn doc-scrolls-sans-pin
  [state]
  (println "init doc-scrolls")
  (fn [state]
    (r/with-let [docs (r/cursor state [:docs])]
      (into [:div]
            (for [id (keys @docs)]
              [:div.w3-container.w3-border-bottom {:key id}
               [document state id]])))))

(defn doc-scrolls
  [state]
  (println "init doc-scrolls")
  (fn [state]
    (r/with-let [docs (r/cursor state [:docs])]
      (let [pinned @(r/cursor state [:posts/pinned])
            docs-sorted (sort-by (t/>>> val :timestamp.unix) > @docs)]
        (into [:div
               [:div.w3-container.w3-border-bottom {:key pinned}
                [:span.w3-right "🖈"]
                [document state pinned]]]

              (for [id (remove #{pinned} (keys docs-sorted))]
                [:div.w3-container.w3-border-bottom {:key id}
                 [document state id]]))))))

(defn doc-points
  [state]
  (println "init doc-scrolls")
  (fn [state]
    (r/with-let [docs (r/cursor state [:docs])]
      (let [pinned @(r/cursor state [:posts/pinned])
            docs-sorted (sort-by (t/>>> val :timestamp.unix) > @docs)]
        (into [:div
               [:div.w3-container.w3-border-bottom {:key pinned}
                [:span.w3-right "🖈"]
                [document state pinned]]]

              (for [id (remove #{pinned} (keys docs-sorted))]
                [:div.w3-container.w3-border-bottom {:key id}
                 [symautre.doc/document-point state id]]))))))




#_(defn doc-titles
    [state]
    (r/with-let [pinned-id @(r/cursor state [:posts/pinned])
                 doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state)))))
                 doc-ids (if pinned-id (cons pinned-id doc-ids_) doc-ids_)]
      (fn [state]
        (into [:div]
              (for [id doc-ids]
                (when-let [title (:title (get @state id))]
                  [:div.w3-container.w3-border-bottom {:key id}
                   [:p title]]))))))

#_(defn doc-nodes
  [state]
  (r/with-let [pinned-id @(r/cursor state [:posts/pinned])
               doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state)))))
               doc-ids (if pinned-id (cons pinned-id doc-ids_) doc-ids_)]
    (fn [state]
      (into [:div]
            (for [id doc-ids]
              (when-let [title (:title (get @state id))]
                [:div.w3-container.w3-border-bottom {:key id}
                 [:p title]]))))))

#_(defn animation-shite
    [state]
    (fn [state]
      (r/with-let [t (r/atom 0)
                   interval-id (r/atom nil)
                   data ["ambient barks transduce the other"
                         "& all the nites are obsidian"
                         "let's liquid speech for whiles"
                         "would this breach blink across ports enveloped in toilet horizons"
                         "rooks crows ravens dogs"
                         "taste the blank memory of void on superzero shawties  "
                         "bloodruns & foam obliviated in entropy edge"]
                   dt 2000
                   frame (fn [i t data_] [:div {:class (if (> @t (* i dt)) "fade-in-image" "hidden")}
                                          [:p  data_]])

                   interval-fn #(js/setInterval
                                 (fn [] (swap! t + dt))
                                 dt)

                   frames (into [:div] (mapv vector (repeat frame) (range) (repeat t) data))
                   ]



        (into [:div
               [:h1 "Full Digital Anvilist"]
               [:div.center
                [:img.w3-container
                 {
                  ;; :style {
                  ;;         :float :left
                  ;;         :width "30%"}
                  :src
                  "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}]
                
                

                [:div {:style {:float :left}} frames]
                #_(doseq [x data]
                    
                    )
                
                

                #_(r/with-let [x (atom (cycle ["■" ""]))]
                    [(fn [t]
                       @t
                       (swap! x rest)
                       [:span {:style {:display "block"}} (first @x)]) t])]]
              
              [(if-not @interval-id
                 [:button.w3-button.w3-xxlarge {:on-click #(do (reset! t 0)
                                                               (reset! interval-id (interval-fn)))}
                  "⏵"]
                 [:button.w3-button.w3-xxlarge {:on-click #(do (reset! t 0)
                                                               (js/clearInterval @interval-id)
                                                               (reset! interval-id nil))}])
               [:div {:style {:clear :both}}]
               [:hr]]

              ))))

(defn animation-shite-2
  [state]
  (fn [state]
    (r/with-let [t (r/atom 0)
                 interval-id (r/atom nil)
                 data ["ambient barks transduce the other"
                       "& all the nites are obsidian"
                       "let's liquid speech for whiles"
                       "would this breach blink across ports enveloped in toilet horizons"
                       "rooks crows ravens dogs"
                       "taste the blank memory of void on superzero shawties  "
                       "bloodruns & foam obliviated in entropy edge"]
                 dt 2000
                 display-data (r/atom [])
                 data-chan-atom (atom (a/to-chan! data))

                 ]

      (r/with-let [player (r/atom :stop)]
        [:div.w3-container
         [:h1 "Full Digital Anvilist"]
         [:div.w3-container
          [:img
           {
            :style {:display :block
                    :margin-left :auto
                    :margin-right :auto
                    :width "50%"
                    }  #_{
                          :float :left
                          :width "30%"}
            :src
            "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}]
          
          

          [:div.w3-container {:style {:float :left
                                      ;; :clear :left
                                      }}
           (conj (into [:div]
                       (for [x @display-data]
                         [:p {:class "fade-in-image" } x]))
                 (when (and (= @player :play) (not (empty? @display-data)))
                   [:span {:class "cursor-blinking"} "█"])
                 [:br])]]

         (case @player
           :stop [:button.w3-button.w3-xxlarge
                  {
                   :on-click #(do
                                (reset! display-data [])
                                (reset! data-chan-atom (a/to-chan! data))
                                (reset! player :play)
                                (a/go-loop [c @data-chan-atom]
                                  (case @player
                                    :play
                                    (let [x (a/<! c)]
                                      (if (some? x)
                                        (do  (swap! display-data conj x)
                                             (a/<! (a/timeout dt))
                                             (recur c))
                                        (do (reset! player :stop)
                                            )))
                                    :pause
                                    (do (a/<! (a/timeout 1000))
                                        (recur c)))))
                   } "⏵"]
           

           :play [:button.w3-button.w3-xxlarge
                  {:on-click  #(do (reset! player :pause)
                                   )}
                  "⏸"]

           :pause [:button.w3-button.w3-xxlarge {
                                                 :on-click #(do (reset! player :play))}
                   "⏵"])

         [:br]
         [:hr]]))))

#_(defn canvas
    [state]
    (swap! state assoc :canvas [:canvas {:id "canvas" :height 300}] )
    (r/with-let [this (r/cursor state [:canvas])]
      (fn [state]
        (let [get-ctx #(-> (r/current-component) (.getContext "2d"))]
          @this
          
          ))))

(defn misc
  [state]
  (fn [state]
    [:div

     #_[canvas state]
     [:div.w3-center.w3-container
      [:canvas.w3-border {:id "canvas" :width 400 :height 400}]]

     [:br]

     
     [animation-shite-2 state]

     
     
     [t/collapsible :root symautre.data.sample-data/sample-map  ]
     [:br]
     [:hr]
     [t/edn->hiccup symautre.data.sample-data/sample-map  ]
     
     (r/with-let [pinned (r/cursor state [:posts/pinned])]
       [:div
        [:h1.w3-h1 "pinned"]
        (if-let [pinned_ @pinned]
          [document state pinned_])])

     #_(r/with-let [id (r/cursor state [:posts/pinned])]
         (let [id_ @id]
           [pinned state id_]))

     [:div
      [:h1.w3-h1 "pubsub model"]
      [:h1.w3-h1 "new color new weather"]]

     [:div
      [:h1.w3-h1 "n!structions"]
      [:p "fork site"]
      [:p "add own namespace"]]
     

     [:h1.w3-h1 "trollbox"]
     [:p "[topic placeholder]"]

     #_[:p (pr-str @state)]
     #_[:div
        [:h1.w3-h1 "state"]
        [:p (pr-str (remove #(= :document (:type (val %))) @state))]
        [:br]
        [:p (pr-str @state)]]]))

#_(defn mid-column-2
    [state]
    (swap! state assoc-in [:ui :body :mid-column :core :content :core] [doc-scrolls state])
    (r/with-let [
                 mid-column_ (r/cursor state [:ui :body :mid-column])
                 content (r/cursor mid-column_ [:content])
                 tab (r/cursor state [:tab])]
      (if-not (some? @tab) (reset! tab :content))
      (fn [state]
        [:div.w3-border
         (case @tab
           
           :content
           ;; @content
           [:p "foo"]

           :io
           [upload-download-docs state]

           :misc
           [misc state])])))

(defn mid-column
  [state]
  (println "init mid-column")
  (r/with-let [tab (r/cursor state [:tab])
               content (r/cursor state [:ui :body :mid-column :content])]
    (if-not (some? @tab) (reset! tab :content))
    (fn [state]
      [:div.w3-border
       (case @tab
         
         :content
         [:div
          (or @content [doc-points state])
          
          #_[doc-scrolls state]
          #_[doc-titles state]]
         
         :io
         [upload-download-docs state]

         :misc
         [misc state])])))


(defn footer-links
  [state]
  (let [links   [{:url "https://www.tumblr.com/blog/arrowsfrom"
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
                 ]
        ]
    (fn [state]
      [:div.w3-container.w3-display-bottom-right
       [:div.w3-cell-row.w3-container {:style {:justify-content :center :display :flex}}
        (into [:div]
              (for [{:keys [url title timestamp]} links]
                [:div.w3-cell.w3-container
                 [:a {:key url
                      ;;:src url
                      :href url} title]

                 ]))]])))



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
  [state & more]
  (fn [state & more]
    [:button.w3-btn.w3-border-white.w3-border.w3-round.w3-hover-red
     {:style {:float :left}
      :on-click
      #(let [new-doc (doc (select-keys @state [:actor.evm/address ]))
             id (:id new-doc)]
         (swap! state assoc-in [:docs id] new-doc))}
     [:span (if (empty? more) "new" (apply str more))]]))



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
     [:span "upload"]
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
     [:div
      [:span  "download"]
      [:button.w3-btn {:id "file-output"
                       :on-click #(save-file "myfile.edn" "text/edn" (pr-str (symautre.local-storage/get-local)))} "download"]]
     #_[:button.w3-btn {:id "file-output" :on-click #(tap> (fn [state]
                                                             (println "downloading file")
                                                             (let [url (js/URL.createObjectURL (js/Blob. (clj->js (vector (str @state))) #js {:type "text/edn"}))]
                                                               (js/window.navigator.msSaveBlob url)
                                                               )))} "download"]]
    )
  )

(defn tab-item
  [state id]
  (println "init tab item")
  (r/with-let [local-state (r/cursor state [:tab])]
    (fn [state id]
      (let [this (r/current-component)]
        [:div {:key id
               :class (str "w3-third tablink  w3-padding")
               :style {:float :left
                       :border-bottom "5px solid grey"
                       
                       :min-width "20%"
                       :border-color (if (= id @local-state) "#df00a6" "grey")
                       }
               :on-click #(tap> (fn[ss] (swap! ss assoc :tab id))
                                
                                ;; (println (-> this (r/ )))
                                ;; (set! (-> this (r/props ) .-style .-display ) "none")
                                )}
         [:a id]
         ]))))

(defn tab
  [s]
  (tap> (fn [state] (swap! state assoc-in [:tab] :misc)))
  (let [
        displayables [:content :io :misc]
        display (r/atom :content)]
    (fn [s]
      (println "hello "  (r/current-component))
      
      (into [:div {:style {:width "100%" :cursor :pointer}}]
            (for [x displayables]
              [tab-item s x]
              )))))

(defn dropdown
  [state title & xs]
  (fn [state title & xs]
    (r/with-let [dropdown-hide? (r/cursor state [:controls title :dropdown])]
      [:div#controls.view [:button.w3-btn.w3-border-white.w3-border.w3-round
                           {:style {:width "100%"}
                            :on-click #(swap! dropdown-hide? not)}
                           [:span.w3-align-left.w3-left {:style {}}  (str (if @dropdown-hide? "⮞"  "⮟" ) "  "  title)]]
       (into [:div.w3-container {:class (if @dropdown-hide? :w3-hide :w3-show)}

              ;; [:button.w3-btn.w3-border-white.w3-border.w3-round {:on-click #()} "button"]
              ]
             xs)]
      )))

#_(defn controls
    [state]
    (fn [state]
      [dropdown state "controls"
       [new-doc state]
       [:button.w3-btn.w3-border-white.w3-border.w3-round
        {:on-click #(tap>
                     (fn [s]
                       (println "saving state to local storage")
                       (symautre.local-storage/set-local!
                        (into {} (remove (fn [[id v]]
                                           (not (or (map? v) (string? v) (keyword? v) (number? v))) ) @s))))
                     )}
        #_(symautre.local-storage/get-local)
        "persist!"]
       [dropdown state "view"
        [:div {:key (t/uuid)}
         [(fn [state]
            [:button.w3-btn {:key (t/uuid) :on-click #(tap> (fn[s](swap! s assoc-in [:controls :view ] :port-money)))}
             [:input {:style {} :type :radio :checked (= :port-money @(r/cursor state [:controls :view])) :on-change #()}]
             [:span.w3-margin "port money"]]) state]]
        [(fn [state]
           [:button.w3-btn {:key (t/uuid) :on-click #(tap> (fn[s](swap! s assoc-in [:controls :view ] :scroll)))}
            [:input {:style {} :type :radio :checked (= :scroll @(r/cursor state [:controls :view])) :on-change #()}]
            [:span.w3-margin "scroll"]]) state]]])
    )

#_[:post-titles
   (r/track!
    (fn [state]
      (tap> (fn [s] (swap! s (sort-by :timestamp.unix > (vals (filter #(= (:type (val %)) :document)  @state )))))))
    state)

   (defn post-titles
     [state posts]
     (fn [state posts]
       (println "rendering post titles")
       [:div#post-titles
        [:h1.w3-h1 "posts"]
        (into [:div]
              (for [p posts]
                [post-title p]))]) )


   [post-titles state]

   ]

(defn modal
  [state modal]
  (fn [state modal]
    [:div.w3-container.w3-border
     {:style
      {:position :absolute
       :z-index 3
       :background-color "#090909"
       ;; :background-color "darkblue"
       ;; :color "green"
       :display (if modal "block" "none")
       :width "80%"
       ;; :height "100%"
       :top "5%"
       :left "10%"
       ;; :bottom 0
       ;; :left 0
       ;; :right 0

       :align-items :center
       :justify-content :center
       ;; :padding "140px"
       :padding-left "10px"
       ;; :padding-right "10px"
       ;; :margin-top "1%"
       ;; :margin-left "1%"
       ;; :left 0
       ;; :top 0
       ;; :float :center
       ;; :padding-left "10%"
       ;; :margin-top "3%"
       ;; :padding-left "10px"
       
       }}

     [:div
      [:div {:style {:float :right}}
       [:button.w3-button {:on-click #(swap! state dissoc :modal)} "x"]
       ]
      [:br]
      (when modal
        [doc/document state modal])]]))

(defn control-bar
  [state]
  (r/with-let [selected (r/cursor state [:selected])]
    (reset! selected {:id "" :controls "click an item to interact with its methods"})
    (fn [state]
      (println "rendering control bar")
      (when (some? @selected) 
        [:div.w3-center.w3-black.w3-border {:style {:position :fixed
                                                    ;; :top "90%"
                                                    :bottom "0%"
                                                    :left "50%"
                                                    ;; :margin "5% auto"
                                                    :min-height "10%"
                                                    :width "100%"
                                                    :transform "translateX(-50%)"
                                                    ;; :margin-right "10%"
                                                    ;;           :width "100%"
                                                    }}
         [:div {:style {:float :right}}
          [:button.w3-button {:on-click #(swap! state dissoc :selected)} "x"]]
         [:span (subs (:id @selected) 0 20)]
         (:controls @selected)])
      )))




(defn views-controls
  [state]
  (fn [state]
    (let [id "view controls" ]
      [:div {:key id}
       [:button.w3-border.w3-round.w3-container.w3-btn
        {:key (t/uuid)
         :on-click (fn []
                     (tap>
                      (fn[s]
                        (swap! s update-in [:selected] merge
                               {:id id
                                :controls
                                [(fn [sss]
                                   [:div
                                    [:button.w3-border.w3-round.w3-container.w3-btn
                                     {:key (t/uuid)
                                      :on-click #(tap> (fn [s] (swap! s update-in [:ui :body :mid-column :content ] (constantly [doc-scrolls state]))))}
                                     "scroll"]
                                    #_[:button.w3-border.w3-round.w3-container.w3-btn
                                       {:key (t/uuid)
                                        :on-click (tap> (fn [s] (swap! s update-in [:ui :body :mid-column :content] (constantly  [doc-titles]) )))}
                                       "titles"]

                                    [:button.w3-border.w3-round.w3-container.w3-btn
                                     {:key (t/uuid)
                                      :on-click #(tap> (fn [s] (swap! s update-in [:ui :body :mid-column :content]
                                                                      (constantly [doc-points state]))))}
                                     "hovering"]
                                    ]) state]}))))}
        
        "views"]])))

(defn left-column
  [state & more]
  (fn [state & more]
    [:div#left-column.w3-cell.w3-container.w3-left {:style {:width "20%"}}
     
     
     (into
      [:div

       [:div.w3-container [new-doc state "new!"]]
       [:hr]

       ;; [slider state]
       ;; [symautre.slider/randomize state]
       [:div "ochannel :view content :controls pop push"]
       [symautre.slider/slider-button state]
       [:br]
       #_[views-controls state]
       [:br]
       
       #_[(fn [state]
            [:div.w3-container {:style {:clear :left :float :left}}
             [:label {:float :right} "view:"]
             [:button.w3-btn {:key (t/uuid) :on-click #(tap> (fn[s](swap! s assoc-in [:controls :view ] :scroll)))}
              [:input {:style {} :type :radio :checked (= :scroll @(r/cursor state [:controls :view])) :on-change #()}]
              [:span.w3-margin "scroll"]]]) state]]
      more)

     
     #_[controls state]]))

(defn button
  [state {:keys [title on-click-fn]}]
  (fn [state]
    [:button.w3-border.w3-round.w3-container.w3-btn
     {:on-click
      (fn [s] (tap> on-click-fn))}
     title]))

(defn body
  [state]
  ;; (swap! state assoc-in [:ui :body :mid-column :core] [mid-column state])
  (fn [state]
    (println "rendering body")
    [:div.w3-container {:style {
                                :background-color @(r/cursor state [:slider :hex])
                                ;; :background-color "#090909"
                                ;; :font-size "200%"
                                }}

     #_[:div.w3-center.w3-black.w3-border {:style {:position :fixed
                                                   ;; :top "90%"
                                                   :bottom "0%"
                                                   :left "50%"
                                                   ;; :margin "5% auto"
                                                   :min-height "10%"
                                                   :width "100%"
                                                   :transform "translateX(-50%)"
                                                   ;; :margin-right "10%"
                                                   ;;           :width "100%"
                                                   }}
        ]
     [control-bar state]




     
     ;; [:a {:href "/posts.edn"} "posts"]
     
     [:div.w3-row {:id "top"}
      ;; [modal state @(r/cursor state [:modal])] 
      [modal state @(r/cursor state [:modal])]
      
      #_[(fn [modal]
           [:div {:style {:position :absolute :z-index 3 :background-color "black"
                          :color "green"
                          :display (if @modal "block" "none")
                          :width "80%" :height "100%"
                          :padding-left "10px"
                          :padding-top "10px"
                          }}

            [doc/document state @modal]])
         (r/cursor state [:modal])]
      [wallet state]
      [:h1.w3-center [:a {:href "#top" :style {:text-decoration "none"}} "VOIDGATE:://VCN88TS"]]]

     #_[:div#tab.w3-cell-row {:style {:width "100%"}}
        [tab state]]

     
     
     [:br]

     


     
     [:div#columns.w3-cell-row

      #_[left-column state]
      [left-column state
       [views-controls state]
       [button state {:title "stack" :on-click-fn (fn [s])}]
       [:br]
       [button state {:title "UNITS" :on-click-fn (fn [s])}]]

      [:div#mid-column.w3-container.w3-cell {:style {:float :left :min-width "50%" :max-width "30px"}}
       [tab state]
       
       [mid-column state]
       ;; @(r/cursor state [:ui :body :mid-column])

       ]
      
      [:div#right-col.w3-cell.w3-container {:style {:float :left :height "100%" :width "20%" :max-width "30px"}}
       [:h1.w3-h1 "voidnet://"]
       [:p "[placeholder]"]

       ]]

     [:br]
     [:br]
     [:br]
     [:br]
     [:br]
     [:hr]
     #_[footer-links state]
     [:br]]))


