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
                                         _ (println "(slurp (:body req))=> " slurped)
                                         add-result (ipfs/add slurped)]
                                     
                                     (println "add result " add-result)
                                     {:status 200
                                      :body (pr-str add-result)
                                      }))
}}]]]
     ["/*" (ring/create-resource-handler {
                                          :root "public/voidnet"
} )]]
    {:conflicts nil})))
















