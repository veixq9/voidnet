(ns symautre.system
  (:gen-class)
  (:require
   [org.httpkit.server]
   [symautre.router :as router]))

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

