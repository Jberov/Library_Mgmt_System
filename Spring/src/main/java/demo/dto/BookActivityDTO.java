package demo.dto;

import demo.status.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class BookActivityDTO {

    @Null
    private long id;

    @NotNull
    private UserDTO userDTO;

    @NotNull
    private BookDTO bookDTO;

    @NotNull
    private Status statusDTO;

    public BookActivityDTO () {
    }

    public BookActivityDTO (UserDTO userDTO, BookDTO bookDTO, Status statusDTO) {
        this.userDTO = userDTO;
        this.bookDTO = bookDTO;
        this.statusDTO = statusDTO;
    }


    public UserDTO getUser () {
        return userDTO;
    }

    public void setUser (UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public BookDTO getBook () {
        return bookDTO;
    }

    public void setBook (BookDTO bookDTO) {
        this.bookDTO = bookDTO;
    }

    public Status getStatus () {
        return statusDTO;
    }

    public void setStatus (Status statusDTO) {
        this.statusDTO = statusDTO;
    }
}
