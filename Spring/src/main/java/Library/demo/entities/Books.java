package Library.demo.entities;

import javax.persistence.*;

@Entity(name = "BOOKS")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long isbn;

    @Column
    private int count_books;
    @Column
    private String author;
    @Column
    private String name;
    @Column
    private String description;


    public Books( int count_books, String author, String name, String description) {
        this.count_books = count_books;
        this.author = author;
        this.name = name;
        this.description = description;
    }

    public Books() {
    }


    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getCount() {
        return count_books;
    }

    public void setCount(int count) {
        this.count_books = count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
