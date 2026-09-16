package by.it.group510901.kushnerevich.lesson05;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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

        // 1. Сортируем отрезки по началу с помощью быстрой сортировки
        quickSort(segments, 0, segments.length - 1);

        // 2. Для каждой точки определяем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Находим первый отрезок с start <= point (бинарный поиск)
            int firstIndex = findFirstSegmentWithStartLessOrEqual(segments, point);
            if (firstIndex == -1) {
                result[i] = 0;  // нет отрезков, которые начинаются раньше или в точке
                continue;
            }

            // Находим последний отрезок с start <= point (бинарный поиск)
            int lastIndex = findLastSegmentWithStartLessOrEqual(segments, point);

            // Среди найденных отрезков считаем те, у которых stop >= point
            for (int j = firstIndex; j <= lastIndex; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка массива отрезков
    private void quickSort(Segment[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Разбиение массива для быстрой сортировки
    private int partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];  // опорный элемент
        int i = low - 1;  // индекс для элементов меньше опорного

        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                // меняем местами arr[i] и arr[j]
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // ставим опорный элемент на правильное место
        Segment temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // Поиск первого отрезка с start <= point (бинарный поиск)
    private int findFirstSegmentWithStartLessOrEqual(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;      // нашли кандидата
                right = mid - 1;   // ищем более левый
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    // Поиск последнего отрезка с start <= point (бинарный поиск)
    private int findLastSegmentWithStartLessOrEqual(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;      // нашли кандидата
                left = mid + 1;    // ищем более правый
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
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке (уже сделано при чтении)
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            // сравниваем сначала по началу, если начала равны - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}