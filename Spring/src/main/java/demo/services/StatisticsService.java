package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.entities.BooksActivity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

  private final BookRecordsDAO bookRecordsDAO;
  private final BooksDAOImpl booksDAO;

  @Autowired
  public StatisticsService(BookRecordsDAO bookRecordsDAO, BooksDAOImpl booksDAO) {
    this.bookRecordsDAO = bookRecordsDAO;
    this.booksDAO = booksDAO;
  }

  public HashMap<String, Integer> getMostReadBooks(Optional<LocalDate> date){
    List<BooksActivity> logs;
    if (date.isPresent()){
      logs = bookRecordsDAO.getReadLogs(date.get());
    }
    else
      logs = bookRecordsDAO.getAllActivity();

    HashMap<String, Integer> countOfReadBooks = new HashMap<>();

    for (BooksActivity activity : logs) {
      if(countOfReadBooks.containsKey(activity.getBook().getName())){
        countOfReadBooks.put(activity.getBook().getName(), countOfReadBooks.get(activity.getBook().getName()) + 1);
        continue;
      }
      countOfReadBooks.put(activity.getBook().getName(), 1);
    }

    return countOfReadBooks.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
  }


  public String suggestBookToUser(String username) {
    List<BooksActivity> logs = bookRecordsDAO.getAllUserRecords(username);

    Random random = new Random();
    int number;
    String recommendedBookName;
    List<String> currentlyTakenBooks;
    for (BooksActivity activity : logs) {
      number = random.nextInt(booksDAO.getAllBooks().size());
      recommendedBookName = booksDAO.getAllBooks().get(number).getName();
      if (activity.getBook().getName().equals(recommendedBookName)){
        continue;
      }
      return recommendedBookName;
    }
    return "";
  }

  public HashMap<String, Integer> getMostReadGenresByDate(Optional<LocalDate> date){
    List<BooksActivity> logs;
    if (date.isEmpty()){
      logs = bookRecordsDAO.getAllActivity();
    } else{
      logs = bookRecordsDAO.getReadLogs(date.get());
    }
    HashMap<String, Integer> countOfReadBooks = new HashMap<>();

    for (BooksActivity activity : logs) {
      if (countOfReadBooks.containsKey(activity.getBook().getGenre().getGenre())) {
        countOfReadBooks.put(activity.getBook().getGenre().getGenre(), countOfReadBooks.get(activity.getBook().getGenre().getGenre()) + 1);
        continue;
      }
      countOfReadBooks.put(activity.getBook().getGenre().getGenre(), 1);
    }

    return  countOfReadBooks.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
  }

  public HashMap<String, Integer> getMostReadAuthorsByDate(Optional<LocalDate> date){
    List<BooksActivity> logs;
    if (date.isEmpty()){
      logs = bookRecordsDAO.getAllActivity();
    } else{
      logs = bookRecordsDAO.getReadLogs(date.get());
    }
    HashMap<String, Integer> countOfReadBooks = new HashMap<>();

    for (BooksActivity activity : logs) {
      if (countOfReadBooks.containsKey(activity.getBook().getAuthor().getName())) {
        countOfReadBooks.put(activity.getBook().getAuthor().getName(), countOfReadBooks.get(activity.getBook().getAuthor().getName()) + 1);
        continue;
      }
      countOfReadBooks.put(activity.getBook().getAuthor().getName(), 1);
    }

    return  countOfReadBooks.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> newValue, LinkedHashMap::new));
  }

  public int getCountOfReadBooks(Optional<LocalDate> date) {
    return date.map(localDate -> bookRecordsDAO.getReadLogs(localDate).size())
        .orElseGet(() -> bookRecordsDAO.getAllActivity().size());
  }
}
