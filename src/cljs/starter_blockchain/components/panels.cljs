(ns starter-blockchain.components.panels
  (:require
    [cljsjs.web3]
    [cljs-web3.core :as web3]
    [cljs-web3.eth  :as web3-eth]
    ["0x.js" :refer [ZeroEx]]
    [re-frame.core :refer [subscribe]]
    [starter-blockchain.router :as router]
    [starter-blockchain.use-web3.components :refer [use-web3]]))

(defn main-panel [state]
  (let [page-handler (-> state :active-page :handler)]
    [:div {}
     [:p [:a {:href (router/path-for :home)} "Home"]]
     [:p [:a {:href (router/path-for :view-1)} "Use web3 + 0x.js"]]
     [:p [:a {:href (router/path-for :view-2)} "view-2"]]
     (case page-handler
       :view-1 (use-web3 state)
       :view-2 [:p "View 2"]
       [:p "Home panel"])]))
