package demo.mappers;

import demo.dto.BookActivityDTO;
import demo.entities.BooksActivity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    private BookMapper bookMapper;
    private UserMapper userMapper;
 public BookActivityDTO activityToDTO(BooksActivity activity){
     if(activity == null){
         return null;
     }
     BookActivityDTO activityDTO = new BookActivityDTO();
     activityDTO.setUser(userMapper.userToDTO(activity.getUser()));
     activityDTO.setStatus(activity.getStatus());
     activityDTO.setBook(bookMapper.bookToDTO(activity.getBook()));
     return activityDTO;
 }
}
