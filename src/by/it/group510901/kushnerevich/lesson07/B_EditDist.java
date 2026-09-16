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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int len1 = one.length();
        int len2 = two.length();

        // Создаем матрицу (len1+1) x (len2+1) для хранения расстояний
        // dp[i][j] - расстояние редактирования между первыми i символами строки one
        // и первыми j символами строки two
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Инициализация базовых случаев
        // Если одна строка пуста, то расстояние равно длине другой строки
        // (нужно вставить или удалить все символы)
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;  // удалить i символов из первой строки
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;  // вставить j символов в первую строку
        }

        // Заполняем матрицу dp
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // Если символы совпадают, стоимость не увеличивается
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе выбираем минимальную стоимость из трех операций:
                    // 1. Удаление символа из первой строки: dp[i-1][j] + 1
                    // 2. Вставка символа в первую строку: dp[i][j-1] + 1
                    // 3. Замена символа: dp[i-1][j-1] + 1
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int replace = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(delete, Math.min(insert, replace));
                }
            }
        }

        // Результат находится в правом нижнем углу матрицы
        int result = dp[len1][len2];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}