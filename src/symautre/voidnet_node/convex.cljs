(ns symautre.voidnet-node.convex
  (:require
   [symautre.tools.core :as t]
   ["@convex-dev/convex-api-js" :as cvx :refer  [API, Account, KeyPair]])
  )

(enable-console-print!)

(def state (atom nil))

(defn create-api
  [state]
  (swap! state assoc :api (cvx/default.API.create "https://convex.world")))

(defn create-account
  [state]
  (when (nil? (:api @state)) (create-api state))
  (-> (cvx/default.KeyPair.create)
      (.then (fn [kp]
               (swap! state assoc :kp kp)
               (js/console.log kp)
               (-> (:api @state)
                   (.createAccount kp)
                   (.then #(swap! state assoc :account %)))
               ))))


(defn get-balance
  [state]
  (-> (:api @state)
      (.getBalance (:account @state))
      (.then #(js/console.log %))))

(defn send
  [state code-string]
  (let [{:keys [api account]} @state]
    (-> api
        (.send code-string )
        (.then #(js/console.log %)))))

(comment
  (send state "(map inc [1 2 3])")
  (-> api
      (.send "(map inc [1 2 3])" account)
      (.then #(js/console.log %)))

  (get-balance state))

