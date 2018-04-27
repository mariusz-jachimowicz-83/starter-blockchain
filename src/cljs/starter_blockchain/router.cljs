(ns starter-blockchain.router
  (:require
    [clojure.string  :as string]
    [accountant.core :as accountant]
    [bidi.bidi       :as bidi]
    [cemerick.url    :as url]
    [medley.core     :as medley]
    [re-frame.core   :as re-frame])
  (:import (goog Uri)))

(def routes
  ["/" [["home"   :home]
        ["view-1" :view-1]
        ["view-2" :view-2]]])

(defn path-for [& args]
  (str "#" (apply bidi/path-for routes args)))

(defn current-location-hash []
  (let [hash (-> js/document
                 .-location
                 .-hash
                 (string/split #"\?")
                 first
                 (string/replace "#" ""))]
    (if (empty? hash) "/" hash)))

(defn set-location-hash! [s]
  (set! (.-hash js/location) s))

(defn current-url []
  (url/url (string/replace (.-href js/location) "#" "")))

(defn set-location-query! [query-params]
  (set-location-hash!
    (str "#" (current-location-hash)
         (when-let [query (url/map->query query-params)]
           (str "?" query)))))

#_(defn add-to-location-query! [query-params]
  (let [current-query (:query (current-url))
        new-query (merge current-query (->> query-params
                                         (medley/map-keys constants/keyword->query)
                                         (medley/remove-keys nil?)))]
    (set-location-query! new-query)))

#_(defn current-url-query []
  (->> (:query (current-url))
    (medley/map-keys (set/map-invert ethlance.constants/keyword->query))
    (medley/remove-keys nil?)
    (map (fn [[k v]]
           (if-let [f (constants/query-parsers k)]
             {k (f v)}
             {k v})))
    (into {})))

(defn nav-to! [route route-params]
  (set-location-hash! (medley/mapply path-for route route-params)))

(defn match-current-location []
  (bidi/match-route routes (current-location-hash)))

(defn dispatch-current-path! []
  (accountant/dispatch-current!))

(defn go-to [path]
  (accountant/navigate! path))

(defn current-path []
  (.getPath (.parse Uri js/window.location)))

(defn init []
  (accountant/configure-navigation!
   {:nav-handler identity
    :path-exists? (fn [path]
                    (boolean (bidi/match-route routes path)))}))

;; re-frame specific
(defn set-page-after-browser-url-change []
  (set! (.-onhashchange js/window)
        #(re-frame/dispatch [:set-active-page (match-current-location)])))
