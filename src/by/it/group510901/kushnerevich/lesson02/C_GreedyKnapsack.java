package by.it.group510901.kushnerevich.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортируем предметы по убыванию удельной стоимости (cost/weight)
        Arrays.sort(items, (a, b) -> Double.compare(
                (double)b.cost / b.weight,
                (double)a.cost / a.weight
        ));

        double result = 0;
        int remainingWeight = W;

        // Жадный алгоритм: берем предметы с максимальной удельной стоимостью
        for (Item item : items) {
            if (remainingWeight >= item.weight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Взяли целиком: %s (осталось места: %d)\n", item, remainingWeight);
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Взяли часть (%.2f) от %s (осталось места: 0)\n", fraction, item);
                break; // Рюкзак заполнен
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", valuePerKg=" + String.format("%.2f", (double)cost/weight) +
                    '}';
        }
    }
}