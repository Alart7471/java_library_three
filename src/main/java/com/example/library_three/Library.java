package com.example.library_three;

import com.example.library_three.Book;

public class Library {
    private Book[] books;
    private int bookCount;

    public Library(int capacity) {
        books = new Book[capacity];
        bookCount = 0;
    }

    public boolean addBook(String title, String author) {
        if (bookCount < books.length) {
            for (int i = 0; i < bookCount; i++) {
                if (books[i].getTitle().equalsIgnoreCase(title)) {
                    System.out.println("Книга с таким названием уже есть в библиотеке.");
                    return false;
                }
            }
            books[bookCount++] = new Book(title, author);
            return true;
        } else {
            System.out.println("Нет места для добавления новой книги.");
            return false;
        }
    }

    public Book[] getAllBooks() {
        Book[] result = new Book[bookCount];
        System.arraycopy(books, 0, result, 0, bookCount);
        return result;
    }

    public void removeBook() {
        if (bookCount > 0) {
            for (int i = 0; i < bookCount - 1; i++) {
                books[i] = books[i + 1];
            }
            books[bookCount - 1] = null;
            bookCount--;
        } else {
            System.out.println("Нет книг для удаления.");
        }
    }

    public Book getBookByTitle(String title) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getTitle().equalsIgnoreCase(title)) {
                return books[i];
            }
        }
        System.out.println("Книга с названием \"" + title + "\" не найдена.");
        return null;
    }
}
