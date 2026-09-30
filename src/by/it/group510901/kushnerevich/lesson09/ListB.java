package by.it.group510901.kushnerevich.lesson09;

import java.util.*;

public class ListB<E> implements List<E> {

    // Начальная ёмкость массива по умолчанию
    private static final int DEFAULT_CAPACITY = 10;

    // Внутренний массив для хранения элементов
    private Object[] elements;

    // Текущее количество элементов в списке
    private int size;

    // Конструктор по умолчанию
    public ListB() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка в виде [элемент1, элемент2, ...]
     */
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    /**
     * Добавляет элемент в конец списка.
     * @return всегда true
     */
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1); // проверяем, хватит ли места
        elements[size++] = e;     // записываем элемент и увеличиваем размер
        return true;
    }

    /**
     * Удаляет элемент по индексу и возвращает его.
     * Все элементы после удалённого сдвигаются влево.
     */
    @Override
    public E remove(int index) {
        checkIndex(index); // проверяем корректность индекса
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // сохраняем удаляемый элемент
        int numMoved = size - index - 1;  // сколько элементов нужно сдвинуть
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // обнуляем последнюю ячейку и уменьшаем размер
        return oldValue;
    }

    /**
     * Возвращает количество элементов в списке.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Вставляет элемент по указанному индексу.
     * Все элементы после индекса сдвигаются вправо.
     */
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index); // проверяем индекс (можно вставлять в конец)
        ensureCapacity(size + 1);
        // Сдвигаем элементы вправо, начиная с позиции index
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    /**
     * Удаляет первое вхождение указанного объекта.
     * @return true, если элемент был найден и удалён
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * Заменяет элемент по индексу, возвращает старое значение.
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * Проверяет, пуст ли список.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает список (обнуляет все элементы и размер).
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // помогаем сборщику мусора
        }
        size = 0;
    }

    /**
     * Возвращает индекс первого вхождения объекта (или -1, если не найден).
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * Возвращает элемент по индексу.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    /**
     * Проверяет, содержится ли объект в списке.
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Возвращает индекс последнего вхождения объекта (или -1).
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Проверяет, содержит ли список все элементы указанной коллекции.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в конец списка.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size + numNew);
        System.arraycopy(a, 0, elements, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Вставляет все элементы коллекции, начиная с указанного индекса.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0) return false;
        ensureCapacity(size + numNew);
        int numMoved = size - index;
        if (numMoved > 0) {
            // Сдвигаем хвост вправо, чтобы освободить место
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elements, index, numNew);
        size += numNew;
        return true;
    }

    /**
     * Удаляет из списка все элементы, содержащиеся в указанной коллекции.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--; // после удаления следующий элемент сместился на текущую позицию
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Оставляет в списке только те элементы, которые содержатся в указанной коллекции.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Возвращает подсписок [fromIndex, toIndex).
     * Возвращается новый независимый список (копия).
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListB<E> sub = new ListB<>();
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(get(i));
        }
        return sub;
    }

    /**
     * Возвращает итератор списка, начиная с указанной позиции.
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return new ListIterator<E>() {
            private int cursor = index;   // текущая позиция курсора
            private int lastRet = -1;     // индекс последнего возвращённого элемента

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (cursor <= 0) throw new NoSuchElementException();
                lastRet = --cursor;
                return (E) elements[cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListB.this.remove(lastRet);
                if (lastRet < cursor) cursor--;
                lastRet = -1;
            }

            @Override
            public void set(E e) {
                if (lastRet < 0) throw new IllegalStateException();
                ListB.this.set(lastRet, e);
            }

            @Override
            public void add(E e) {
                ListB.this.add(cursor++, e);
                lastRet = -1;
            }
        };
    }

    /**
     * Возвращает итератор списка с начала.
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Возвращает массив элементов указанного типа.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Если массив слишком мал — создаём новый нужного размера
            return (T[]) java.util.Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // обнуляем «лишнюю» ячейку
        }
        return a;
    }

    /**
     * Возвращает массив Object[] со всеми элементами списка.
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Обычный итератор (только вперёд, с поддержкой remove).
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                lastRet = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListB.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////
    ////////                   Вспомогательные методы                ////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Увеличивает внутренний массив, если требуется.
     * Новая ёмкость = старая + 50% (или minCapacity, если больше).
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1);
            if (newCapacity < minCapacity) newCapacity = minCapacity;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /**
     * Проверяет, что индекс находится в допустимых границах [0, size).
     * Бросает IndexOutOfBoundsException при нарушении.
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Проверяет индекс для операции добавления (допустимо значение == size).
     */
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}