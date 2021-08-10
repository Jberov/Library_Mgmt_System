# Library Application

Hello,
this is the LibraryApp application. This is a small programme, which simulates a real-life library. This app has the following features:

- Users
    - Take a book
    - Return a book
    - View history of taken and returned books
    - View a list of all books
- Librarians
    - See who has taken a book by its isbn
    - Add a new book to the library
    - Remove a book from the library
    - Get details about a book from its isbn
    - Get details about a user by his/her username
    - View a list of all books

## API end points

### POST /admin/addBook/{count_books}{author}{name}{description}
Adds a new book to the database. Response : "Success", if operation was successful

### DELETE /admin/books/delete{name}
Deletes a book from the database, given its name Response : "Delete successful", if operation was successful

### GET admin/getBook{isbn}
Gets all information about a book, given its id. Response : Status 200 OK and a JSON object with the requested book information

### GET /admin/book/users{isbn}
Gets all users, who have loaned the book with the given id(isbn). Response : Status 200 OK and a JSON object with all users, if they exist

### GET /admin/user/profile{username}
Gets all  information about a user, given his/her username. Response : Status 200 OK and a JSON object with the user information, if he/she exists.

### PATCH /users/lease{isbn}{username}
Allows a user to lease a book from the library, if enough copies are available. Response : "Book successfully leased", if conditions are met, otherwise a custom message will show what went wrong.

### PATCH /users/returnBook{isbn}{username}
Allows a user to return a book from the library. Response : "Book successfully returned", if conditions are met, otherwise a custom message will show what went wrong.

###GET /admin/books/all
Lists all books. Response : Status 200 OK and a JSON object with the books, if any are present.

### GET /users/history{username}
Lists all books, which the user has ever used, given his/her username Response : Status 200 OK and a JSON object with the books, if any are present.

## Roadmap
- Add authentication
- Automate deployment
