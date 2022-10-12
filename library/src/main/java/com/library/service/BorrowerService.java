package com.library.service;

import com.library.dto.BorrowerDTO;
import com.library.model.Borrower;
import com.library.repository.BorrowerRepository;
import com.library.repository.LendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service with methods for managing borrowers in the database.
 *
 *
 */
@Component
@Transactional
@RequiredArgsConstructor
public class BorrowerService {

    private final LendingRepository lendingRepository;

    private final BorrowerRepository borrowerRepository;

    /**
     * Returns a lists of all borrowers in the database.
     *
     * @return List of all borrowers in the database
     */
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream().map(this::mapToBorrowerDTO).collect(Collectors.toList());
    }

    /**
     * Adds a new borrower to the database
     *
     * @param borrower New borrower entity
     * @return List of all borrowers in the database
     */
    public ResponseEntity<List<BorrowerDTO>> createBorrower(Borrower borrower) {
        if (borrowerRepository.findByFirstNameAndLastName(borrower.getFirstName(), borrower.getLastName()).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Borrower already added");

        borrower.setLendings(null);
        borrowerRepository.save(borrower);
        return new ResponseEntity<>(getAllBorrowers(), HttpStatus.CREATED);
    }

    /**
     * Removes a borrower from the database by its id. Throws an exception if the borrower with provided id is currently
     * borrowing something.
     *
     * @param id Id of a borrower to be deleted
     * @return List of all borrowers in the database
     */
    public List<BorrowerDTO> deleteBorrowerById(Integer id) {
        if (lendingRepository.findByBorrowerIdAndDateReturned(id, null).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Borrower currently borrows books");
        borrowerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found")
        );
        borrowerRepository.deleteById(id);
        return getAllBorrowers();
    }

    private BorrowerDTO mapToBorrowerDTO(Borrower borrower) {
        return BorrowerDTO.builder().id(borrower.getId())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName()).build();
    }
}
