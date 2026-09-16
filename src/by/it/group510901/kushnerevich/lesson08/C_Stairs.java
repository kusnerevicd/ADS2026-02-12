package by.it.group510901.kushnerevich.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //общая длина последовательности
        int n = scanner.nextInt();
        int[] stairs = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        // Если нет ступенек
        if (n == 0) {
            return 0;
        }

        // Если одна ступенька
        if (n == 1) {
            return stairs[0];
        }

        // Создаем массив для динамического программирования
        // dp[i] - максимальная сумма при достижении i-й ступеньки
        int[] dp = new int[n];

        // База динамики
        dp[0] = stairs[0];                    // на первую ступеньку можно только запрыгнуть
        dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);  // на вторую ступеньку можно с первой или с земли

        // Заполняем остальные ступеньки
        for (int i = 2; i < n; i++) {
            // На i-ю ступеньку можно попасть:
            // 1) с (i-1)-й ступеньки (шаг на 1)
            // 2) с (i-2)-й ступеньки (шаг на 2)
            // Выбираем максимальную сумму и добавляем текущую ступеньку
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        int result = dp[n - 1];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}