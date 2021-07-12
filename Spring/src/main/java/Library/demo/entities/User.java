package Library.demo.entities;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.LinkedList;

@Entity(name = "user")
public class User {
    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private LinkedList <Books> taken_books;
    private LinkedList <Books> returned_books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Books> getTaken_books() {
        return taken_books;
    }

    public void setTaken_books(LinkedList<Books> taken_books) {
        this.taken_books = taken_books;
    }

    public LinkedList<Books> getReturned_books() {
        return returned_books;
    }

    public void setReturned_books(LinkedList<Books> returned_books) {
        this.returned_books = returned_books;
    }
}
