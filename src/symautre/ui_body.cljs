(ns symautre.ui-body
  (:require
   [reagent.core :as r]))

(defn content
  [state]
  (let [data (r/cursor state [:data])]
    (fn [state]
      (into [:div]
            (interpose [:div [:br ][:hr]]
                       (for [{:keys [title media body timestamp]} @data]
                         [:div {:id title :key title}
                          [:h1.w3-h1 title]
                          [:span {:style {:font-style :italic}} (subs (str timestamp) 3 24)]
                          
                          (into [:div {:style {:white-space :pre-line}}] (for [x body]
                                                                           [:p x]))]))))))  

(def links [{:url "https://www.tumblr.com/blog/arrowsfrom"
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

(defn post-titles
  [state]
  (let [data (r/cursor state [:data])]
    (fn [state]
      (println "rerendering posts")
      (into [:div]
            (for [p @data]
              [:a.w3-conatiner {:href (str "#" (:title p))}
               [:div.w3-h2
                [:span (:title p)]
                [:span "   "]
                [:span {:style {:font-style :italic}} (subs (str (:timestamp p)) 3 24)]]])))))

(defn settings
  [state]
  (let [dropdown-hide? (r/atom false)
        settings-state (r/cursor state [:settings])]
    (fn [state]
      [:div 
       [:h1.w3-h1 "settings"]
       [:button.w3-btn.w3-border-white.w3-border.w3-round
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
         [:span.w3-margin "scroll"]]]
       ])))

(defn body
  [state]
  (fn [state]
    [:div.w3-container #_{:style { :font-size "200%"


                                  
                                  }}

     
     ;; [:a {:href "/posts.edn"} "posts"]
     [:div.w3-row {:id "top"} [:h1.w3-center.w3-border [:a {:href "#top" :style {:text-decoration "none"}} "VOIDNET"]]]
     [:div.w3-cell-row

      [:div.w3-cell.w3-container.w3-left {:style {:width "100%"}}
       [settings state]
       #_[indexes]
       #_[:div.w3-bar
          [:h1.w3-h1 "settings"]
          [:h1.w3-bar-item "foo"]
          [:button.w3-button "foo"]
          ]]

      [:div.w3-container.w3-cell {:style {:min-width "50%" :max-width "30px"}}
       [content state]]
      
      [:div.w3-cell.w3-container.w3-border-left {:style {:width "20%" :max-width "30px"}}
       [:h1.w3-h1 "posts"]
       [post-titles state]
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
