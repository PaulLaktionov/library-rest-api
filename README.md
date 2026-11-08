# Library REST API

REST API for a small library: manage books and readers, and issue books to readers.
Issuing a book is **atomic**: the available-copies counter is decreased and the loan record is created in one transaction — either both happen, or neither.

## Tech stack

- Java 21
- Spring Boot 4.1.2 (Spring Web MVC, Spring Data JPA / Hibernate)
- MySQL 8
- Lombok
- Maven

## Features

- CRUD for books and readers
- Issue a book to a reader (`POST /api/loans`) with business rules:
  - the book must exist → otherwise `404`
  - the book must have available copies → otherwise `400`
  - the reader must exist → otherwise `404`
- Global error handling (`@RestControllerAdvice`): every error is returned as JSON in one format
- Unique reader email (duplicate → `400`)
- Return a book (`POST /api/loans/{id}/return`): sets the return date and puts the copy back; returning twice → `400`
- Input validation (`@Valid`): empty title/author/name, invalid email, negative copies → `400` with the list of invalid fields

## Architecture

```
src/main/java/com/example/library
├── controller   – REST endpoints
├── service      – business logic (LoanService.issueBook is @Transactional)
├── repository   – JpaRepository interfaces
├── model        – JPA entities: Book, Reader, Loan
└── exception    – NotFoundException, ErrorResponse, GlobalExceptionHandler
```

## How to run

**Requirements:** Java 21, MySQL running locally.

1. Create the database:
   ```sql
   CREATE DATABASE librarydb;
   ```
2. Set the MySQL password as an environment variable (it is not stored in the code):
   - PowerShell: `$env:DB_PASSWORD="your_password"`
   - Git Bash / Linux / macOS: `export DB_PASSWORD=your_password`
3. If your MySQL user is not `root`, change `spring.datasource.username` in `src/main/resources/application.properties`.
4. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
   or build a JAR and run it:
   ```bash
   ./mvnw clean package
   java -jar target/library-0.0.1-SNAPSHOT.jar
   ```

Tables are created automatically. The API is available at `http://localhost:8080`.

## API

| Method | Endpoint | Success | Errors |
|---|---|---|---|
| GET | `/api/books` | 200 – list of books | |
| GET | `/api/books/{id}` | 200 – book | 404 |
| POST | `/api/books` | 201 – created book | |
| PUT | `/api/books/{id}` | 200 – updated book | 404 |
| DELETE | `/api/books/{id}` | 204 | 404 |
| GET | `/api/readers` | 200 – list of readers | |
| GET | `/api/readers/{id}` | 200 – reader | 404 |
| POST | `/api/readers` | 201 – created reader | 400 (duplicate email) |
| PUT | `/api/readers/{id}` | 200 – updated reader | 404, 400 |
| DELETE | `/api/readers/{id}` | 204 | 404 |
| GET | `/api/loans` | 200 – list of loans | |
| POST | `/api/loans` | 201 – created loan | 404, 400 |
| POST | `/api/loans/{id}/return` | 200 – returned loan | 404, 400 (already returned) |


## Manual testing (Postman)



| # | Request | Body | Expected |
|---|---|---|---|
| 1 | `POST /api/books` | `{"title":"Clean Code","author":"Robert Martin","availableCopies":1}` | **201** – book with `id` |
| 2 | `POST /api/readers` | `{"name":"Ivan","email":"ivan@mail.com"}` | **201** – reader with `id` |
| 3 | `POST /api/loans` | `{"bookId":1,"readerId":1}` | **201** – loan with `loanDate` |
| 4 | `GET /api/books/1` | – | **200** – `availableCopies` is now `0` |
| 5 | `POST /api/loans` (again) | `{"bookId":1,"readerId":1}` | **400** – no copies left |
| 6 | `GET /api/books/999` | – | **404** – book not found |
| 7 | `POST /api/readers` (same email) | `{"name":"Bob","email":"ivan@mail.com"}` | **400** – duplicate email |
| 8 | `POST /api/loans/1/return` | – | **200** – loan with `returnDate` |
| 9 | `GET /api/books/1` | – | **200** – `availableCopies` is back to `1` |
| 10 | `POST /api/loans/1/return` (again) | – | **400** – already returned |
| 11 | `POST /api/books` | `{"title":"","author":"X","availableCopies":-1}` | **400** – validation errors |

Error responses always have the same format:

```json 
{"error": "Bad Request", "message": "No available copies for book with id 1"}
{"error": "Not Found", "message": "Book with id 999 not found"}
{"error": "Bad Request", "message": "Reader with this email already exists"}
{"error": "Bad Request", "message": "availableCopies: must be 0 or more, title: must not be blank"}
```