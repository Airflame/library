package com.library.controller;

import com.library.dto.BookDTO;
import com.library.dto.BookHistoryDTO;
import com.library.dto.EveryBookDTO;
import com.library.dto.LentBookDTO;
import com.library.model.Book;
import com.library.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller with endpoints for managing books
 *
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@Api("Books Management API")
public class BookController {

    private final BookService bookService;

    /**
     * Returns a lists of all books in the database along with associated lendings.
     * Accepts GET requests at /api/books/.
     *
     * @return List of all books in the database
     */
    @ApiOperation(value = "Lists all books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books found")
    })
    @GetMapping(path = "/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Adds a new book to the database.
     * Accepts POST requests at /api/books.
     * Returns HTTP status code 422 if a book with a given author and title already exists
     *
     * @param author   Author of the book.
     * @param title    Title of the book.
     * @param category Name of the category to which the book belongs. Gets set to "Default" if not found.
     * @return Created book entity
     */
    @ApiOperation(value = "Creates a book")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book created"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 422, message = "Book already added")
    })
    @PostMapping(path = "/books")
    public ResponseEntity<Book> createBook(@RequestParam String author,
                                           @RequestParam String title,
                                           @RequestParam(required = false) String category) {
        return bookService.createBook(author, title, category);
    }

    /**
     * Removes a book from the database.
     * Accepts DELETE requests at /api/books/{id}.
     * Returns HTTP status code 404 if the book with a given id doesn't exist.
     * Returns HTTP status code 422 if the book is currently being lent to someone.
     *
     * @param id Id of a book to be deleted
     */
    @ApiOperation(value = "Deletes a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book deleted"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 422, message = "Book is currently being lent")
    })
    @DeleteMapping(path = "/books/{id}")
    public void deleteBookById(@PathVariable Integer id) {
        bookService.deleteBookById(id);
    }

    /**
     * Returns a list of all books currently available for lending along with the information about the author,
     * and category of every book.
     * Accepts GET requests at /api/books/available.
     *
     * @return List of available books
     */
    @ApiOperation(value = "Lists all books available for lending")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books found"),
    })
    @GetMapping(path = "/books/available")
    public List<BookDTO> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    /**
     * Returns a list of all books currently being lent to someone along with the information about the author,
     * title and category of every book, the first name and the last name of the borrower, the date when the book
     * has been lent and the optional comment.
     * Accepts GET requests at /api/books/lent.
     *
     * @return List of lent books
     */
    @ApiOperation(value = "Lists all lent books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books found"),
    })
    @GetMapping(path = "/books/lent")
    public List<LentBookDTO> getLentBooks() {
        return bookService.getLentBooks();
    }

    /**
     * Returns a list of all books along with information about the author, title and category of every book,
     * the information about whether the book is currently being lent, the first name and the last name of the borrower
     * and the date when the book has been lent.
     * Accepts GET requests at /api/books/all.
     *
     * @return List of all books
     */
    @ApiOperation(value = "Lists all books with information about their availability")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books found"),
    })
    @GetMapping(path = "/books/all")
    public List<EveryBookDTO> getAvailableAndLentBooks() {
        return bookService.getAvailableAndLentBooks();
    }

    /**
     * Returns a lending history of a given book, which contains information about whether the book is currently being
     * lent and a list of all past and current lendings, with the first name and the last name of the borrower, the date
     * when the book has been lent, the date when the book has been returned (set to null if it hasn't been returned
     * yet) and the optional comment.
     * Accepts GET requests at /api/books/history/{id}.
     * Returns HTTP status code 404 if the book with a given id doesn't exist.
     *
     * @param id Id of a given book
     * @return Lending history of a given book
     */
    @ApiOperation(value = "Lists a lending history of a given book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @GetMapping(path = "/books/history/{id}")
    public BookHistoryDTO getBookHistory(@PathVariable Integer id) {
        return bookService.getBookHistory(id);
    }
}
