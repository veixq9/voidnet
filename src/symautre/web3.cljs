(ns symautre.web3
  (:require
   [reagent.core :as r]
   [symautre.tools.core :as t]))

(defn metamask-installed?
  []
  (some? (type js/window.ethereum)))

(defn get-accounts!
  []
  (-> (js/ethereum.request
       (cljs.core/clj->js {:method "eth_requestAccounts"}))
      (.then (fn [result]
               (println "accounts: " result)
               (tap> {:fn (fn [sys]
                            (swap! sys assoc
                                   :actor.evm/address (first result)))})
               result))))

(defn connect!
  "there is only connect!"
  []
  (get-accounts!))


(defn sign
  [msg from]
  (js/ethereum.request
   (cljs.core/clj->js {
                       ;; :id "1"
                       ;; :jsonrpc "2.0"
                       :method "personal_sign"
                       :params [
                                msg from]})))

(defn connect-btn
  []
  [:button#connect-wallet.btn-primary
   {
    :key "menu"
    :role "button"
    ;; :type "button"
    :class "btn-info"
    ;; :color "blue"
    :data-mdb-toggle "button"
    :on-click #(connect!)} "connect wallet!"])

(defn wallet
  [state]

  (r/with-let [addr (r/cursor state [:actor.evm/address])]
    (fn [state]
      [:div#wallet.w3-right
       (if (metamask-installed?)
         (do (connect!)
             (if @addr
               [:div
                [:span (str (subs @addr 0 7) "..." (t/subs @addr -6 -1))]]
               [connect-btn]))         
         [:p "install metamask!"])])))






