package demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
public class Genre {

  @Id
  @GeneratedValue
  @Column(name = "Id")
  private Long id;

  @Column(nullable = false)
  private String genre;

  @Column
  private String description;

  public Genre() {
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Genre(String genre, String description) {
    this.genre = genre;
    this.description = description;
  }

}
