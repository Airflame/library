package com.library.service;

import com.library.dto.*;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Category;
import com.library.model.Lending;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.LendingRepository;
import com.library.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service with methods for managing books in the database.
 *
 *
 */
@Component
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final LendingRepository lendingRepository;

    private final BorrowerRepository borrowerRepository;

    /**
     * Returns a lists of all books in the database along with associated lendings.
     *
     * @return List of all books in the database
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Adds a new book to the database.
     *
     * @param author       Author of the book.
     * @param title        Title of the book.
     * @param categoryName Name of the category to which the book belongs. Gets set to "Default" if not found.
     * @return Created book entity
     */
    public ResponseEntity<Book> createBook(String author, String title, String categoryName) {
        if (bookRepository.findByAuthorAndTitle(author, title).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book already added");
        Book book = Book.builder().author(author).title(title).isLent(false).build();
        Category category = categoryRepository.findByName(categoryName).orElse(
                categoryRepository.findByName("default").orElseThrow()
        );
        book.setCategoryId(category.getId());
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * Removes a book from the database. Throws an exception if the book with a given id doesn't exist or is currently
     * being lent to someone.
     *
     * @param id Id of a book to be deleted
     */
    public void deleteBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Book not found"));
        if (book.getIsLent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book is currently being lent");
        bookRepository.deleteById(id);
    }

    /**
     * Returns a list of all books currently available for lending along with the information about the author,
     * and category of every book.
     *
     * @return List of available books
     */
    public List<BookDTO> getAvailableBooks() {
        List<Book> books = bookRepository.findByIsLent(false);
        Set<Integer> categoryIds = books.stream().map(Book::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryRepository.findByIdIn(categoryIds);
        return books.stream().map(book -> mapToAvailableBookDTO(book, categories)).collect(Collectors.toList());
    }

    /**
     * Returns a list of all books currently being lent to someone along with the information about the author, title and category
     * of every book, the first name and the last name of the borrower, the date when the book has been lent
     * and the optional comment.
     *
     * @return List of lent books
     */
    public List<LentBookDTO> getLentBooks() {
        List<Book> books = bookRepository.findByIsLent(true);
        List<Borrower> borrowers = borrowerRepository.findAll();
        Set<Integer> categoryIds = books.stream().map(Book::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryRepository.findByIdIn(categoryIds);
        List<Lending> lendings = lendingRepository.findByDateReturned(null);
        return books.stream().map(book -> mapToLentBookDTO(book, categories, lendings, borrowers)).collect(Collectors.toList());
    }

    /**
     * Returns a list of all books along with information about the author, title and category of every book, the information about
     * whether the book is currently being lent, the first name and the last name of the borrower and the date
     * when the book has been lent.
     *
     * @return List of all books
     */
    public List<EveryBookDTO> getAvailableAndLentBooks() {
        List<Book> books = bookRepository.findAll();
        List<Borrower> borrowers = borrowerRepository.findAll();
        Set<Integer> categoryIds = books.stream().map(Book::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryRepository.findByIdIn(categoryIds);
        List<Lending> lendings = lendingRepository.findByDateReturned(null);
        return books.stream().map(book -> mapToEveryBookDTO(book, categories, lendings, borrowers)).collect(Collectors.toList());
    }

    /**
     * Returns a lending history of a given book, which contains information about whether the book is currently being
     * lent and a list of all past and current lendings, with the first name and the last name of the borrower, the date
     * when the book has been lent, the date when the book has been returned (set to null if it hasn't been returned
     * yet) and the optional comment. Throws an exception if the book with a given id doesn't exist.
     *
     * @param id Id of a given book
     * @return Lending history of a given book
     */
    public BookHistoryDTO getBookHistory(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Book not found"));
        List<Borrower> borrowers = borrowerRepository.findAll();
        return BookHistoryDTO.builder().id(id).author(book.getAuthor()).title(book.getTitle()).isLent(book.getIsLent())
                .lendings(book.getLendings().stream().sorted(Comparator.comparingInt(Lending::getId))
                        .map(l -> mapToLendingDTO(l, borrowers)).collect(Collectors.toList())).build();
    }

    private BookDTO mapToAvailableBookDTO(Book book, List<Category> categories) {
        return BookDTO.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle())
                .category(categories.stream().filter(
                        category -> category.getId().equals(book.getCategoryId())).findFirst().orElseThrow().getName())
                .build();
    }

    private LentBookDTO mapToLentBookDTO(Book book, List<Category> categories, List<Lending> lendings, List<Borrower> borrowers) {
        Lending lending = lendings.stream().filter(l -> l.getBookId().equals(book.getId())).findFirst().orElseThrow();
        Borrower borrower = null;
        if (lending != null)
            borrower = borrowers.stream().filter(u -> u.getId().equals(lending.getBorrowerId())).findFirst().orElse(null);
        return LentBookDTO.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle())
                .category(categories.stream().filter(
                        category -> category.getId().equals(book.getCategoryId())).findFirst().orElseThrow().getName())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName()).dateLent(lending.getDateLent())
                .comment(lending.getComment() != null ? lending.getComment() : "No comment").build();
    }

    private EveryBookDTO mapToEveryBookDTO(Book book, List<Category> categories, List<Lending> lendings, List<Borrower> borrowers) {
        Lending lending = lendings.stream().filter(l -> l.getBookId().equals(book.getId())).findFirst().orElse(null);
        Borrower borrower = null;
        if (lending != null)
            borrower = borrowers.stream().filter(u -> u.getId().equals(lending.getBorrowerId())).findFirst().orElse(null);
        return EveryBookDTO.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle())
                .category(categories.stream().filter(
                        category -> category.getId().equals(book.getCategoryId())).findFirst().orElseThrow().getName())
                .isLent(book.getIsLent())
                .firstName(lending != null ? borrower.getFirstName() : null)
                .lastName(lending != null ? borrower.getLastName() : null)
                .dateLent(lending != null ? lending.getDateLent().toString() : null).build();
    }

    private LendingDTO mapToLendingDTO(Lending lending, List<Borrower> borrowers) {
        Borrower borrower = borrowers.stream().filter(u -> u.getId().equals(lending.getBorrowerId())).findFirst().orElse(null);
        return LendingDTO.builder().id(lending.getId()).bookId(lending.getBookId())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName())
                .dateLent(lending.getDateLent()).dateReturned(lending.getDateReturned())
                .comment(lending.getComment() != null ? lending.getComment() : "No comment").build();
    }
}
