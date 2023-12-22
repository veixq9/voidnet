(ns symautre.ui-body
  (:require
   [reagent.core :as r]
   [symautre.tools.core :as t]
   [symautre.doc :refer [ doc document]]
   [symautre.local-storage]
   [symautre.data.sample-data]
   [symautre.web3 :refer [wallet]]

   [clojure.core.async :as a]))

(declare upload-download-docs new-doc)

(defn content
  [state]
  (let [pinned-id (:posts/pinned @state)
        doc-ids_ (remove #{pinned-id} (map :id (sort-by :timestamp.unix > (filter #(= :document (:type %)) (vals @state)))))
        doc-ids (if pinned-id (cons pinned-id doc-ids_) doc-ids_)
        ]
    (into [:div]
          (for [id doc-ids]
            [:div.w3-container.w3-border-bottom {:key id}
             (when (= id pinned-id) [:span.w3-right "🖈"])
             [document state id]]))))

(defn animation-shite
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
             [:div
              [:img.w3-container
               {
                :style {
                        :float :left
                        :width "30%"}
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
            :style {
                    :float :left
                    :width "30%"}
            :src
            "https://media.tenor.com/wE_qxJqpxj0AAAAd/nether-portal-minecraft.gif"}]
          
          

          [:div.w3-container {:style {:float :left :clear :left}}
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

(defn misc
  [state]
  (fn [state]
    [:div
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

     [:div
      [:h1.w3-h1 "state"]
      [:p (pr-str (remove #(= :document (:type (val %))) @state))]
      [:br]
      [:p (pr-str @state)]]]))

(defn mid-column
  [state]
  (r/with-let [tab (r/cursor state [:tab])]
    (if-not (some? @tab) (reset! tab :content))
    (fn [state]
      [:div.w3-border
       (case @tab
         
         :content
         [content state]

         :io
         [upload-download-docs state]

         :misc
         [misc state])]


      
      )))



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
                      (assoc id new-doc)))))} [:span "new!"]]))



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
  (r/with-let [local-state (r/cursor state [:tab])]
    (fn [state id]
      (let [this (r/current-component)]
        (println "hello2 " this)
        [:div {:key id
               :class (str "w3-third tablink  w3-padding")
               :style {:float :left
                       :border-bottom "5px solid grey"
                       
                       :min-width "20%"
                       :border-color (if (= id @local-state) "red" "grey")
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

(defn controls
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

(defn pinned
  [state id]
  (fn [state id]
    ;; let [pinned (r/reaction (get-in @state [:posts/pinned]))]
    [:div
     [:h1.w3-h1 "pinned"]
     (if (some? id)
       [document state id]
       [:p "placeholder"]
       )
     ]
    ))

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

(defn body
  [state]
  (fn [state]
    (println "rendering body")
    [:div.w3-container {:style {

                                :background-color "#090909"
                                ;; :font-size "200%"

                                }}
     
     ;; [:a {:href "/posts.edn"} "posts"]
     [:div.w3-row {:id "top"}
      #_[wallet state]
      [:h1.w3-center [:a {:href "#top" :style {:text-decoration "none"}} "voidnet:://VCN88TS"]]]

     [:div#tab.w3-cell-row {:style {:width "100%"}}
      [tab state]]

     [:br]
     
     [:div#cols.w3-cell-row

      [:div#left-col.w3-cell.w3-container.w3-left {:style {:width "20%"}}
       [controls state]]

      [:div#mid-col.w3-container.w3-cell {:style {:float :left :min-width "50%" :max-width "30px"}}
       [mid-column state]]
      
      [:div#right-col.w3-cell.w3-container.w3-border-left {:style {:float :left :height "100%" :width "20%" :max-width "30px"}}
       [:h1.w3-h1 "voidnet://"]
       [:p "[placeholder]"]

       ]]

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


