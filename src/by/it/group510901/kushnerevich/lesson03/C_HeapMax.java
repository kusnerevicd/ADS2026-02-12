package by.it.group510901.kushnerevich.lesson03;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

//      Sample Input:
//      6
//      Insert 200
//      Insert 10
//      ExtractMax
//      Insert 5
//      Insert 500
//      ExtractMax
//
//      Sample Output:
//      200
//      500


public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        // Читаем входные данные из файла dataC.txt
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();

        // Находим максимальное значение среди всех извлеченных элементов
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    // Эта процедура читает данные из файла и обрабатывает команды
    // Возвращает максимальное значение среди всех извлеченных элементов
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;          // Переменная для хранения максимального извлеченного значения
        MaxHeap heap = new MaxHeap(); // Создаем экземпляр max-кучи

        // Читаем данные из входного потока
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt(); // Читаем количество операций

        // Обрабатываем каждую операцию
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine(); // Читаем строку с командой

            // Обработка команды ExtractMax
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax(); // Извлекаем максимальный элемент
                if (res != null && res > maxValue) maxValue = res; // Обновляем максимум
                System.out.println(); // Выводим пустую строку для форматирования
                i++;
            }

            // Обработка команды Insert
            if (s.contains(" ")) {
                String[] p = s.split(" "); // Разделяем команду и значение
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1])); // Вставляем значение в кучу
                i++;
            }
        }
        return maxValue; // Возвращаем максимальное извлеченное значение
    }

    // Внутренний класс, реализующий max-кучу (пирамиду)
    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        // Хранилище элементов кучи в виде ArrayList
        // Индексация: для элемента с индексом i:
        // - левый потомок: 2*i + 1
        // - правый потомок: 2*i + 2
        // - родитель: (i-1)/2
        private List<Long> heap = new ArrayList<>();


        int siftDown(int i) {
            int size = heap.size(); // Текущий размер кучи

            // Продолжаем, пока у элемента есть хотя бы один потомок
            while (2 * i + 1 < size) {
                int left = 2 * i + 1;   // Индекс левого потомка
                int right = 2 * i + 2;  // Индекс правого потомка
                int largest = i;        // Предполагаем, что текущий элемент наибольший

                // Сравниваем с левым потомком
                if (heap.get(left) > heap.get(largest)) {
                    largest = left;
                }

                // Сравниваем с правым потомком (если существует)
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }

                // Если наибольший элемент не текущий, меняем их местами
                if (largest != i) {
                    swap(i, largest);  // Обмениваем элементы
                    i = largest;       // Переходим к новому индексу для продолжения просеивания
                } else {
                    break; // Элемент на своем месте, завершаем
                }
            }
            return i; // Возвращаем конечную позицию элемента
        }

        /**
         * Вспомогательный метод для обмена двух элементов в куче
         * @param i индекс первого элемента
         * @param j индекс второго элемента
         */
        private void swap(int i, int j) {
            Long tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }

        /**
         * Просеивание элемента ВВЕРХ (sift up)
         * Используется после вставки нового элемента
         * @param i индекс элемента, который нужно просеять вверх
         * @return новый индекс элемента после просеивания
         */
        int siftUp(int i) {
            // Продолжаем, пока не дошли до корня и текущий элемент больше родителя
            while (i > 0) {
                int parent = (i - 1) / 2; // Индекс родительского узла

                // Если текущий элемент больше родителя, меняем их местами
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent; // Переходим к родителю для продолжения проверки
                } else {
                    break; // Свойство кучи восстановлено, завершаем
                }
            }
            return i; // Возвращаем конечную позицию элемента
        }

        /**
         * Вставка нового элемента в кучу
         * @param value значение для вставки
         */
        void insert(Long value) {
            heap.add(value);              // Добавляем элемент в конец массива
            siftUp(heap.size() - 1);     // Просеиваем его вверх для восстановления свойств кучи
        }

        /**
         * Извлечение максимального элемента из кучи
         * @return максимальный элемент или null, если куча пуста
         */
        Long extractMax() {
            if (heap.isEmpty()) return null; // Если куча пуста, возвращаем null

            Long max = heap.get(0);              // Максимальный элемент всегда в корне (индекс 0)
            Long last = heap.remove(heap.size() - 1); // Удаляем последний элемент

            // Если куча не стала пустой после удаления последнего элемента
            if (!heap.isEmpty()) {
                heap.set(0, last);   // Перемещаем последний элемент в корень
                siftDown(0);         // Просеиваем его вниз для восстановления свойств кучи
            }

            System.out.print(max + " "); // Выводим извлеченный элемент (как в примере)
            return max;                  // Возвращаем максимальный элемент
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }
}