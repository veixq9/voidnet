(ns symautre.slider
  (:require [thi.ng.color.core :as col]
            [reagent.core :as r]))

(def hex->rgba (fn [hex] (reduce-kv (fn [acc k v] (assoc acc k (* v 255))) {} (col/as-rgba (col/hex->int hex)))))
(defn rgba-map->rgba-string
  "does not include alpha!!!"
  [map-rgba]
  (:col (col/as-css (apply col/rgba (map #(/ % 255) (map (partial get map-rgba) [:r :g :b :a]))))))

(defn rgba->hex
  "does not include alpha!!!"
  [rgba]
  ;; (:col (col/as-css (col/as-int24 (col/css "rgba(92,50,156,0.7098039215686275)"))))
  ;; (col/as-int24 (col/css (col/->CSS rgba)))
  (cond
    (string? rgba) (:col (col/as-css (col/as-int24 (col/css rgba))))
    :default
    (rgba->hex (:col rgba)))
  ;; (:col (col/as-css (apply col/rgba (map #(/ % 255) (map (partial get map-rgba) [:r :g :b :a])))))
  )

(defn slider-input
  [color-element]
  (fn [color-element]
    [:input
     {:type "range" :value @color-element :min 0 :max 255
      :style {:width "100%"}
      :on-change (fn [e]
                   (let [new-value (js/parseInt (.. e -target -value))]
                     (reset! color-element new-value)))}]))

(defn slider
  "rgba"
  [state]
  (r/with-let [this (r/cursor state [:slider])
               hex (r/cursor this [:hex])
               rgba (r/cursor this [:rgba])
               ]
    (fn [state]
      (when (nil? @hex) (reset! hex "#0" ))
      
      (reset! rgba (hex->rgba @hex))

      
      
      (r/with-let [
                   r (r/cursor rgba [:r])
                   g (r/cursor rgba [:g])
                   b (r/cursor rgba [:b])
                   a (r/cursor rgba [:a])

                   hex (r/cursor state [:slider :hex])]
        ;; NOTE: does not take into account alpha!
        (r/track! #(reset! hex
                           (rgba->hex (rgba-map->rgba-string {:r @r :g @g :b @b :a @a}))
                           ;; (rgba->hex {:r @r :g @g :b @b :a @a})
                           ))

        (fn [state]
          [:div.w3-container {:width "300%" :clear :both}

           (into [:div.w3-row]
                 (map #(identity [:div.w3-cell
                                  [:span {:style {:color %1 :margin-right "10px"}} (str %2)]]) ["red" "green" "blue" "white"] [@r @g @b @a]))
           [slider-input (r/cursor rgba [:r])]
           [slider-input (r/cursor rgba [:g])]
           [slider-input (r/cursor rgba [:b])]
           [slider-input (r/cursor rgba [:a])]
           [:span @hex]
           ])))))

(comment

  (def c {:r 0 :g 0 :b 0 :a 0})

  (col/css c)

  (rgba->hex (rgba->hex-false c))
  (rgba->hex (rgba-map->rgba-string c))
  )

(defn randomize
  "rgba"
  [state]
  (let [slider-cursor (r/cursor state [:slider])]
    (fn [state]
      [:button.w3-border.w3-round.w3-container.w3-btn
       {:on-click
        (fn [] (tap> #(swap! slider-cursor assoc-in [:rgba]
                             ;; merge
                             ;; #_(hex->rgba (str "#" (.toString (rand-int (inc 0xffffff)) 16)))
                             (zipmap [:r :g :b :a] [(rand-int 256) (rand-int 256) (rand-int 256) (rand-int 256) ])
                             
                             )))}
       "randomize"])))

(defn slider-button
  "rgba"
  [state & more]
  (let [id "theme"]
    (fn [state & more]
      [:button.w3-border.w3-round.w3-container.w3-btn
       {:id id
        :on-click
        #(tap> (fn[state] (swap! state update-in [:selected]
                                 (fn [selected] (if (= id (:id selected))
                                                  nil
                                                  {:id id
                                                   :controls
                                                   [:div
                                                    [slider state]
                                                    [randomize state]]})))))}
       more])))

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

