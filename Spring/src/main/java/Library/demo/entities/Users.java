package Library.demo.entities;

import javax.persistence.*;


@Entity(name = "users")
public class Users {
    public Users(String name) {
        this.name = name;
    }

    public Users() {
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
    @Column
    private String name;
    /*
    private LinkedList <Books> taken_books;


    private LinkedList <Books> returned_books;
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
