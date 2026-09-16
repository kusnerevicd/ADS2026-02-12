package by.it.group510901.kushnerevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {

    // Массив для мемоизации результатов
    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Инициализируем массив для мемоизации размером (len1+1) x (len2+1)
        // Значение -1 означает, что результат еще не вычислен
        memo = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        // Вызываем рекурсивную функцию
        int result = editDistRecursive(one, two, one.length(), two.length());

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивная функция для вычисления расстояния Левенштейна
    private int editDistRecursive(String one, String two, int i, int j) {
        // Базовые случаи
        if (i == 0) {
            // Если первая строка пуста, нужно вставить все символы второй строки
            return j;
        }
        if (j == 0) {
            // Если вторая строка пуста, нужно удалить все символы первой строки
            return i;
        }

        // Если результат уже вычислен, возвращаем его из мемоизации
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Если символы совпадают, рекурсивно вычисляем для строк без этих символов
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDistRecursive(one, two, i - 1, j - 1);
            return memo[i][j];
        }

        // Если символы разные, рассматриваем три операции:
        // 1. Удаление символа из первой строки (cost = 1 + расстояние для строк без этого символа)
        int delete = editDistRecursive(one, two, i - 1, j) + 1;

        // 2. Вставка символа в первую строку (cost = 1 + расстояние для строк без символа из второй)
        int insert = editDistRecursive(one, two, i, j - 1) + 1;

        // 3. Замена символа (cost = 1 + расстояние для строк без обоих символов)
        int replace = editDistRecursive(one, two, i - 1, j - 1) + 1;

        // Выбираем минимальную стоимость из трех операций
        memo[i][j] = Math.min(delete, Math.min(insert, replace));

        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}