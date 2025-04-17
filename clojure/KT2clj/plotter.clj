(ns plotter.core
  (:require [clojure.string :as str]))

;; Типы и константы

(defrecord Position [x y])

(defn position [x y]
  {:x x :y y})

(def carriage-up :up)
(def carriage-down :down)

(def line-colors {:black "чёрный"
                  :red "красный"
                  :green "зелёный"})

;; Печать (Printer)
(defn printer [s]
  (println s))

;; Вспомогательные функции

(defn draw-line [prt from to color]
  (prt (format "...Чертим линию из (%d, %d) в (%d, %d) используя %s цвет."
               (:x from) (:y from) (:x to) (:y to) (get line-colors color))))

(defn calc-new-position [distance angle current]
  ;; angle в градусах, преобразуем в радианы
  (let [angle-rads (* angle (/ Math/PI 180.0))
        x (+ (:x current) (* distance (Math/cos angle-rads)))
        y (+ (:y current) (* distance (Math/sin angle-rads)))]
    {:x (Math/round x)
     :y (Math/round y)}))

;; Команды плоттера

(defn move [prt distance state]
  (let [new-pos (calc-new-position distance (:angle state) (:position state))]
    (if (= (:carriage-state state) carriage-down)
      (draw-line prt (:position state) new-pos (:color state))
      (prt (format "Передвигаем на %d от точки (%d, %d)"
                   distance (:x (:position state)) (:y (:position state)))))
    ;; Возвращаем новое состояние с обновлённой позицией
    (assoc state :position new-pos)))

(defn turn [prt angle state]
  (prt (format "Поворачиваем на %d градусов" angle))
  ;; Угол по модулю 360
  (let [new-angle (mod (+ (:angle state) angle) 360)]
    (assoc state :angle new-angle)))

(defn carriage-up-fn [prt state]
  (prt "Поднимаем каретку")
  (assoc state :carriage-state carriage-up))

(defn carriage-down-fn [prt state]
  (prt "Опускаем каретку")
  (assoc state :carriage-state carriage-down))

(defn set-color [prt color state]
  ;; color - ключ из line-colors
  (prt (format "Устанавливаем %s цвет линии." (get line-colors color)))
  (assoc state :color color))

(defn set-position [prt position state]
  ;; position - map с :x и :y
  (prt (format "Устанавливаем позицию каретки в (%d, %d)." (:x position) (:y position)))
  (assoc state :position position))

;; Функции для черчения фигур

(defn draw-triangle [prt size state]
  ;; Опускаем каретку
  (let [state1 (carriage-down-fn prt state)]
    ;; Рисуем три стороны с поворотом на 120 градусов
    (loop [i 0 st state1]
      (if (< i 3)
        (recur 
          (inc i)
          ;; move + turn
          ((fn [s] 
             ((fn [s2] 
                turn prt 120 s2)
              ((fn [s1] 
                 move prt size s1))
              s)) st))
        ;; Поднимаем каретку после рисования
        (carriage-up-fn prt st)))))

(defn draw-square [prt size state]
  ;; Опускаем каретку
  (let [state1 (carriage-down-fn prt state)]
    ;; Рисуем четыре стороны с поворотом на 90 градусов
    (loop [i 0 st state1]
      (if (< i 4)
        (recur 
          (inc i)
          ((fn [s] 
             ((fn [s2] 
                turn prt 90 s2)
              ((fn [s1] 
                 move prt size s1))
              s)) st))
        ;; Поднимаем каретку после рисования
        (carriage-up-fn prt st)))))

;; Инициализация состояния плоттера

(def init-position {:x 0 :y 0})
(def init-color :black)
(def init-angle 0.0)
(def init-carriage-state carriage-up)

(def initial-state {:position init-position
                    :angle init-angle
                    :color init-color
                    :carriage-state init-carriage-state})

;; Пример использования

(defn -main []
  ;; Начальное состояние
  (let [state0 initial-state
        ;; Рисуем треугольник со стороной 100
        state1   (draw-triangle printer 100.0 state0)
        ;; Устанавливаем позицию в {10,10}
        state2   (set-position printer {:x 10 :y 10} state1)
        ;; Устанавливаем красный цвет линии
        state3   (set-color printer :red state2)
        ;; Рисуем квадрат со стороной 80
        final-statesquare   (draw-square printer 80.0 state3)]
    final-statesquare))

;; Запуск main при необходимости:
;; (-main)
