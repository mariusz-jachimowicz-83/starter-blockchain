(ns starter-blockchain.handler.web
  (:require
   [clojure.java.io :as io]
   [compojure.core :refer [context GET routes]]
   [compojure.route :as route]
   [integrant.core :as ig]))

(defmethod ig/init-key :starter-blockchain.handler/web [_ conf]
  (routes
   (GET "/" [] (io/resource "starter_blockchain/public/index.html"))
   (route/resources "/")
   (route/not-found "<h1>Not Found :(</h1>")))
