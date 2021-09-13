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

### POST /admin/addBook/{count_books}{author}{name}{description}

Adds a new book to the database. Response : "Success", if operation was successful

### DELETE /admin/book/delete{name}

Deletes a book from the database, given its name Response : "Delete successful", if operation was successful

### GET admin/getBook{isbn}

Gets all information about a book, given its id. Response : Status 200 OK and a JSON object with the requested book
information

### GET /admin/book/users{isbn}

Gets all users, who have loaned the book with the given id(isbn). Response : Status 200 OK and a JSON object with all
users, if they exist

### GET /admin/user/profile{username}

Gets all all information about a user, given his/her username. Response : Status 200 OK and a JSON object with the user
information, if he/she exists.

### PATCH /users/lease{isbn}{username}

Allows a user to lease a book from the library, if enough copies are available. Response : "Book successfully leased",
if conditions are met, otherwise a custom message will show what went wrong.

### PATCH /users/returnBook{isbn}{username}

Allows a user to return a book from the library. Response : "Book successfully returned", if conditions are met,
otherwise a custom message will show what went wrong.

### GET /admin/book/all

Lists all book. Response : Status 200 OK and a JSON object with the book, if any are present.

### GET /users/history{username}

Lists all book, which the user has ever used, given his/her username Response : Status 200 OK and a JSON object with the
book, if any are present.

