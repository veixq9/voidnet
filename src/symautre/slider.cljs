(ns symautre.slider
  (:require [thi.ng.color.core :as col]
            [reagent.core :as r]))

(def hex->rgba (fn [hex] (reduce-kv (fn [acc k v] (assoc acc k (* v 255))) {} (col/as-rgba (col/hex->int hex)))))
(defn rgba->hex
  "does not include alpha!!!"
  [map-rgba]
  (:col (col/as-css (apply col/rgba (map #(/ % 255) (map (partial get map-rgba) [:r :g :b :a]))))))



(defn slider-input
  [color-element]
  (fn [color-element]
    [:input
     {:type "range" :value @color-element :min 0 :max 255
      :style {:width "100%"}
      :on-change (fn [e]
                   (let [new-value (js/parseInt (.. e -target -value))]
                     (reset! color-element new-value)))}]))

#_(defn slider-old
    "rgba"
    [cursor-palette]

    (let [state (r/atom (hex->rgba (:background-color @cursor-palette)))
          [r g b a] (mapv (partial r/cursor state) [[:r] [:g] [:b] [:a]])]
      (r/track! #(swap! cursor-palette assoc :background-color (rgba->hex {:r @r :g @g :b @b :a @a})))
      (r/track! #(reset! state (hex->rgba (:background-color @cursor-palette))))
      
      (fn [cursor-palette]
        [:div.w3-container {:width "300%" :clear :both}
         [:div.w3-row
          ;; [:p (pr-str @cursor-palette)]
          ;; [:p (pr-str [@r @g @b @a])]
          [:p.w3-cell "colors"]]
         (into [:div.w3-row
                [:div.w3-cell
                 [:span {:style {:color "white" :margin-right "10px"}} (:background-color @cursor-palette)]]]
               (map #(identity [:div.w3-cell
                                [:span {:style {:color %1 :margin-right "10px"}} (str %2)]
                                #_[:i.material-icons "blank"]
                                #_[:span " "]]) ["red" "green" "blue" "white"] [@r @g @b @a]))
         [slider-input r]
         [slider-input g]
         [slider-input b]
         #_[slider-input a]
         ])))

(defn slider
  "rgba"
  [state]
  (when (nil? (:slider @state)) (swap! state assoc-in [:slider] (hex->rgba "#0")))
  (r/with-let [this (r/cursor state [:slider])

               r (r/cursor this [:r])
               g (r/cursor this [:g])
               b (r/cursor this [:b])
               a (r/cursor this [:a])

               hex (r/cursor state [:slider :hex])

               ]
    ;; NOTE: does not take into account alpha!
    (r/track! #(reset! hex (rgba->hex {:r @r :g @g :b @b :a @a})))

    (fn [state]
      [:div.w3-container {:width "300%" :clear :both}

       #_[:div.w3-row
          ;; [:p.w3-cell "colors"]
          #_[:label.w3-cell "colors"]
          [:span "color"]
          ]
       (into [:div.w3-row
              #_[:div.w3-cell
                 [:span {:style {:color "white" :margin-right "10px"}} @this]]]
             (map #(identity [:div.w3-cell
                              [:span {:style {:color %1 :margin-right "10px"}} (str %2)]]) ["red" "green" "blue" "white"] [@r @g @b @a]))
       [slider-input (r/cursor this [:r])]
       [slider-input (r/cursor this [:g])]
       [slider-input (r/cursor this [:b])]
       [slider-input (r/cursor this [:a])]
       [:span @hex]
       ])))

(defn randomize
  "rgba"
  [state]
  (fn [state]
    [:button.w3-border.w3-round.w3-container.w3-btn
     {:on-click
      (fn [] (tap> #(swap! state update :slider merge
                           #_(hex->rgba (str "#" (.toString (rand-int (inc 0xffffff)) 16)))
                           (zipmap [:r :g :b :a] [(rand-int 256) (rand-int 256) (rand-int 256) (rand-int 256) ])
                           )))}
     "randomize"]))

(defn slider-button
  "rgba"
  [state]
  (fn [state]
    [:button.w3-border.w3-round.w3-container.w3-btn
     {:on-click
      (fn [] (tap> #(swap! state assoc-in [:selected]
                           {:id "color"
                            :controls
                            [:div
                             [slider state]
                             [randomize state]]}

                           )))}
     "color"]))

(comment

  (hex->rgba (str "#" (.toString (rand-int (inc 0xffffff)) 16)))
  (goog.string/format "foo %x" (rand-int (inc 0xffffff)))

  
  (hex->rgba "#000")

  (rgba->hex (zipmap [:r :g :b :a] [(rand-int 256) (rand-int 256) (rand-int 256) 255]))
  (rgba->hex (dissoc (zipmap [:r :g :b :a] [(rand) (rand) (rand) (rand)]) :a))
  (rgba->hex (zipmap [:r :g :b :a] [(rand) (rand) (rand) 1]) )

  (rgba->hex {:r 103, :g 39, :b 26 :a 0})

  (col/as-rgba {:r 103, :g 39, :b 26 :a 0})

  )



#_(comment
  (def cursor-palette (r/cursor tunnl71.webpage.core/system [:palette]))

  @(r/reaction ((juxt :r :g :b :a) (hex->rgba (:background-color @cursor-palette))))

  (:col (col/as-css (apply col/rgba (map #(/ % 255) [@r @g @b @a]))))
  (:col (col/as-css (apply col/rgba (map #(/ % 255) (col/rgba (col/as-css ))))))

  (vals col/as-css (vals ))

  (col/as-css (col/rgba (vals (hex->rgba (:background-color @cursor-palette )))))
  (:col (col/as-css (col/rgba @r @g @b @a)))
  (hex->rgba "yellow")
  (col/as-css )

  (col/as-css {:r 2  :g 2  :b 2 :a 2 })
  (col/as-css (col/rgba 2 2 2 2))
  )

