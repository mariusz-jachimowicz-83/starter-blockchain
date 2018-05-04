(ns starter-blockchain.dev
  (:require
    #_[figwheel.client :as fw]
    [starter-blockchain.core]))

(enable-console-print!)

#_(fw/start {:on-jsload starter-blockchain.core/init
           :websocket-url "ws://localhost:3449/figwheel-ws"})

;; display Hello World
#_(set! (.-innerText (.getElementById js/document "app")) "Hello World :)")
