package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dto.BookDTO;
import demo.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BookService {

    @Autowired
    private BooksDAOImpl booksDAO;

    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    @Autowired
    private BookMapper bookMapper;

    public LinkedList<BookDTO> getAllBooks () {
        return bookMapper.linkedListToDTO(booksDAO.getAllBooks());
    }

    public BookDTO addBook (BookDTO bookDTO) {
        if (booksDAO.getBook(bookDTO.getIsbn()) != null) {
            if (!booksDAO.getBook(bookDTO.getIsbn()).getName().equals(bookDTO.getName())) {
                return null;
            }else if(!booksDAO.getBook(bookDTO.getIsbn()).isExists()){
                return bookMapper.bookToDTO(booksDAO.restoreBook(bookMapper.bookToEntity(bookDTO)));
            }
            booksDAO.increaseCount(bookDTO.getIsbn(), bookDTO.getCount());
        }
        return bookMapper.bookToDTO(booksDAO.addBook(bookMapper.bookToEntity(bookDTO)));
    }

    public BookDTO deleteBook (String isbn) {
        if (!booksDAO.bookExistsByID(isbn)) {
            return null;
        } else if (bookRecordsDAO.userHistoryExists(isbn)) {
            return null;
        }
        return  bookMapper.bookToDTO(booksDAO.deleteBookAdmin(isbn));
    }

    public BookDTO getBookById (String isbn) {
        return bookMapper.bookToDTO(booksDAO.getBook(isbn));
    }
}

