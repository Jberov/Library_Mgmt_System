package Library.demo.entities;

import javax.persistence.*;
import java.util.LinkedList;


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


    public LinkedList<Books> getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(LinkedList<Books> userHistory) {
        this.userHistory = userHistory;
    }

    @Transient
    private LinkedList<Books> userHistory;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
