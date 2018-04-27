(ns starter-blockchain.components.panels
  (:require
    [re-frame.core :refer [subscribe]]
    [starter-blockchain.router :as router]))

(defn main-panel [state]
  (let [page-handler (-> state :active-page :handler)]
    [:div {}
     [:p [:a {:href (router/path-for :home)} "Home"]]
     [:p [:a {:href (router/path-for :view-1)} "view-1"]]
     [:p [:a {:href (router/path-for :view-2)} "view-2"]]
     (case page-handler
       :view-1 [:p "View 1"]
       :view-2 [:p "View 2"]
       [:p "Home panel"])]))
