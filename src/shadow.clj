(ns symautre.some-app.shadow
  (:require [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server.dev-http]
            [clojure.java.shell :refer [sh]]))


(do
  
  (shadow/watch :voidnet)
  (shadow/repl :voidnet)
  ;; (shadow/browser-repl :voidnet)

  )


(shadow.cljs.devtools.server.dev-http/start   )

shadow/dev

(shadow/watch :browser)
(shadow/browser-repl :browser)

