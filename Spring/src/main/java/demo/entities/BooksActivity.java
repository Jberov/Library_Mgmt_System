package demo.entities;

import javax.persistence.*;

@Entity(name = "ACTIVITY")
public class BooksActivity {
    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Books books;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public BooksActivity() {
    }

    public BooksActivity(Users user, Books book, Status status) {
        this.user = user;
        this.books = book;
        this.status = status;
    }


    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Books getBook() {
        return books;
    }

    public void setBook(Books book) {
        this.books = book;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}