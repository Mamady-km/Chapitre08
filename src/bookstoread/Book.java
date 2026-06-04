package bookstoread;

import java.time.LocalDate;

public class Book implements Comparable<Book> {

    private final String title;
    private final String author;
    private final LocalDate publishedOn;

    public Book(String title, String author, LocalDate publishedOn) {
        this.title = title;
        this.author = author;
        this.publishedOn = publishedOn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(Book that) {
        return this.title.compareTo(that.title);
    }
}