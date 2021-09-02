package demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import demo.entities.Books;


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

    @JsonProperty("user_id")
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

    private HashMap<String,LinkedList<Books>> userHistory;

    @JsonProperty("user_history")
    public HashMap<String, LinkedList<Books>> getUserHistory () {
        return userHistory;
    }

    public void setUserHistory (HashMap<String,LinkedList<Books>> userHistory) {
        this.userHistory = userHistory;
    }

    @JsonProperty("username")
    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
