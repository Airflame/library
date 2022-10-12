package com.library.service;

import com.library.dto.CategoryBookCountDTO;
import com.library.dto.CategoryDTO;
import com.library.exceptions.CategoryNotFoundResponse;
import com.library.model.Book;
import com.library.model.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
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

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void getAllCategoriesTest() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("Default").isStarting(true).build());
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> returned = categoryService.getAllCategories();
        Assertions.assertEquals(2, returned.size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getId(), categories.get(i).getId());
            Assertions.assertEquals(returned.get(i).getName(), categories.get(i).getName());
        }
    }

    @Test
    public void createCategoryTest() {
        List<Category> categories = new ArrayList<>();
        Category category = Category.builder().name("New").isStarting(false).build();
        categories.add(category);

        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        ResponseEntity<List<CategoryDTO>> responseEntity = categoryService.createCategory(category);
        List<CategoryDTO> returned = responseEntity.getBody();
        int returnedCode = responseEntity.getStatusCode().value();
        Assertions.assertEquals(returned.get(0).getName(), category.getName());
        Assertions.assertEquals(returnedCode, 201);
    }

    @Test
    public void createDuplicateCategoryTest() {
        Mockito.when(categoryRepository.findByName("default")).thenReturn(Optional.of(
                Category.builder().name("default").isStarting(true).build()
        ));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            categoryService.createCategory(Category.builder().name("default").build());
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void getAllCategoriesWithBookCountTest() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id(1).author("autor1").title("tytul1").isLent(true).categoryId(1).build());
        books.add(Book.builder().id(2).author("autor2").title("tytul2").isLent(true).categoryId(2).build());
        books.add(Book.builder().id(3).author("autor3").title("tytul3").isLent(false).categoryId(2).build());

        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("Default").isStarting(true).build());
        categories.add(Category.builder().id(2).name("Programming").isStarting(true).build());

        Mockito.when(bookRepository.findAll()).thenReturn(books);
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryBookCountDTO> categoriesBookCount = new ArrayList<>();
        categoriesBookCount.add(CategoryBookCountDTO.builder().id(1).name("Default")
                .bookCount(1).isStarting(true).build());
        categoriesBookCount.add(CategoryBookCountDTO.builder().id(2).name("Programming")
                .bookCount(2).isStarting(true).build());

        List<CategoryBookCountDTO> returned = categoryService.getAllCategoriesWithBookCount();
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(returned.get(i).getId(), categoriesBookCount.get(i).getId());
            Assertions.assertEquals(returned.get(i).getName(), categoriesBookCount.get(i).getName());
            Assertions.assertEquals(returned.get(i).getIsStarting(), categoriesBookCount.get(i).getIsStarting());
            Assertions.assertEquals(returned.get(i).getBookCount(), categoriesBookCount.get(i).getBookCount());
        }
    }

    @Test
    public void deleteCategoryByIdTest() {
        Category defaultCategory = Category.builder().id(1).name("default").isStarting(true).build();
        Category category = Category.builder().id(5).name("New").isStarting(false).build();

        Mockito.when(categoryRepository.findById(5)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findByName("default")).thenReturn(Optional.of(defaultCategory));

        categoryService.deleteCategoryById(5);
    }

    @Test
    public void deleteCategoryByNameTest() {
        Category defaultCategory = Category.builder().id(1).name("default").isStarting(true).build();
        Category category = Category.builder().id(5).name("new").isStarting(false).build();

        Mockito.when(categoryRepository.findByName("new")).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findByName("default")).thenReturn(Optional.of(defaultCategory));

        categoryService.deleteCategoryByName("new");
    }

    @Test
    public void deleteStartingCategoryByIdTest() {
        Category category = Category.builder().id(1).name("Default").isStarting(true).build();

        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            categoryService.deleteCategoryById(1);
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deleteStartingCategoryByNameTest() {
        Category category = Category.builder().id(1).name("Default").isStarting(true).build();

        Mockito.when(categoryRepository.findByName("Default")).thenReturn(Optional.of(category));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            categoryService.deleteCategoryByName("Default");
        });
        Assertions.assertEquals(exception.getStatus().value(), 422);
    }

    @Test
    public void deleteCategoryNotFoundByIdTest() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1).name("default").isStarting(true).build());

        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        ResponseEntity<?> returned = categoryService.deleteCategoryById(1);
        Assertions.assertEquals(returned.getStatusCode(), HttpStatus.NOT_FOUND);
        CategoryNotFoundResponse response = (CategoryNotFoundResponse) returned.getBody();
        Assertions.assertEquals(response.getStatus(), 404);
        Assertions.assertEquals(response.getError(), "Bad Request");
        Assertions.assertEquals(response.getMessage(), "Category not found");
        Assertions.assertNotNull(response.getTimestamp());
        Assertions.assertEquals(response.getCategories().get(0).getId(), categories.get(0).getId());
        Assertions.assertEquals(response.getCategories().get(0).getName(), categories.get(0).getName());
        Assertions.assertEquals(response.getCategories().get(0).getIsStarting(), categories.get(0).getIsStarting());
    }

    @Test
    public void deleteCategoryNotFoundByNameTest() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().name("default").isStarting(false).build());
        Category c = new Category();
        Set<Book> books = c.getBooks();

        Mockito.when(categoryRepository.findByName("new")).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        ResponseEntity<?> returned = categoryService.deleteCategoryByName("new");
        Assertions.assertEquals(returned.getStatusCode(), HttpStatus.NOT_FOUND);
        CategoryNotFoundResponse response = (CategoryNotFoundResponse) returned.getBody();
        Assertions.assertEquals(response.getStatus(), 404);
        Assertions.assertEquals(response.getError(), "Bad Request");
        Assertions.assertEquals(response.getMessage(), "Category not found");
        Assertions.assertNotNull(response.getTimestamp());
        Assertions.assertEquals(response.getCategories().get(0).getId(), categories.get(0).getId());
        Assertions.assertEquals(response.getCategories().get(0).getName(), categories.get(0).getName());
        Assertions.assertEquals(response.getCategories().get(0).getIsStarting(), categories.get(0).getIsStarting());
    }
}
