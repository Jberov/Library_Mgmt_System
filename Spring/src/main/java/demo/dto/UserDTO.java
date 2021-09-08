package demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.LinkedList;

public class UserDTO {

    public UserDTO(String name) {
        this.name = name;
    }

    public UserDTO() {
    }

    public long getId () {
        return id;
    }

    public void setId (long id) {
        this.id = id;
    }

    @Null
    private long id;

    @NotNull
    private String name;

    private HashMap<String,LinkedList<BookDTO>> userHistory;

    public HashMap<String, LinkedList<BookDTO>> getUserHistory () {
        return userHistory;
    }

    public void setUserHistory (HashMap<String,LinkedList<BookDTO>> userHistory) {
        this.userHistory = userHistory;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
