package demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {

  @Id
  @GeneratedValue
  @Column(name = "Id")
  private Long id;

  @Column(nullable = false)
  private String name;

  public Author(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public Author() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  private String description;
}
