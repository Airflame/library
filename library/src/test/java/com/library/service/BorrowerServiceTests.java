package com.library.service;

import com.library.dto.BorrowerDTO;
import com.library.model.Borrower;
import com.library.model.Lending;
import com.library.repository.BorrowerRepository;
import com.library.repository.LendingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BorrowerServiceTests {

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private LendingRepository lendingRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    @Test
    public void getAllBorrowersTest() {
        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("John").lastName("Smith").lendings(null).build());
        borrowers.add(Borrower.builder().id(2).firstName("Jane").lastName("Doe").lendings(null).build());
        Borrower b = new Borrower();
        Set<Lending> lendings = b.getLendings();

        Mockito.when(borrowerRepository.findAll()).thenReturn(borrowers);

        List<BorrowerDTO> returned = borrowerService.getAllBorrowers();
        Assertions.assertEquals(returned.size(), 2);
        for (int i = 0; i < 2; i ++ ) {
            Assertions.assertEquals(returned.get(i).getId(), borrowers.get(i).getId());
            Assertions.assertEquals(returned.get(i).getFirstName(), borrowers.get(i).getFirstName());
        }
    }

    @Test
    public void createBorrowerTest() {
        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("John").lastName("Smith").build());
        borrowers.add(Borrower.builder().id(2).firstName("Jane").lastName("Doe").build());
        Borrower borrower = Borrower.builder().firstName("Jane").lastName("Doe").build();

        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(Optional.empty());
        Mockito.when(borrowerRepository.findAll()).thenReturn(borrowers);

        ResponseEntity<List<BorrowerDTO>> response = borrowerService.createBorrower(borrower);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        List<BorrowerDTO> returned = response.getBody();
        Assertions.assertEquals(returned.size(), 2);
        for (int i = 0; i < 2; i ++ ) {
            Assertions.assertEquals(returned.get(i).getId(), borrowers.get(i).getId());
            Assertions.assertEquals(returned.get(i).getFirstName(), borrowers.get(i).getFirstName());
            Assertions.assertEquals(returned.get(i).getLastName(), borrowers.get(i).getLastName());
        }
    }

    @Test
    public void createDuplicateBorrowerTest() {
        Borrower borrower = Borrower.builder().firstName("Jane").lastName("Doe").build();

        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(Optional.of(borrower));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                borrowerService.createBorrower(borrower)
        );
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deleteBorrowerByIdTest() {
        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("John").lastName("Smith").build());
        Borrower borrower = Borrower.builder().id(2).firstName("Jane").lastName("Doe").build();

        Mockito.when(lendingRepository.findByBorrowerIdAndDateReturned(2, null))
                .thenReturn(Optional.empty());
        Mockito.when(borrowerRepository.findById(2)).thenReturn(Optional.of(borrower));

        borrowerService.deleteBorrowerById(2);
    }

    @Test
    public void deleteBorrowingBorrowerByIdTest() {
        Mockito.when(lendingRepository.findByBorrowerIdAndDateReturned(2, null))
                .thenReturn(Optional.of(Lending.builder().id(1).build()));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                borrowerService.deleteBorrowerById(2)
        );
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deleteNotFoundBorrowerByIdTest() {
        Mockito.when(lendingRepository.findByBorrowerIdAndDateReturned(1, null))
                .thenReturn(Optional.empty());
        Mockito.when(borrowerRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                borrowerService.deleteBorrowerById(1)
        );
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }
}
