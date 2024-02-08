(ns symautre.some-app.shadow
  (:require [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server.dev-http]
            ;; [clojure.java.shell :refer [sh]]
            ))

(do
  (shadow/watch :voidnet)
  (shadow/repl :voidnet)
  ;; (shadow/browser-repl :voidnet)
  )

"https://code.thheller.com/blog/shadow-cljs/2017/10/14/bootstrap-support.html"

(do
  (shadow/watch :voidnet-node)
  (shadow/node-repl {:build-id :voidnet-node })
  (shadow/repl  :voidnet-node )
  ;; (shadow/browser-repl :voidnet)
  )


(comment
  
  (defn new-tickChanneli
    [url]
    (let url []))

  (new-tickChannel :url "ipfs-atom://oeabfnaukf" :buffer-size )

  (shadow.cljs.devtools.server.dev-http/start   )

  shadow/dev

  (shadow/watch :browser)
  (shadow/browser-repl :browser))



