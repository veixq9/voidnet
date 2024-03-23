(ns symautre.system
  (:gen-class)
  (:require
   symautre.token
   [org.httpkit.server]
   [symautre.router :as router]
   symautre.cell
   [symautre.tools.core :as t :refer :all]
   ))

(defonce system (atom {:log nil}))

(defn init
  []
  (do
    (if (some? (:http @system)) ((:http @system)))
    (swap! system assoc :http
           #_(ring.adapter.jetty/run-jetty router/app {:port 3030})

           (org.httpkit.server/run-server
            router/app
            #_{:port 3030}
            {:server-header "http-kit", :ip "0.0.0.0", :queue-size 20480, :proxy-protocol :disable, :port 3030, :legacy-return-value? true, :thread 4, :worker-name-prefix "worker-", :max-ws 4194304, :max-body 8388608, :max-line 8192}))

    ;; (add-tap (fn [e] (swap! system update :log #(cons e %))))
    ))


(init)

(comment
  ::bar
  (namespace *ns*)
  &env
  ns*
  (init)
  system

  
  )

{:id "3525a0fd-78f9-494d-a090-314203c6bb95",
 :timestamp 1708838107223,
 :pillars [
           :pillar-profile
           :octagon-ring
           :octagon-profile
           (symautre.cell/cell (delete :live-cursors))
           
           [:soul-reaver :trovo :shit]
           
           ]
 :actor/public-key-ed25519
 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}

{:id "17efef13-7216-4316-a996-be38ea74b68d",
 :timestamp 1708857265088,
 :body ["UMI x Gendai No Kaishaku Restaurant"
        "restaurant asian"
        "micro wave"
        "μ hut"
        ]
 :actor/public-key-ed25519
 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"

 }


{:id "18b0f723-a02b-4107-a718-fc73ba42aa23",
 :timestamp 1708884156087,
 :body #{},
 :actor/public-key-ed25519
 "0xec077faa8a230c86aa0b958c137a25a1dadae77c69edca95d2a1d5045ba79ca8"}


