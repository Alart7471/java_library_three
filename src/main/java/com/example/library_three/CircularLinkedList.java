package com.example.library_three;

public class CircularLinkedList<T> implements Iterable<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;

    public CircularLinkedList() {
        head = null;
        tail = null;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
    }

    public void remove(String readerName) {
        System.out.println(readerName);
        if (head == null) return;

        Node<T> current = head;
        Node<T> previous = null;
        do {
            if (((ReaderRecord) current.data).getReaderName().equals(readerName)) {
                if (previous == null) {  // Удаление первого элемента
                    head = current.next;
                    tail.next = head;
                } else {
                    previous.next = current.next;
                    if (current == tail) {
                        tail = previous;
                    }
                }
                return;
            }
            previous = current;
            current = current.next;
        } while (current != head);
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private Node<T> current = head;
            private boolean firstIteration = true;

            @Override
            public boolean hasNext() {
                // Проверяем, что текущий элемент существует и что мы не вернулись к началу
                return current != null && (firstIteration || current != head);
            }


            @Override
            public T next() {
                if (current == null) {
                    throw new java.util.NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                firstIteration = false;
                return data;
            }
        };
    }

    public int size() {
        if (head == null) return 0;
        int size = 0;
        Node<T> current = head;
        do {
            size++;
            current = current.next;
        } while (current != head);
        return size;
    }
}

