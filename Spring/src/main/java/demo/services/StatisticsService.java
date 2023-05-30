package demo.services;

import demo.dao.BookRecordsDAO;
import demo.entities.BooksActivity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

  private final BookRecordsDAO bookRecordsDAO;

  @Autowired
  public StatisticsService(BookRecordsDAO bookRecordsDAO) {
    this.bookRecordsDAO = bookRecordsDAO;
  }

  public HashMap<String, Integer> getMostReadBooks(LocalDate date){
    List<BooksActivity> logs = bookRecordsDAO.getReadLogs(date);

    HashMap<String, Integer> countOfReadBooks = new HashMap<>();

    for (BooksActivity activity : logs) {
      if(countOfReadBooks.containsKey(activity.getBook().getName())){
        countOfReadBooks.put(activity.getBook().getName(), countOfReadBooks.get(activity.getBook().getName()) + 1);
        continue;
      }
      countOfReadBooks.put(activity.getBook().getName(), 1);
    }
    return  countOfReadBooks.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
  }

  public HashMap<String, Integer> getMostReadGenresByDate(LocalDate date){
    List<BooksActivity> logs = bookRecordsDAO.getReadLogs(date);

    HashMap<String, Integer> countOfReadBooks = new HashMap<>();

    for (BooksActivity activity : logs) {
      if(countOfReadBooks.containsKey(activity.getBook().getName())){
        countOfReadBooks.put(activity.getBook().getName(), countOfReadBooks.get(activity.getBook().getName()) + 1);
        continue;
      }
      countOfReadBooks.put(activity.getBook().getName(), 1);
    }
    return  countOfReadBooks.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
  }

  public int getCountOfReadBooksForTime(LocalDate date){
   return bookRecordsDAO.getReadLogs(date).size();
  }
}
