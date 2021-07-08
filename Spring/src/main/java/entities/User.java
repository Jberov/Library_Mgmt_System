package entities;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.LinkedList;

@Entity(name = "user")
public class User {
    private @Id @GeneratedValue int id;
    private String name;
    private LinkedList taken_books;
    private LinkedList returned_books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList getTaken_books() {
        return taken_books;
    }

    public void setTaken_books(LinkedList taken_books) {
        this.taken_books = taken_books;
    }

    public LinkedList getReturned_books() {
        return returned_books;
    }

    public void setReturned_books(LinkedList returned_books) {
        this.returned_books = returned_books;
    }
}
