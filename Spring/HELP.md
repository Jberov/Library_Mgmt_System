# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.2/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.2/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/docs/2.5.2/reference/htmlsingle/#boot-features-security-oauth2-server)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.5.2/reference/htmlsingle/#boot-features-security-oauth2-client)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.2/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

# Project documentation - REST API Endpoints

### POST /api/v1/books

Adds a new book to the database, information is sent via a JSON object . Response : "Success", if operation was successful

### DELETE /api/v1/books/{isbn}

Deletes a book from the database, given its isbn. Response : "Delete successful", if operation was successful

### GET /api/v1/books/{isbn}

Gets all information about a book, given its id. Response : Status 200 OK and a JSON object with the requested book
information

### GET /admin/book/users/{isbn}

Gets all users, who have loaned the book with the given id(isbn). Response : Status 200 OK and a JSON object with all
users, if they exist

### GET /api/v1/users/info/{name}

Gets all information about a user, given his/her username. Response : Status 200 OK and a JSON object with the user
information, if he/she exists.

### PATCH api/v1/books/rental/{isbn}

Allows a user to lease a book from the library, if enough copies are available. Response : "Book successfully leased",
if conditions are met, otherwise a custom message will show what went wrong.

### PATCH "api/v1/books/reconveyance/{isbn}"

Allows a user to return a book from the library. Response : "Book successfully returned", if conditions are met,
otherwise a custom message will show what went wrong.

### GET api/v1/books

Lists all book. Response : Status 200 OK and a JSON object with the book, if any are present.

### GET "api/v1/users/history"

Lists all books, which the user has ever used, given his/her username Response : Status 200 OK and a JSON object with the
book, if any are present.

### DELETE "api/v1/users/{username}"

Deletes a user, given his username. Returns the details of the deleted user, if operation is successful, or an appropriate error message.

