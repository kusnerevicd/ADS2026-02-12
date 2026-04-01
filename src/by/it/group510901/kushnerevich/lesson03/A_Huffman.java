package by.it.group510901.kushnerevich.lesson03;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//Lesson 3. A_Huffman.
//Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

// По данным файла (непустой строке ss длины не более 104104),
// состоящей из строчных букв латинского алфавита,
// постройте оптимальный по суммарной длине беспрефиксный код.

// Используйте Алгоритм Хаффмана — жадный алгоритм оптимального
// безпрефиксного кодирования алфавита с минимальной избыточностью.

// В первой строке выведите количество различных букв kk,
// встречающихся в строке, и размер получившейся закодированной строки.
// В следующих kk строках запишите коды букв в формате "letter: code".
// В последней строке выведите закодированную строку. Примеры ниже

//        Sample Input 1:
//        a
//
//        Sample Output 1:
//        1 1
//        a: 0
//        0

//        Sample Input 2:
//        abacabad
//
//        Sample Output 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

public class A_Huffman {

    // Статическая карта для хранения кодов Хаффмана для каждого символа
    // TreeMap используется для автоматической сортировки по ключам (символам)
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        // Читаем входные данные из файла dataA.txt
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();

        // Выполняем кодирование строки
        String result = instance.encode(inputStream);

        long finishTime = System.currentTimeMillis();

        // Выводим результат в требуемом формате:
        // 1) количество различных букв и длину закодированной строки
        // 2) коды для каждой буквы
        // 3) саму закодированную строку
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {
        // Читаем строку для кодирования из входного потока
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // Подсчет частоты встречаемости каждого символа
        // Создаем карту, где ключ - символ, значение - количество его вхождений
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            // getOrDefault возвращает текущее значение или 0, если символа еще нет
            // затем прибавляем 1 и сохраняем обратно
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // Создание листьев дерева Хаффмана
        // Каждый лист содержит символ и его частоту
        // Используем PriorityQueue для автоматической сортировки по частоте
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        // Для каждого уникального символа создаем листовой узел и добавляем в очередь
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Построение дерева Хаффмана
        // Алгоритм: пока в очереди больше одного узла, извлекаем два с наименьшей частотой,
        // создаем из них родительский узел (сумма частот детей) и возвращаем его в очередь

        if (priorityQueue.size() == 1) {
            // Особый случай: строка состоит из одного повторяющегося символа
            // Например, "aaaaa" - все символы одинаковые
            // В этом случае просто назначаем ему код "0"
            Node only = priorityQueue.poll();
            only.fillCodes("0");
        } else {
            // Общий случай: есть минимум два разных символа
            while (priorityQueue.size() > 1) {
                // Извлекаем два узла с наименьшими частотами
                Node left = priorityQueue.poll();   // левый ребенок (с меньшей частотой)
                Node right = priorityQueue.poll();  // правый ребенок (со следующей по величине)

                // Создаем родительский узел, частота которого равна сумме частот детей
                Node parent = new InternalNode(left, right);

                // Возвращаем родительский узел обратно в очередь
                priorityQueue.add(parent);
            }

            // После цикла в очереди остается один узел - корень дерева Хаффмана
            Node root = priorityQueue.poll();
            if (root != null) {
                // Запускаем рекурсивное построение кодов от корня
                // Пустая строка-префикс для корня
                root.fillCodes("");
            }
        }

        // Кодирование исходной строки
        // Используем построенные коды для замены каждого символа на его двоичный код
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            // Для каждого символа берем его код из карты codes и добавляем в результат
            sb.append(codes.get(c));
        }

        // Возвращаем закодированную строку
        return sb.toString();
        // Пример для "abacabad": 01001100100111
    }

    // Абстрактный базовый класс для узлов дерева Хаффмана
    // Реализует Comparable для сортировки в PriorityQueue по частоте
    abstract class Node implements Comparable<Node> {
        private final int frequence; // частота символа (для листьев) или сумма частот детей (для внутренних узлов)

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Абстрактный метод для рекурсивного заполнения кодов
        // Принимает текущий префикс кода (для корня - пустая строка)
        abstract void fillCodes(String code);

        // Сравнение узлов по частоте (нужно для PriorityQueue)
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Класс для внутреннего узла дерева (имеет двух детей)
    private class InternalNode extends Node {
        Node left;  // левый ребенок (код будет заканчиваться на 0)
        Node right; // правый ребенок (код будет заканчиваться на 1)

        // Конструктор: создает родительский узел из двух детей
        // Частота родителя = сумма частот детей
        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        // Рекурсивное заполнение кодов для внутреннего узла
        // Для левого ребенка добавляем "0" к текущему коду
        // Для правого ребенка добавляем "1" к текущему коду
        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    ////////////////////////////////////////////////////////////////////////////////////
    // Класс для листового узла дерева (содержит конкретный символ)
    private class LeafNode extends Node {
        char symbol; // символ, который хранится в этом листе

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        // Для листового узла рекурсия завершается
        // Сохраняем полученный код для символа в статическую карту codes
        @Override
        void fillCodes(String code) {
            // Когда дошли до листа, код для символа полностью сформирован
            // Запоминаем его в карте для последующего кодирования
            codes.put(this.symbol, code);
        }
    }
}