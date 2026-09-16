package by.it.group510901.kushnerevich.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {

    // Переменная для замера времени выполнения
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        // Создаем экземпляр класса FiboA для первого замера
        FiboA fibo = new FiboA();
        int n = 33; // Задаем число для вычисления (33-й член последовательности)

        // Выводим результат обычного рекурсивного метода
        // %d - целое число, %n - перенос строки
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        // Создаем новый экземпляр для следующего замера (сброс таймера)
        fibo = new FiboA();
        n = 34; // Увеличиваем число для демонстрации медленности рекурсии

        // Выводим результат метода с BigInteger
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    // Метод для замера времени с момента последнего вызова
    private long time() {
        long res = System.currentTimeMillis() - startTime; // Разница с последним запуском
        startTime = System.currentTimeMillis(); // Сбрасываем таймер для следующего замера
        return res; // Возвращаем затраченное время в миллисекундах
    }

    // Обычный рекурсивный метод (ограничен типом int)
    private int calc(int n) {
        // Базовый случай рекурсии: F(0) = 0
        if (n == 0) return 0;

        // Базовый случай рекурсии: F(1) = 1
        if (n == 1) return 1;

        // Рекурсивный случай: F(n) = F(n-1) + F(n-2)
        // Из-за рекурсии время выполнения O(2^n) - очень медленно
        return calc(n - 1) + calc(n - 2);
    }

    // Рекурсивный метод с использованием BigInteger (без ограничения размера числа)
    BigInteger slowA(Integer n) {
        // Базовый случай: если n == 0, возвращаем BigInteger.ZERO (константа 0)
        if (n == 0) return BigInteger.ZERO;

        // Базовый случай: если n == 1, возвращаем BigInteger.ONE (константа 1)
        if (n == 1) return BigInteger.ONE;

        // Рекурсивный случай: складываем два предыдущих числа
        return slowA(n - 1).add(slowA(n - 2));
    }
}