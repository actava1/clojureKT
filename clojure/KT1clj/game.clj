(ns guess-number-game)

;; Атомы для хранения состояния игры
(def min-range (atom nil))
(def max-range (atom nil))
(def current-guess (atom nil))

(defn start [min max]
  "Инициализирует новую игру с заданным диапазоном"
  (reset! min-range min)
  (reset! max-range max)
  (reset! current-guess (quot (+ min max) 2))
  "Я готов...")

(defn guess-my-number []
  "Возвращает текущую догадку программы"
  @current-guess)

(defn smaller []
  "Показывает, что загаданное число меньше текущей догадки"
  (swap! max-range (fn [old-max] (dec @current-guess)))
  (reset! current-guess (quot (+ @min-range @max-range) 2))
  @current-guess)

(defn bigger []
  "Показывает, что загаданное число больше текущей догадки"
  (swap! min-range (fn [old-min] (inc @current-guess)))
  (reset! current-guess (quot (+ @min-range @max-range) 2))
  @current-guess)