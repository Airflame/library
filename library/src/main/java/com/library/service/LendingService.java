package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.LendingDTO;
import com.library.dto.LendingHistoryDTO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service with methods for managing lendings in the database.
 *
 *
 */
@Component
@Transactional
@RequiredArgsConstructor
public class LendingService {

    private final LendingRepository lendingRepository;

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final BorrowerRepository borrowerRepository;

    /**
     * Returns a list of all lendings in the database.
     *
     * @return List of all lendings in the database
     */
    public List<Lending> getAll() {
        return lendingRepository.findAll();
    }

    /**
     * Lends a book with a given author and title to a person with a given first and last name.
     *
     * @param author    Author of the book to be lent
     * @param title     Title of the book to be lent
     * @param firstName First name of the borrower
     * @param lastName  Last name of the borrower
     * @param dateLent  Starting date of the lending. Gets set to current date if not provided.
     * @param comment   Comment attached to the lending, not required.
     * @return Created lending entity
     */
    public ResponseEntity<Lending> lendWithBookData(String author, String title, String firstName, String lastName,
                                                    LocalDate dateLent, String comment) {
        Book book = bookRepository.findByAuthorAndTitle(author, title).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        });
        Borrower borrower = borrowerRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found");
        });
        if (book.getIsLent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book already lent");
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(borrower.getId())
                .dateLent(dateLent != null ? dateLent : LocalDate.now()).comment(comment).build();
        Lending savedLending = lendingRepository.save(lending);
        book.setIsLent(true);
        return new ResponseEntity<>(savedLending, HttpStatus.CREATED);
    }

    /**
     * Lends a book with a given id to a person with a given first and last name.
     *
     * @param id        Id of the book to be lent
     * @param firstName First name of the borrower
     * @param lastName  Last name of the borrower
     * @param dateLent  Starting date of the lending. Gets set to current date if not provided.
     * @param comment   Comment attached to the lending, not required.
     * @return Created lending entity
     */
    public ResponseEntity<Lending> lendWithBookId(Integer id, String firstName, String lastName,
                                                  LocalDate dateLent, String comment) {
        Book book = bookRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        });
        Borrower borrower = borrowerRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found");
        });
        if (book.getIsLent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book already lent");
        Lending lending = Lending.builder().bookId(id).borrowerId(borrower.getId())
                .dateLent(dateLent != null ? dateLent : LocalDate.now()).comment(comment).build();
        Lending savedLending = lendingRepository.save(lending);
        book.setIsLent(true);
        return new ResponseEntity<>(savedLending, HttpStatus.CREATED);
    }

    /**
     * Gives back a lent book with a given author and title.
     *
     * @param author       Author of the book to be returned
     * @param title        Title of the book to be returned
     * @param dateReturned Ending date of the lending. Gets set to current date if not provided.
     * @return Edited lending entity
     */
    public ResponseEntity<Lending> returnWithBookData(String author, String title, LocalDate dateReturned) {
        Book book = bookRepository.findByAuthorAndTitle(author, title).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        });
        Lending lending = lendingRepository.findByBookIdAndDateReturned(book.getId(), null).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book not lent");
        });
        if ((dateReturned != null ? dateReturned : LocalDate.now()).isBefore(lending.getDateLent()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book returned before lent");
        lending.setDateReturned(dateReturned != null ? dateReturned : LocalDate.now());
        book.setIsLent(false);
        return new ResponseEntity<>(lending, HttpStatus.OK);
    }

    /**
     * Gives back a lent book with a given id.
     *
     * @param id           Id of the book to be returned
     * @param dateReturned Ending date of the lending. Gets set to current date if not provided.
     * @return Edited lending entity
     */
    public ResponseEntity<Lending> returnWithBookId(Integer id, LocalDate dateReturned) {
        Book book = bookRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        });
        Lending lending = lendingRepository.findByBookIdAndDateReturned(book.getId(), null).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Book not lent");
        });
        if ((dateReturned != null ? dateReturned : LocalDate.now()).isBefore(lending.getDateLent()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book returned before lent");
        lending.setDateReturned(dateReturned != null ? dateReturned : LocalDate.now());
        book.setIsLent(false);
        return new ResponseEntity<>(lending, HttpStatus.OK);
    }

    /**
     * Returns a list of all lendings of a given person which includes all past lendings, all current lendings, the id
     * author, title and category name of all currently borrowed books along with the total number of them.
     *
     * @param firstName First name of the borrower
     * @param lastName Last name of the borrower
     * @return List of all lendings associated with a person with given personal data
     */
    public LendingHistoryDTO getLendingHistory(String firstName, String lastName) {
        Borrower borrower = borrowerRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found");
        });
        List<Lending> lendings = lendingRepository.findByBorrowerId(borrower.getId());
        List<Lending> pastLendings = lendings.stream().filter(
                l -> l.getDateReturned() != null).collect(Collectors.toList());
        List<Lending> currentLendings = lendings.stream().filter(
                l -> l.getDateReturned() == null).collect(Collectors.toList());
        List<Book> currentBooks = bookRepository.findByIdIn(
                currentLendings.stream().map(Lending::getBookId).collect(Collectors.toSet()));
        Set<Integer> categoryIds = currentBooks.stream().map(Book::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryRepository.findByIdIn(categoryIds);
        return LendingHistoryDTO.builder().firstName(firstName).lastName(lastName)
                .pastLendings(pastLendings.stream().map(l -> mapToLendingDTO(l, borrower))
                        .collect(Collectors.toList()))
                .currentLendings(currentLendings.stream().map(l -> mapToLendingDTO(l, borrower))
                        .collect(Collectors.toList()))
                .lentBooks(currentBooks.stream().map(book -> mapToBookDTO(book, categories)).collect(Collectors.toList()))
                .bookCount(currentBooks.size()).build();
    }

    /**
     * Removes a given finished lending from the database.
     *
     * @param id Id of the lending to be deleted
     */
    public void deletePastLendingById(Integer id) {
        Lending lending = lendingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Lending not found"));
        if (lending.getDateReturned() == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lending not finished");
        lendingRepository.deleteById(id);
    }

    private BookDTO mapToBookDTO(Book book, List<Category> categories) {
        return BookDTO.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle())
                .category(categories.stream().filter(
                        category -> category.getId().equals(book.getCategoryId())).findFirst().orElseThrow().getName())
                .build();
    }

    private LendingDTO mapToLendingDTO(Lending lending, Borrower borrower) {
        return LendingDTO.builder().id(lending.getId()).bookId(lending.getBookId())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName())
                .dateLent(lending.getDateLent()).dateReturned(lending.getDateReturned())
                .comment(lending.getComment() != null ? lending.getComment() : "No comment" ).build();
    }
}
