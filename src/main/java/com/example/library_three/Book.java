package com.example.library_three;

//import com.example.library_three.CircularLinkedList;

public class Book {
    private String title;
    private String author;
    private CircularLinkedList<ReaderRecord> readerRecords;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.readerRecords = new CircularLinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void addReaderRecord(String readerName, int days) {
        readerRecords.add(new ReaderRecord(readerName, days));
    }

    public void removeReaderRecord(String readerName) {
        readerRecords.remove(readerName);
    }

    public CircularLinkedList<ReaderRecord> getReaderRecords() {
        return readerRecords;
    }

    public int getTotalUsageDays() {
        int total = 0;
        for (ReaderRecord record : readerRecords) {
            total += record.getDays();
        }
        return total;
    }
}
