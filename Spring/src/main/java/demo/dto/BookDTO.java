package demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

public class BookDTO {


        @Pattern(regexp = "([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})")
        @NotNull
        private String isbn;

        @PositiveOrZero
        private int count_books;

        @NotNull
        private String author;

        @NotNull
        private String name;

        @NotNull
        private String description;

        @NotNull
        private boolean existence;


        public boolean isExists () {
            return existence;
        }

        public void setExists (boolean exists) {
            this.existence = exists;
        }



        public BookDTO (String isbn, int count_books, String author, String name, String description, boolean existence) {
            this.isbn = isbn;
            this.count_books = count_books;
            this.author = author;
            this.name = name;
            this.description = description;
            this.existence = existence;
        }

        public BookDTO () {
        }

        @JsonProperty("book_isbn")
        public String getIsbn () {
            return isbn;
        }

        public void setIsbn (String isbn) {
            this.isbn = isbn;
        }

        @JsonProperty("book_count")
        public int getCount () {
            return count_books;
        }

        public void setCount (int count) {
            this.count_books = count;
        }

        @JsonProperty("book_count")
        public String getAuthor () {
            return author;
        }

        public void setAuthor (String author) {
            this.author = author;
        }

        @JsonProperty("book_name")
        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        @JsonProperty("book_description")
        public String getDescription () {
            return description;
        }

        public void setDescription (String description) {
            this.description = description;
        }
}
