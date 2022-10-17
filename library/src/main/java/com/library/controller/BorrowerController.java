package com.library.controller;

import com.library.dto.BorrowerDTO;
import com.library.model.Borrower;
import com.library.service.BorrowerService;
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
 * REST controller with endpoints for managing borrowers
 *
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Api("Borrowers Management API")
public class BorrowerController {

    private final BorrowerService borrowerService;

    /**
     * Returns a lists of all borrowers in the database.
     * Accepts GET requests at /api/borrowers/.
     *
     * @return List of all borrowers in the database
     */
    @ApiOperation(value = "Lists all borrowers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Borrowers found")
    })
    @GetMapping(path = "/borrowers")
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    /**
     * Adds a new borrower to the database.
     * Accepts POST requests at /api/borrowers/.
     * Returns HTTP status code 422 if a borrower with given data already exists.
     *
     * @param borrower New borrower entity
     * @return List of all borrowers in the database
     */
    @ApiOperation(value = "Creates a borrower")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Category created"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 422, message = "Borrower already added")
    })
    @PostMapping(path = "/borrowers")
    public ResponseEntity<List<BorrowerDTO>> createBorrower(@RequestBody Borrower borrower) {
        return borrowerService.createBorrower(borrower);
    }

    /**
     * Removes a borrower from the database by its id.
     * Accepts DELETE requests at /api/borrowers/{id}.
     * Returns HTTP status code 422 if the borrower with a given id is currently borrowing something.
     *
     * @param id Id of a borrower to be deleted
     * @return List of all borrowers in the database
     */
    @ApiOperation(value = "Deletes a borrower by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Borrower deleted"),
            @ApiResponse(code = 404, message = "Borrower not found"),
            @ApiResponse(code = 422, message = "Borrower currently borrows books")
    })
    @DeleteMapping(path = "/borrowers/{id}")
    public List<BorrowerDTO> deleteBorrowerById(@PathVariable Integer id) {
        return borrowerService.deleteBorrowerById(id);
    }
}
