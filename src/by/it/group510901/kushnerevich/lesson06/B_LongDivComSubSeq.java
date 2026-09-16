package by.it.group510901.kushnerevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j+1]] делится на A[i[j]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] arr = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Решение методом динамического программирования
        // dp[i] - длина наибольшей кратной подпоследовательности,
        // заканчивающейся на элементе arr[i]
        int[] dp = new int[n];

        // Инициализация: каждый элемент сам по себе образует последовательность длины 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Заполняем массив dp
        // Для каждого i смотрим все предыдущие элементы j < i
        // Если arr[i] делится на arr[j] (arr[i] % arr[j] == 0),
        // то можем продолжить последовательность, заканчивающуюся на j
        // Тогда dp[i] = max(dp[i], dp[j] + 1)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем, делится ли текущий элемент на предыдущий
                // Важно: порядок важен - A[i[j+1]] делится на A[i[j]]
                // То есть следующий элемент должен делиться на предыдущий
                if (arr[i] % arr[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Находим максимальное значение в dp - это и есть ответ
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = Math.max(result, dp[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}