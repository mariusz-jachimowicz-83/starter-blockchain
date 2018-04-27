(ns starter-blockchain.components.panels
  (:require
    [cljsjs.web3]
    [cljs-web3.core :as web3]
    [cljs-web3.eth :as web3-eth]
    #_[cljsjs.zero-ex]
    [ZeroEx]

    [re-frame.core :refer [subscribe]]
    [starter-blockchain.router :as router]))

(defn main-panel [state]
  (let [page-handler (-> state :active-page :handler)
        provides-web3? (boolean (aget js/window "web3"))
        web            (web3/create-web3 (:node-url "http:\\localhost:8545"))
        provider      (web3/current-provider web)
        web-connected (web3/connected? web)
        testnet-cfg (clj->js  { :networkId 50 })
        _ (println "== all accounts ==")
        _ (println (web3-eth/accounts web))
        _ (println "== init 0x")
        zx (js/ZeroEx.ZeroEx provider testnet-cfg)]
    [:div {}
     [:p [:a {:href (router/path-for :home)} "Home"]]
     [:p [:a {:href (router/path-for :view-1)} "view-1"]]
     [:p [:a {:href (router/path-for :view-2)} "view-2"]]
     (case page-handler
       :view-1 [:p "View 1"]
       :view-2 [:p "View 2"]
       [:p "Home panel"])]))
