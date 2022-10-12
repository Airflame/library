package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.LendingDTO;
import com.library.dto.LendingHistoryDTO;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.model.Category;
import com.library.model.Lending;
import com.library.repository.BookRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.LendingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class LendingServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LendingRepository lendingRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private LendingService lendingService;

    @Test
    public void getAllLendingsTest() {
        List<Lending> lendings = new ArrayList<>();
        lendings.add(Lending.builder().id(1).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1))
                .dateReturned(LocalDate.of(2020, 7, 1)).build());
        lendings.add(Lending.builder().id(2).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2021, 1, 2)).build());

        Mockito.when(lendingRepository.findAll()).thenReturn(lendings);

        List<Lending> returned = lendingService.getAll();
        for (int i = 0; i < lendings.size(); i++) {
            Assertions.assertEquals(lendings.get(i).getId(), returned.get(i).getId());
            Assertions.assertEquals(lendings.get(i).getBookId(), returned.get(i).getBookId());
            Assertions.assertEquals(lendings.get(i).getDateLent(), returned.get(i).getDateLent());
            Assertions.assertEquals(lendings.get(i).getDateReturned(), returned.get(i).getDateReturned());
            Assertions.assertEquals(lendings.get(i).getComment(), returned.get(i).getComment());
            Assertions.assertEquals(lendings.get(i).getBorrowerId(), returned.get(i).getBorrowerId());
        }

    }

    @Test
    public void lendLentBookTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();

        Borrower borrower = Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jan", "Kowalski")).thenReturn(Optional.of(borrower));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.lendWithBookId(1, "Jan", "Kowalski", null, null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
        exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.lendWithBookData("autor1", "tytul1",
                    "Jan", "Kowalski", null, null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void lendBookWithAuthorAndTitleTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(false).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Borrower borrower = Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build();

        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.save(lending)).thenReturn(lending);
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jan", "Kowalski")).thenReturn(Optional.of(borrower));

        ResponseEntity<Lending> responseEntity = lendingService.lendWithBookData("autor1", "tytul1",
                "Jan", "Kowalski", LocalDate.of(2020, 6, 1), "test");

        Lending returnedLending = responseEntity.getBody();
        int returnedCode = responseEntity.getStatusCode().value();
        Assertions.assertEquals(returnedLending.getId(), lending.getId());
        Assertions.assertEquals(returnedLending.getBookId(), lending.getBookId());
        Assertions.assertEquals(returnedLending.getBorrowerId(), lending.getBorrowerId());
        Assertions.assertEquals(returnedLending.getDateLent(), lending.getDateLent());
        Assertions.assertEquals(returnedLending.getComment(), lending.getComment());
        Assertions.assertEquals(returnedCode, 201);
        Assertions.assertEquals(book.getIsLent(), true);
    }

    @Test
    public void lendBookWithAuthorAndTitleNotFoundTest() {
        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.lendWithBookData("autor1", "tytul1",
                    "imie1", "nazwisko1", null, null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void lendBookWithAuthorAndTitleBorrowerNotFoundTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(false).categoryId(1).build();

        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("imie1", "nazwisko1")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.lendWithBookData("autor1", "tytul1",
                    "imie1", "nazwisko1", null, null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void lendBookWithIdTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(false).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Borrower borrower = Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.save(lending)).thenReturn(lending);
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jan", "Kowalski")).thenReturn(Optional.of(borrower));

        ResponseEntity<Lending> responseEntity = lendingService.lendWithBookId(1, "Jan", "Kowalski",
                LocalDate.of(2020, 6, 1), "test");

        Lending returnedLending = responseEntity.getBody();
        int returnedCode = responseEntity.getStatusCode().value();
        Assertions.assertEquals(returnedLending.getId(), lending.getId());
        Assertions.assertEquals(returnedLending.getBookId(), lending.getBookId());
        Assertions.assertEquals(returnedLending.getBorrowerId(), lending.getBorrowerId());
        Assertions.assertEquals(returnedLending.getDateLent(), lending.getDateLent());
        Assertions.assertEquals(returnedLending.getComment(), lending.getComment());
        Assertions.assertEquals(returnedCode, 201);
        Assertions.assertEquals(book.getIsLent(), true);
    }

    @Test
    public void lendBookWithIdNotFoundTest() {
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                lendingService.lendWithBookId(1, "imie1", "nazwisko1", null, null));
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void lendBookWithIdBorrowerNotFoundTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(false).categoryId(1).build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("imie1", "nazwisko1")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                lendingService.lendWithBookId(1,
                "imie1", "nazwisko1", null, null));
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void returnNotLentBookTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookId(1, LocalDate.of(2020, 7, 1));
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
        exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookData("autor1", "tytul1", LocalDate.of(2020, 7, 1));
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void returnBookWithAuthorAndTitleTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.findByBookIdAndDateReturned(book.getId(), null)).thenReturn(
                Optional.of(lending));

        ResponseEntity<Lending> responseEntity = lendingService.returnWithBookData("autor1", "tytul1",
                LocalDate.of(2020, 7, 1));
        Assertions.assertEquals(lending.getDateReturned(), LocalDate.of(2020, 7, 1));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 200);
        Assertions.assertNotNull(lending.getDateReturned());
    }

    @Test
    public void returnBookWithAuthorAndTitleNotFoundTest() {
        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookData("autor1", "tytul1", null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void returnBookWithAuthorAndTitleBeforeLentTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Mockito.when(bookRepository.findByAuthorAndTitle("autor1", "tytul1")).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.findByBookIdAndDateReturned(book.getId(), null)).thenReturn(
                Optional.of(lending));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookData("autor1", "tytul1", LocalDate.of(2020,5,1));
        });
        Assertions.assertEquals(exception.getStatus().value(), 400);
    }

    @Test
    public void returnBookWithIdTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.findByBookIdAndDateReturned(book.getId(), null)).thenReturn(
                Optional.of(lending));

        ResponseEntity<Lending> responseEntity = lendingService.returnWithBookId(
                1, LocalDate.of(2020, 7, 1));

        Assertions.assertEquals(lending.getDateReturned(), LocalDate.of(2020, 7, 1));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 200);
        Assertions.assertNotNull(lending.getDateReturned());
    }

    @Test
    public void returnBookWithIdNotFoundTest() {
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookId(1, null);
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void returnBookWithIdBeforeLentTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();
        Lending lending = Lending.builder().bookId(book.getId()).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).comment("test").build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(lendingRepository.findByBookIdAndDateReturned(1, null))
                .thenReturn(Optional.of(lending));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.returnWithBookId(1, LocalDate.of(2020, 5, 1));
        });
        Assertions.assertEquals(exception.getStatus().value(), 400);
    }

    @Test
    public void getLendingHistoryTest() {
        List<Lending> lendings = new ArrayList<>();
        lendings.add(Lending.builder().id(1).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1))
                .dateReturned(LocalDate.of(2020, 7, 1)).build());
        lendings.add(Lending.builder().id(2).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2021, 1, 2)).build());
        List<Lending> pastLendings = lendings.stream().filter(
                l -> l.getDateReturned() != null).collect(Collectors.toList());
        List<Lending> currentLendings = lendings.stream().filter(
                l -> l.getDateReturned() == null).collect(Collectors.toList());

        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(2).author("autor2").title("tytul2").isLent(true).categoryId(2).build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());

        Borrower borrower = Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build();

        Mockito.when(lendingRepository.findByBorrowerId(1)).thenReturn(lendings);
        Mockito.when(bookRepository.findByIdIn(Set.of(2))).thenReturn(books);
        Mockito.when(categoryRepository.findByIdIn(Set.of(2))).thenReturn(categories);
        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jan", "Kowalski")).thenReturn(Optional.of(borrower));

        List<BookDTO> lentBooks = new ArrayList<>();
        lentBooks.add(BookDTO.builder().id(2).author("autor2").title("tytul2").category("Programming").build());

        LendingHistoryDTO lendingHistory = LendingHistoryDTO.builder().firstName("Jan").lastName("Kowalski")
                .pastLendings(pastLendings.stream().map(l -> mapToLendingDTO(l, borrower)).collect(Collectors.toList()))
                .currentLendings(currentLendings.stream().map(l -> mapToLendingDTO(l, borrower)).collect(Collectors.toList()))
                .lentBooks(lentBooks).bookCount(1).build();

        LendingHistoryDTO returned = lendingService.getLendingHistory("Jan", "Kowalski");
        Assertions.assertEquals(returned.getPastLendings(), lendingHistory.getPastLendings());
        Assertions.assertEquals(returned.getCurrentLendings(), lendingHistory.getCurrentLendings());
        Assertions.assertEquals(returned.getLentBooks(), lendingHistory.getLentBooks());
        Assertions.assertEquals(returned.getFirstName(), lendingHistory.getFirstName());
        Assertions.assertEquals(returned.getLastName(), lendingHistory.getLastName());
        Assertions.assertEquals(returned.getBookCount(), lendingHistory.getBookCount());
    }

    @Test
    public void getLendingHistoryNotFoundTest() {
        Borrower borrower = Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build();

        Mockito.when(borrowerRepository.findByFirstNameAndLastName("Jan", "Kowalski")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.getLendingHistory("Jan", "Kowalski");
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    @Test
    public void deletePastLendingTest() {
        Lending lending = new Lending();
        lending = Lending.builder().id(2).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2021, 1, 2))
                .dateReturned(LocalDate.of(2021,2,2)).build();

        Mockito.when(lendingRepository.findById(2)).thenReturn(Optional.of(lending));

        lendingService.deletePastLendingById(2);
    }

    @Test
    public void deleteCurrentLendingTest() {
        Lending lending = Lending.builder().id(2).bookId(2).borrowerId(1)
                .dateLent(LocalDate.of(2021, 1, 2)).build();

        Mockito.when(lendingRepository.findById(2)).thenReturn(Optional.of(lending));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.deletePastLendingById(2);
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deletePastLendingByIdNotFoundTest() {
        Mockito.when(lendingRepository.findById(2)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            lendingService.deletePastLendingById(2);
        });
        Assertions.assertEquals(exception.getStatus().value(), 404);
    }

    private LendingDTO mapToLendingDTO(Lending lending, Borrower borrower) {
        return LendingDTO.builder().id(lending.getId()).bookId(lending.getBookId())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName())
                .dateLent(lending.getDateLent()).dateReturned(lending.getDateReturned())
                .comment(lending.getComment() != null ? lending.getComment() : "No comment" ).build();
    }
}
