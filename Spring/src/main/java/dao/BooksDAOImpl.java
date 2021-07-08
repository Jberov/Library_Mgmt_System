package dao;

import entities.Books;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;


public class BooksDAOImpl {
    @GetMapping("/books")
    public static LinkedList<Books> get_all_books(){
        LinkedList<Books> books = new LinkedList<>();
        books.add(new Books(2,"A", "B", "C"));
        books.add(new Books(21,"B", "S", "FG-42"));
        return books;
    }
}
