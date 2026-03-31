package by.it.group510901.kushnerevich.lesson02;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        // Создаем экземпляр класса
        A_VideoRegistrator instance = new A_VideoRegistrator();

        // Массив событий (временные метки в произвольном порядке)
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};

        // Вызываем метод с длительностью работы камеры = 1
        List<Double> starts = instance.calcStartTimes(events, 1);

        // Выводим список моментов старта видеокамеры
        System.out.println(starts);
    }

    // Основной метод, реализующий жадный алгоритм
    List<Double> calcStartTimes(double[] events, double workDuration) {
        // events - события которые нужно зарегистрировать
        // workDuration - время работы видеокамеры после старта

        // Создаем список для хранения результатов (моментов старта)
        List<Double> result = new ArrayList<>();

        // Проверка на пустой массив или null
        if (events == null || events.length == 0) return result;

        // Сортируем события по времени возрастания
        Arrays.sort(events);

        int i = 0;           // Индекс текущего события в массиве
        int n = events.length; // Общее количество событий


        while (i < n) {
            // берем текущее необработанное событие
            double start = events[i];    // момент старта видеокамеры
            result.add(start);

            // Вычисляем момент окончания работы камеры
            double end = start + workDuration; // Камера работает workDuration времени


            while (i < n && events[i] <= end) {
                i++; //
            }
            // i указывает на первое событие, которое не попало в текущий интервал
        }

        return result; // Возвращаем список моментов включения камеры

        // Анализ сложности алгоритма:
        // i - это индекс события events[i]
        // hint: сортировка Arrays.sort обеспечит скорость алгоритма
        // C*(n log n) + C1*n = O(n log n) - это время выполнения
        // где:
        // - O(n log n) - сортировка массива
        // - O(n) - проход по массиву (цикл while)
        // Итоговая сложность: O(n log n)
    }
}