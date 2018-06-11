(ns starter-blockchain.use-web3.fx
  (:require
   [re-frame.core :as rf]
   [promesa.core  :as p]
   [promesa.async-cljs :refer-macros [async]]
   [cljsjs.web3]
   [cljs-web3.core :as web3]
   [cljs-web3.eth  :as web3-eth]
   ["0x.js" :refer [ZeroEx]]))

(def net-url "http:\\localhost:8545")

;; COFX - usefull for init or set something each time before handling some event
;;        (before event-fx)
(rf/reg-cofx
 :web3-init
 (fn [cofx _]
   (let [Web3     (web3/create-web3 (:node-url "http:\\localhost:8545"))
         provider (web3/current-provider Web3)]
     (assoc cofx
            :Web3     Web3
            :provider provider))))

;; FX - handle event of some type
;;    - each FX is getting 2 default COFX injected and available -> db and event name
(rf/reg-event-fx
 :accounts-by-web3
 (fn [{:keys [db] :as cofx} _]
   {:db (assoc db :all-accounts-by-web3 (web3-eth/accounts (:Web3 db)))}))

(rf/reg-event-fx
 :get-accounts
 [(rf/inject-cofx :web3-init)]
 (fn [{:keys [db Web3 provider] :as cofx} _]
   {:db (assoc db
               :Web3 Web3
               :provider provider)
    :dispatch-n [[:accounts-by-web3]
                 [:zeroEx-init]]}))

(rf/reg-event-db
 :set-accounts-by-0x
 (fn [db [_ accounts]]
   (assoc db :all-accounts-by-0x accounts)))

(rf/reg-event-fx
 :accounts-by-zeroEx
 (fn [{:keys [db] :as cofx} _]
   (-> db
       :zx
       (.getAvailableAddressesAsync)
       (p/then #(-> % js->clj))
       (p/then #(rf/dispatch [:set-accounts-by-0x %])))
   {}))

(rf/reg-event-fx
 :zeroEx-init
 (fn [{:keys [db] :as cofx} _]
   {:db (assoc db :zx (ZeroEx. (:provider db)
                               (clj->js {:networkId 50})))
    :dispatch [:accounts-by-zeroEx]}))
