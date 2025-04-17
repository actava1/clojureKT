(ns url-shortener.client
  (:require [clj-http.client :as client]
            [clojure.string :as str]))

(def base-url "http://localhost:3000")

(defn prompt [msg]
  (print msg)
  (flush)
  (read-line))

(defn create []
  (let [url   (prompt "Введите нормальный URL: ")
        resp   (client/post 
                 (str base-url "/normal-url")
                 {:content-type :json 
                  :accept :json 
                  :body         {:url url}
                  :as           :json})]
    (println "Короткий URL:" (:short-url (:body resp)))))

(defn show []
  (let [short   (prompt "Введите короткий URL: ")
        resp    (client/get 
                  (str base-url "/short-url/" short)
                  {:accept :json 
                   :throw-exceptions false 
                   :as           :json})]
    (if (= (:status resp) 200)
      (println "Нормальный URL:" (:url (:body resp)))
      (println "Ошибка:" (:error (:body resp))))))

(defn update []
  (let [short   (prompt "Введите короткий URL для изменения: ")
        newurl   (prompt "Введите новый нормальный URL: ")
        resp     (client/put 
                   (str base-url "/short-url/" short)
                   {:content-type :json 
                    :accept       :json 
                    :body         {:url newurl}
                    :throw-exceptions false 
                    :as           :json})]
    ;; Проверяем статус ответа
    ((if (= (:status resp) 200) println println)
     (:message (:body resp) (:error (:body resp))))))

(defn delete []
  (let [short   (prompt "Введите короткий URL для удаления: ")
        resp    (client/delete 
                  (str base-url "/short-url/" short)
                  {:accept       :json 
                   :throw-exceptions false 
                   :as           :json})]
    ((if (= (:status resp) 200) println println)
     (:message (:body resp) (:error (:body resp))))))

(defn menu []
  ;; Вывод меню и обработка выбора пользователя в цикле рекурсии
  ;; Возвращает nil при выходе из программы.
  
  ;; Можно использовать loop/recur или рекурсию.
  
  ;; Для удобства сделаем рекурсивную функцию:
  
(letfn [(loop-menu []
          ;; Вывод меню:
          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;

          ;;
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          

          

          

          

          

          

          

          

          

          

          

          

          

          

          

          

          

          

           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




  
  




   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

   

   

   

   

   

   

   

   

   

   

   

   

   

   

   

   

   

   

           
  
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         ]
    
    )
    
)
