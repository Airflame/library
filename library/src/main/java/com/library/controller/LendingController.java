package com.library.controller;

import com.library.dto.LendingHistoryDTO;
import com.library.model.Lending;
import com.library.service.LendingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller with endpoints for managing lendings
 *
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Api("Lendings Management API")
public class LendingController {

    private final LendingService lendingService;

    /**
     * Returns a list of all lendings in the database.
     * Accepts GET requests at /api/lendings/
     *
     * @return List of all lendings in the database
     */
    @ApiOperation(value = "Lists all lendings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lendings found")
    })
    @GetMapping(path = "/lendings")
    public List<Lending> getAll() {
        return lendingService.getAll();
    }

    /**
     * Lends a book with a given author and title to a person with a given first and last name.
     * Accepts POST requests at /api/lendings/data/
     * Returns HTTP status code 422 if a book with a given author and title is currently being lent to someone.
     *
     * @param author    Author of the book to be lent
     * @param title     Title of the book to be lent
     * @param firstName First name of the borrower
     * @param lastName  Last name of the borrower
     * @param dateLent  Starting date of the lending. Gets set to current date if not provided.
     * @param comment   Comment attached to the lending, not required.
     * @return Created lending entity
     */
    @ApiOperation(value = "Lends a book with author and title")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book lent"),
            @ApiResponse(code = 422, message = "Book already lent")
    })
    @PostMapping(path = "/lendings/data")
    public ResponseEntity<Lending> lendWithBookData(
            @RequestParam String author,
            @RequestParam String title,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateLent,
            @RequestParam(required = false) String comment) {
        return lendingService.lendWithBookData(author, title, firstName, lastName, dateLent, comment);
    }

    /**
     * Lends a book with a given id to a person with a given first and last name.
     * Accepts POST requests at /api/lendings/data/
     * Returns HTTP status code 422 if a book with a given id is currently being lent to someone.
     *
     * @param id        Id of the book to be lent
     * @param firstName First name of the borrower
     * @param lastName  Last name of the borrower
     * @param dateLent  Starting date of the lending. Gets set to current date if not provided.
     * @param comment   Comment attached to the lending, not required.
     * @return Created lending entity
     */
    @ApiOperation(value = "Lends a book with its id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book lent"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 422, message = "Book already lent")
    })
    @PostMapping(path = "/lendings/{id}")
    public ResponseEntity<Lending> lendWithBookId(
            @PathVariable Integer id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateLent,
            @RequestParam(required = false) String comment) {
        return lendingService.lendWithBookId(id, firstName, lastName, dateLent, comment);
    }

    /**
     * Gives back a lent book with a given author and title.
     * Accepts PUT requests at /api/lendings/data.
     * Returns HTTP status code 404 if a book with a given author and title doesn't exist.
     * Returns HTTP status code 422 if a book with a given author and title is not currently being lent to anyone.
     *
     * @param author       Author of the book to be returned
     * @param title        Title of the book to be returned
     * @param dateReturned Ending date of the lending. Gets set to current date if not provided.
     * @return Edited lending entity
     */
    @ApiOperation(value = "Returns a lent book with author and title")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book returned"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 422, message = "Book not lent")
    })
    @PutMapping(path = "/lendings/data")
    public ResponseEntity<Lending> returnWithBookData(@RequestParam String author,
                                                      @RequestParam String title,
                                                      @RequestParam(required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReturned) {
        return lendingService.returnWithBookData(author, title, dateReturned);
    }

    /**
     * Gives back a lent book with a given id.
     * Accepts PUT requests at /api/lendings/data.
     * Returns HTTP status code 404 if a book with a given id doesn't exist.
     * Returns HTTP status code 422 if a book with a given id is not currently being lent to anyone.
     *
     * @param id           Id of the book to be returned
     * @param dateReturned Ending date of the lending. Gets set to current date if not provided.
     * @return Edited lending entity
     */
    @ApiOperation(value = "Returns a lent book with its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book returned"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 422, message = "Book not lent")
    })
    @PutMapping(path = "/lendings/{id}")
    public ResponseEntity<Lending> returnWithBookId(@PathVariable Integer id,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReturned) {
        return lendingService.returnWithBookId(id, dateReturned);
    }

    /**
     * Returns a list of all lendings of a given person which includes all past lendings, all current lendings, the id
     * author, title and category name of all currently borrowed books, and the number of currently borrowed books.
     * Accepts GET requests at /lendings/history/.
     *
     * @param firstName First name of the borrower
     * @param lastName  Last name of the borrower
     * @return List of all lendings associated with a person with given personal data
     */
    @ApiOperation(value = "Lists all lendings of a given person")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book returned"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 404, message = "Person not found")
    })
    @GetMapping(path = "/lendings/history")
    public LendingHistoryDTO getLendingHistory(@RequestParam String firstName,
                                               @RequestParam String lastName) {
        return lendingService.getLendingHistory(firstName, lastName);
    }

    /**
     * Removes a given finished lending from the database.
     * Accepts DELETE requests at /api/lendings/{id}.
     * Returns HTTP status code 404 if a lending with a given id doesn't exist.
     * Returns HTTP status code 422 if a lending with a given id hasn't been finished yet.
     *
     * @param id Id of the lending to be deleted
     */
    @ApiOperation(value = "Deletes a given finished lending")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lending deleted"),
            @ApiResponse(code = 404, message = "Lending not found"),
            @ApiResponse(code = 422, message = "Lending not finished")
    })
    @DeleteMapping(path = "/lendings/{id}")
    public void deletePastLendingById(@PathVariable Integer id) {
        lendingService.deletePastLendingById(id);
    }
}