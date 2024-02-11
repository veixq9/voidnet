(ns symautre.voidnet.misc
  (:require
   [clojure.core.async :as a]
   [reagent.core :as r]
   [symautre.data.sample-data]
   [symautre.doc :refer [document]]
   [symautre.tools.core :as t]))

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



(defn misc
  [state]
  (fn [state]
    [:div

     [animation-shite-2 state]
     
     #_[canvas state]
     [:div.w3-center.w3-container
      [:canvas.w3-border {:id "canvas"
                          ;; :width 400 :height 400
                          }]]

     [:br]

     


     
     
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

