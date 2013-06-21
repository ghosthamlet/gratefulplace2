(ns gratefulplace.controllers.shared
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]))

(def author-inclusion-options
  {:author {:only [:id :username :gravatar]}})

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})

(def OK
  {:status 200})>

(def NOT-AUTHORIZED
  {:status 401})

(def NOT-FOUND
  {:status 404})

(defmacro id
  []
  '(str->int (:id params)))

(defmacro defmapifier
  [fn-name & mapify-args]
  `(defn- ~fn-name
     [id#]
     (if-let [ent# (db/ent id#)]
       (c/mapify
        ent#
        ~@mapify-args)
       nil)))


(defmacro validator
  "Used in malformed? which is why truth values are reversed"
  [params validation]
  `(fn [ctx#]
     (if-valid
      ~params ~validation errors#
      false
      [true {:errors errors#
             :representation {:media-type "application/json"}}])))

(defn exists?
  [record]
  (if record
    {:record record}))

(defn record-in-ctx
  [ctx]
  (get ctx :record))

(def exists-in-ctx? record-in-ctx)

(defn errors-in-ctx
  [ctx]
  {:errors (get ctx :errors)})

(defn delete-content
  [id]
  (db/t [{:db/id id
          :content/deleted true}]))