package by.it.group510901.kushnerevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            //если концы пришли в обратном порядке, меняем их местами
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // 1. Сортируем отрезки с помощью оптимизированной быстрой сортировки с 3-разбиением
        //    и элиминацией хвостовой рекурсии
        quickSort3Way(segments, 0, segments.length - 1);

        // 2. Для каждой точки определяем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, который может содержать точку
            // (первый отрезок с start <= point, и при этом stop >= point)
            int firstIndex = findFirstMatchingSegment(segments, point);

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Находим последний отрезок, который содержит точку
            // (ищем последний отрезок с stop >= point среди тех, у кого start <= point)
            int lastIndex = findLastMatchingSegment(segments, point, firstIndex);

            // Количество подходящих отрезков = lastIndex - firstIndex + 1
            result[i] = lastIndex - firstIndex + 1;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением (Dutch National Flag algorithm)
    // и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            // Выбираем опорный элемент (медиану из трех для улучшения производительности)
            int pivotIndex = medianOfThree(arr, low, high);
            swap(arr, pivotIndex, low);
            Segment pivot = arr[low];

            // 3-разбиение: элементы меньше опорного, равные опорному, больше опорного
            int lt = low;      // индекс последнего элемента меньше опорного
            int i = low + 1;   // текущий индекс для просмотра
            int gt = high;     // индекс первого элемента больше опорного

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(arr, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }

            // Рекурсивно сортируем левую и правую части
            // Используем элиминацию хвостовой рекурсии - сортируем меньшую часть рекурсивно,
            // а большую - итеративно

            int leftSize = lt - low;
            int rightSize = high - gt;

            if (leftSize < rightSize) {
                quickSort3Way(arr, low, lt - 1);  // рекурсивно сортируем левую часть
                low = gt + 1;                      // продолжаем с правой частью итеративно
            } else {
                quickSort3Way(arr, gt + 1, high); // рекурсивно сортируем правую часть
                high = lt - 1;                    // продолжаем с левой частью итеративно
            }
        }
    }

    // Выбор медианы из трех элементов для улучшения производительности
    private int medianOfThree(Segment[] arr, int low, int high) {
        int mid = low + (high - low) / 2;

        if (arr[mid].compareTo(arr[low]) < 0) {
            if (arr[high].compareTo(arr[low]) < 0) {
                return low;  // low - медиана
            } else if (arr[high].compareTo(arr[mid]) < 0) {
                return high; // high - медиана
            } else {
                return mid;  // mid - медиана
            }
        } else {
            if (arr[high].compareTo(arr[mid]) < 0) {
                return mid;  // mid - медиана
            } else if (arr[high].compareTo(arr[low]) < 0) {
                return high; // high - медиана
            } else {
                return low;  // low - медиана
            }
        }
    }

    // Вспомогательный метод для обмена элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск первого отрезка, который содержит точку
    // Возвращает индекс первого отрезка с start <= point, у которого stop >= point
    private int findFirstMatchingSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        // Сначала находим первый отрезок с start <= point
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                right = mid - 1;  // ищем более левый
            } else {
                left = mid + 1;
            }
        }

        if (result == -1) {
            return -1; // нет отрезков, начинающихся раньше точки
        }

        // Теперь среди отрезков с start <= point ищем первый, у которого stop >= point
        // Начиная с найденного индекса, идем влево до первого подходящего
        int firstMatch = -1;
        for (int i = result; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) {
                firstMatch = i;
                break;
            }
        }

        return firstMatch;
    }

    // Бинарный поиск последнего отрезка, который содержит точку
    private int findLastMatchingSegment(Segment[] segments, int point, int startFrom) {
        // Бинарный поиск среди отрезков от startFrom до конца
        // ищем последний отрезок с stop >= point среди тех, у кого start <= point
        int left = startFrom;
        int right = segments.length - 1;
        int result = -1;

        // Сначала находим правую границу отрезков с start <= point
        int lastWithStartLE = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                lastWithStartLE = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (lastWithStartLE == -1) {
            return -1;
        }

        // Теперь среди отрезков [startFrom, lastWithStartLE] ищем последний с stop >= point
        left = startFrom;
        right = lastWithStartLE;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].stop >= point) {
                result = mid;
                left = mid + 1;  // ищем более правый
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            //подумайте, что должен возвращать компаратор отрезков
            // Сравниваем сначала по началу, потом по концу
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }

}