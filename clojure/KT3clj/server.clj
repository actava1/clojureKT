(ns url-shortener.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response status]]
            [clojure.string :as str]))

;; Хранилище: атом с map short-url -> normal-url
(def db (atom {}))

;; Генерация короткого URL (простой пример - случайная строка из 6 символов)
(defn gen-short-url []
  (let [chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"]
    (apply str (repeatedly 6 #(rand-nth chars)))))

;; Обработчики

(defroutes app-routes

  ;; POST /normal-url {"url": "http://example.com"}
  (POST "/normal-url" {body :body}
    (let [normal-url (:url body)
          short-url (gen-short-url)]
      (swap! db assoc short-url normal-url)
      (response {:short-url short-url})))

  ;; GET /short-url/:short
  (GET "/short-url/:short" [short]
    (if-let [normal (@db short)]
      (response {:url normal})
      (status (response {:error "Not found"}) 404)))

  ;; PUT /short-url/:short/:newurl
  ;; Для удобства принимаем новый url в теле JSON {"url": "..."}
  ;; Но по условию можно и в пути, здесь сделаем через тело запроса
  (PUT "/short-url/:short" {params :params body :body}
    (let [short (:short params)
          new-url (:url body)]
      (if (@db short)
        (do
          (swap! db assoc short new-url)
          (response {:message "Updated successfully"}))
        (status (response {:error "Not found"}) 404))))

  ;; DELETE /short-url/:short
  (DELETE "/short-url/:short" [short]
    (if (@db short)
      (do
        (swap! db dissoc short)
        (response {:message "Deleted successfully"}))
      (status (response {:error "Not found"}) 404)))

  ;; Статические или несуществующие маршруты
  (route/not-found {:error "Not found"}))

(def app
  ;; Оборачиваем middleware для JSON тела и ответа
  (-> app-routes
      wrap-json-body
      wrap-json-response))

(defn -main [& args]
  ;; Запускаем сервер на порту 3000
  (println "Starting server on port 3000")
  (run-jetty app {:port 3000}))