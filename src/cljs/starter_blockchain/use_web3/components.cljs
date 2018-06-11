(ns starter-blockchain.use-web3.components
  (:require
    [re-frame.core :refer [dispatch]]
    [promesa.core :as p]
    [promesa.async-cljs :refer-macros [async]]
    [cljsjs.web3]
    [cljs-web3.core :as web3]
    [cljs-web3.eth  :as web3-eth]
    ["0x.js" :refer [ZeroEx]]
    [starter-blockchain.use-web3.fx]))

(defn accounts-list [accounts]
  [:ul (map #(vector :li {:key %} %)
            accounts)])

(defn use-web3 [state]
  (let [_ (if-not (:Web3 state) (dispatch [:get-accounts]))
        all-by-web3 (:all-accounts-by-web3 state)
        all-by-0x   (:all-accounts-by-0x state)]
    [:div {}
     [:p "All accounts - get by web3 library"]
     (if all-by-web3
       [accounts-list all-by-web3]
       [:p "...Loading"])
     [:p "All accounts - get by 0x.js library"]
     (if all-by-0x
       [accounts-list all-by-0x]
       [:p "... Loading"])]))
