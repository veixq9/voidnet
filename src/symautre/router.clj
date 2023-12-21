(ns symautre.router
  (:require
   [reitit.ring :as ring]
   [symautre.ipfs :as ipfs]))

;; ================= router ========================================
(def app
  (ring/ring-handler
   (ring/router
    [
     ["/api"
      ["/v0"
       ["/add" {:post {
                        :handler (fn [req]
                                   (println req)
                                   (println "(:body req) => " (:body req))
                                   (let [slurped (slurp (:body req))
                                         add-result (ipfs/add slurped)]
                                     (println "(slurp (:body req))=> " slurped)
                                     (println "add result " add-result)
                                     {:status 200
                                      :body add-result
                                      }))
}}]]]
     ["/*" (ring/create-resource-handler {
                                          :root "public/voidnet"
} )]]
    {:conflicts nil})))
















