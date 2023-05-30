package demo.dto;

import demo.enums.Status;

import java.time.LocalDate;
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

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	private LocalDate issueDate;

	private LocalDate returnDate;
	
	public BookActivityDTO(UserDTO userDTO, BookDTO bookDTO, Status statusDTO) {
		this.userDTO = userDTO;
		this.bookDTO = bookDTO;
		this.statusDTO = statusDTO;
	}

	public BookActivityDTO(UserDTO userDTO, BookDTO bookDTO, Status statusDTO, LocalDate issueDate, LocalDate returnDate) {
		this.userDTO = userDTO;
		this.bookDTO = bookDTO;
		this.statusDTO = statusDTO;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}
	
	public UserDTO getUser() {
		return userDTO;
	}
	
	public void setUser(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	public BookDTO getBook() {
		return bookDTO;
	}
	
	public void setBook(BookDTO bookDTO) {
		this.bookDTO = bookDTO;
	}
	
	public Status getStatus() {
		return statusDTO;
	}
	
	public void setStatus(Status statusDTO) {
		this.statusDTO = statusDTO;
	}
}
