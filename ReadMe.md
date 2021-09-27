# LibraryApp Application

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
    - Delete users

## API end points

### POST /api/v1/books
Adds a new book to the database. Response : "Success" and an object with the added book, if a new book is successfully added to the database or increases the . The required data is sent via a JSON object

### DELETE api/v1/books/{isbn}
Deletes a book from the database, given its name. Response includes the object with the deleted book, if operation was successful.

### GET /api/v1/books/{isbn}
Gets all information about a book, given its id. Response : Status 200 OK and a JSON object with the requested book information

### GET /api/v1/users/{isbn}/books
Gets all users, who have loaned the book with the given id(isbn). Response : Status 200 OK and a JSON object with all users, if they exist

### GET /api/v1/users/info/{name}
Gets all  information about a user, given his/her username. Response : Status 200 OK and a JSON object with the user information, if he/she exists.

### PATCH api/v1/books/rental/{isbn}
Allows a user to lease a book from the library, if enough copies are available. Response : "Book successfully leased", or "Another copy successfully fetched", if conditions are met, otherwise a custom message will show what went wrong.

### PATCH api/v1/books/reconveyance/{isbn}
Allows a user to return a book from the library. Response : "Book successfully returned", if conditions are met, otherwise a custom message will show what went wrong.

### GET api/v1/books
Lists all books. Response : Status 200 OK and a JSON object with the books, if any are present.

### GET api/v1/users/history/{username}
Lists all books, which the user has ever used, given his/her username Response : Status 200 OK and a JSON object with the books, if any are present.

### DELETE "api/v1/users/{username}"
Deletes a user, given his username. Returns the details of the deleted user, if operation is successful, or an appropriate error message.

## Roadmap
- Automate deployment
