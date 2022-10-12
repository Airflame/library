package com.library.service;

import com.library.dto.*;
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
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LendingRepository lendingRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void getAllBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1).author("autor1").title("tytul1").categoryId(1).build());
        books.add(Book.builder().id(2).author("autor2").title("tytul2").categoryId(2).build());

        Mockito.when(bookRepository.findAll()).thenReturn(books);

        List<Book> returned = bookService.getAllBooks();
        Assertions.assertEquals(2, returned.size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getAuthor(), books.get(i).getAuthor());
            Assertions.assertEquals(returned.get(i).getTitle(), books.get(i).getTitle());
            Assertions.assertEquals(returned.get(i).getCategoryId(), books.get(i).getCategoryId());
        }
    }

    @Test
    public void createBookTest() {
        Book book = Book.builder().author("autor").title("tytul").isLent(false).categoryId(1).build();

        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(categoryRepository.findByName("default")).thenReturn(
                Optional.of(Category.builder().id(1).name("default").isStarting(true).build()));

        ResponseEntity<Book> responseEntity = bookService.createBook(book.getAuthor(), book.getTitle(), "default");
        Book returnedBook = responseEntity.getBody();
        int returnedCode = responseEntity.getStatusCode().value();
        Assertions.assertEquals(returnedBook.getAuthor(), book.getAuthor());
        Assertions.assertEquals(returnedBook.getTitle(), book.getTitle());
        Assertions.assertEquals(returnedBook.getCategoryId(), book.getCategoryId());
        Assertions.assertEquals(returnedCode, 201);
    }

    @Test
    public void createBookDuplicateTest() {
        Book book = Book.builder().author("autor").title("tytul").isLent(false).categoryId(1).build();

        Mockito.when(bookRepository.findByAuthorAndTitle("autor", "tytul")).thenReturn(Optional.of(book));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            bookService.createBook("autor", "tytul", "Default");
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deleteBookByIdTest() {
        Book book = Book.builder().id(1).author("autor").title("tytul").isLent(false).categoryId(1).build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1);
    }

    @Test
    public void deleteLentBookByIdTest() {
        Book book = new Book();
        book = Book.builder().id(1).author("autor").title("tytul").isLent(true).categoryId(1).build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            bookService.deleteBookById(1);
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void getAvailableBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1).author("autor1").title("tytul1").isLent(false).categoryId(1).build());
        books.add(Book.builder().id(2).author("autor2").title("tytul2").isLent(false).categoryId(2).build());
        books.add(Book.builder().id(2).author("autor3").title("tytul3").isLent(true).categoryId(3).build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("Default").isStarting(true).build());
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());

        List<BookDTO> available = new ArrayList<>();
        available.add(BookDTO.builder().id(1).author("autor1").title("tytul1").category("Default").build());
        available.add(BookDTO.builder().id(2).author("autor2").title("tytul2").category("Programming").build());

        Set<Integer> categoryIds = Set.of(1, 2);
        Mockito.when(bookRepository.findByIsLent(false)).thenReturn(
                books.stream().filter(b -> !b.getIsLent()).collect(Collectors.toList()));
        Mockito.when(categoryRepository.findByIdIn(categoryIds)).thenReturn(categories);

        List<BookDTO> returned = bookService.getAvailableBooks();
        Assertions.assertEquals(2, returned.size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getAuthor(), available.get(i).getAuthor());
            Assertions.assertEquals(returned.get(i).getTitle(), available.get(i).getTitle());
            Assertions.assertEquals(returned.get(i).getCategory(), available.get(i).getCategory());
        }
    }

    @Test
    public void getLentBookTest() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build());
        books.add(Book.builder().id(2).author("autor2").title("tytul2").isLent(true).categoryId(2).build());
        books.add(Book.builder().id(2).author("autor3").title("tytul3").isLent(false).categoryId(3).build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("Default").isStarting(true).build());
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());

        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build());
        borrowers.add(Borrower.builder().id(2).firstName("Adam").lastName("Nowak").build());

        List<Lending> lendings = new ArrayList<>();
        lendings.add(Lending.builder().id(1).bookId(1).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).build());
        lendings.add(Lending.builder().id(2).bookId(2).borrowerId(2)
                .dateLent(LocalDate.of(2021, 1, 2)).build());

        Set<Integer> categoryIds = Set.of(1, 2);
        Mockito.when(bookRepository.findByIsLent(true)).thenReturn(
                books.stream().filter(Book::getIsLent).collect(Collectors.toList()));
        Mockito.when(categoryRepository.findByIdIn(categoryIds)).thenReturn(categories);
        Mockito.when(lendingRepository.findByDateReturned(null)).thenReturn(lendings);
        Mockito.when(borrowerRepository.findAll()).thenReturn(borrowers);

        List<LentBookDTO> lentBooks = new ArrayList<>();
        lentBooks.add(LentBookDTO.builder().id(1).author("autor1").title("tytul1").category("Default")
                .firstName("Jan").lastName("Kowalski").dateLent(LocalDate.of(2020, 6, 1)).build());
        lentBooks.add(LentBookDTO.builder().id(2).author("autor2").title("tytul2").category("Programming")
                .firstName("Adam").lastName("Nowak").dateLent(LocalDate.of(2021, 1, 2)).build());

        List<LentBookDTO> returned = bookService.getLentBooks();
        Assertions.assertEquals(2, returned.size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getId(), lentBooks.get(i).getId());
            Assertions.assertEquals(returned.get(i).getAuthor(), lentBooks.get(i).getAuthor());
            Assertions.assertEquals(returned.get(i).getTitle(), lentBooks.get(i).getTitle());
            Assertions.assertEquals(returned.get(i).getCategory(), lentBooks.get(i).getCategory());
            Assertions.assertEquals(returned.get(i).getFirstName(), lentBooks.get(i).getFirstName());
            Assertions.assertEquals(returned.get(i).getLastName(), lentBooks.get(i).getLastName());
            Assertions.assertEquals(returned.get(i).getDateLent(), lentBooks.get(i).getDateLent());
            Assertions.assertEquals(returned.get(i).getComment(), "No comment");
        }
    }

    @Test
    public void getAvailableAndLentBooksTest() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build());
        books.add(Book.builder().id(2).author("autor2").title("tytul2").isLent(true).categoryId(2).build());
        books.add(Book.builder().id(3).author("autor3").title("tytul3").isLent(false).categoryId(3).build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("Default").isStarting(true).build());
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());
        categories.add(Category.builder().id(3).name("Management").isStarting(true).build());

        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build());
        borrowers.add(Borrower.builder().id(2).firstName("Adam").lastName("Nowak").build());

        List<Lending> lendings = new ArrayList<>();
        lendings.add(Lending.builder().id(1).bookId(1).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1)).build());
        lendings.add(Lending.builder().id(2).bookId(2).borrowerId(2)
                .dateLent(LocalDate.of(2021, 1, 2)).build());

        Set<Integer> categoryIds = Set.of(1, 2, 3);
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        Mockito.when(borrowerRepository.findAll()).thenReturn(borrowers);
        Mockito.when(categoryRepository.findByIdIn(categoryIds)).thenReturn(categories);
        Mockito.when(lendingRepository.findByDateReturned(null)).thenReturn(lendings);

        List<EveryBookDTO> allBooks = new ArrayList<>();
        allBooks.add(EveryBookDTO.builder().id(1).author("autor1").title("tytul1").category("Default").isLent(true)
                .firstName("Jan").lastName("Kowalski").dateLent(LocalDate.of(2020, 6, 1)).build());
        allBooks.add(EveryBookDTO.builder().id(2).author("autor2").title("tytul2").category("Programming").isLent(true)
                .firstName("Adam").lastName("Nowak").dateLent(LocalDate.of(2021, 1, 2)).build());
        allBooks.add(EveryBookDTO.builder().id(3).author("autor3").title("tytul3").category("Management").isLent(false)
                .build());

        List<EveryBookDTO> returned = bookService.getAvailableAndLentBooks();
        Assertions.assertEquals(3, returned.size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getId(), allBooks.get(i).getId());
            Assertions.assertEquals(returned.get(i).getAuthor(), allBooks.get(i).getAuthor());
            Assertions.assertEquals(returned.get(i).getTitle(), allBooks.get(i).getTitle());
            Assertions.assertEquals(returned.get(i).getCategory(), allBooks.get(i).getCategory());
            Assertions.assertEquals(returned.get(i).getFirstName(), allBooks.get(i).getFirstName());
            Assertions.assertEquals(returned.get(i).getLastName(), allBooks.get(i).getLastName());
            Assertions.assertEquals(returned.get(i).getDateLent(), allBooks.get(i).getDateLent());
            Assertions.assertEquals(returned.get(i).getIsLent(), allBooks.get(i).getIsLent());
        }
    }

    @Test
    public void getBookHistoryTest() {
        Book book = Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build();

        List<Borrower> borrowers = new ArrayList<>();
        borrowers.add(Borrower.builder().id(1).firstName("Jan").lastName("Kowalski").build());
        borrowers.add(Borrower.builder().id(2).firstName("Adam").lastName("Nowak").build());

        List<Lending> currentLendings = new ArrayList<>();
        currentLendings.add(Lending.builder().id(1).bookId(1).borrowerId(1)
                .dateLent(LocalDate.of(2020, 6, 1))
                .dateReturned(LocalDate.of(2020, 7, 1)).build());
        currentLendings.add(Lending.builder().id(2).bookId(1).borrowerId(2)
                .dateLent(LocalDate.of(2021, 1, 2)).build());
        Set<Lending> lendings = new HashSet<>(currentLendings);
        book.setLendings(new HashSet<>(currentLendings));

        BookHistoryDTO bookHistory = BookHistoryDTO.builder().id(1).isLent(true).author("autor1").title("tytul1")
                .lendings(currentLendings.stream().map(l -> mapToLendingDTO(l, borrowers))
                        .collect(Collectors.toList())).build();

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(
                Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1)
                        .lendings(lendings).build()));
        Mockito.when(borrowerRepository.findAll()).thenReturn(borrowers);

        BookHistoryDTO returned = bookService.getBookHistory(1);
        Assertions.assertEquals(returned.getId(), bookHistory.getId());
        Assertions.assertEquals(returned.getIsLent(), bookHistory.getIsLent());
        Assertions.assertEquals(returned.getAuthor(), bookHistory.getAuthor());
        Assertions.assertEquals(returned.getTitle(), bookHistory.getTitle());
        Assertions.assertEquals(returned.getLendings().size(), bookHistory.getLendings().size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.getLendings().get(i).getBookId(),
                    bookHistory.getLendings().get(i).getBookId());
            Assertions.assertEquals(returned.getLendings().get(i).getFirstName(),
                    bookHistory.getLendings().get(i).getFirstName());
            Assertions.assertEquals(returned.getLendings().get(i).getLastName(),
                    bookHistory.getLendings().get(i).getLastName());
            Assertions.assertEquals(returned.getLendings().get(i).getDateLent(),
                    bookHistory.getLendings().get(i).getDateLent());
        }
    }

    private LendingDTO mapToLendingDTO(Lending lending, List<Borrower> borrowers) {
        Borrower borrower = borrowers.stream().filter(u -> u.getId().equals(lending.getBorrowerId())).findFirst().orElse(null);
        return LendingDTO.builder().id(lending.getId()).bookId(lending.getBookId())
                .firstName(borrower.getFirstName()).lastName(borrower.getLastName())
                .dateLent(lending.getDateLent()).dateReturned(lending.getDateReturned())
                .comment(lending.getComment() != null ? lending.getComment() : "No comment").build();
    }
}
