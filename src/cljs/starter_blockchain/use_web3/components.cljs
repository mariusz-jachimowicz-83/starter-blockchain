(ns starter-blockchain.use-web3.components
  (:require
    [cljsjs.web3]
    [cljs-web3.core :as web3]
    [cljs-web3.eth  :as web3-eth]
    ["0x.js" :refer [ZeroEx]]))

(defn use-web3 [state]
  (let [provides-web3? (boolean (aget js/window "web3"))
        web            (web3/create-web3 (:node-url "http:\\localhost:8545"))
        provider       (web3/current-provider web)
        web-connected  (web3/connected? web)
        testnet-cfg    (clj->js { :networkId 50 })
        all-accounts   (web3-eth/accounts web)

        _ (println "== all accounts by web3 == ")
        _ (println all-accounts)

        zx (ZeroEx. provider testnet-cfg)
        _ (println "== all accounts by ZeroEx")
        _ (-> (.getAvailableAddressesAsync zx)
              (.then #(-> % js->clj println)))]
    [:div {}
     [:p "All accounts - use web3 library"]
     [:p [:ul (->> all-accounts (map #(vector :li {:key %} %)))]]
     [:p "All accounts - by 0x library (see console)"]]))
