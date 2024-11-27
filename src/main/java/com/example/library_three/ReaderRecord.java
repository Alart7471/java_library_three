package com.example.library_three;

public class ReaderRecord {
    private String readerName;
    private int days;

    public ReaderRecord(String readerName, int days) {
        this.readerName = readerName;
        this.days = days;
    }

    public String getReaderName() {
        return readerName;
    }

    public int getDays() {
        return days;
    }
}
