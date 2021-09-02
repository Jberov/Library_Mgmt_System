package demo.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "books")
public class Books {

    @Id
    private String isbn;

    @Column
    @PositiveOrZero
    private int count_books;

    @Column
    private String author;

    @Column (unique = true)
    private String name;

    @Column
    private String description;

    @Column
    private boolean existence;

    public boolean isExists () {
        return existence;
    }

    public void setExists (boolean exists) {
        this.existence = exists;
    }

    public Books (String isbn, int count_books, String author, String name, String description, boolean existence) {
        this.isbn = isbn;
        this.count_books = count_books;
        this.author = author;
        this.name = name;
        this.description = description;
        this.existence = existence;
    }

    public Books () {
    }

    public String getIsbn () {
        return isbn;
    }

    public void setIsbn (String isbn) {
        this.isbn = isbn;
    }

    public int getCount () {
        return count_books;
    }

    public void setCount (int count) {
        this.count_books = count;
    }

    public String getAuthor () {
        return author;
    }

    public void setAuthor (String author) {
        this.author = author;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }
}
