package com.library.service;

import com.library.dto.CategoryBookCountDTO;
import com.library.dto.CategoryDTO;
import com.library.exceptions.CategoryNotFoundResponse;
import com.library.model.Book;
import com.library.model.Category;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service with methods for managing categories in the database.
 *
 *
 */
@Component
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    /**
     * Returns a lists of all categories in the database along with associated books.
     *
     * @return List of all categories in the database
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Adds a new category to the database.
     *
     * @param category New category entity
     * @return List of all categories
     */
    public ResponseEntity<List<CategoryDTO>> createCategory(Category category) {
        category.setName(category.getName().toLowerCase());
        if (categoryRepository.findByName(category.getName()).isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Category name already used");
        category.setIsStarting(false);
        categoryRepository.save(category);
        return new ResponseEntity<>(
                categoryRepository.findAll().stream().map(this::mapToCategoryDTO).collect(Collectors.toList()),
                HttpStatus.CREATED);
    }

    /**
     * Returns a list of all categories along with their names and a number of books assigned to each one of them.
     *
     * @return List of categories with a number of associated books
     */
    public List<CategoryBookCountDTO> getAllCategoriesWithBookCount() {
        List<Category> categories = categoryRepository.findAll();
        List<Book> books = bookRepository.findAll();
        return categories.stream().map(category -> mapToCategoryBookCountDTO(category, books)).collect(Collectors.toList());
    }

    /**
     * Removes a category from the database by its id.
     *
     * @param id Id of a category to be deleted
     */
    public ResponseEntity<?> deleteCategoryById(Integer id) {
        Optional<Category> foundCategory = categoryRepository.findById(id);
        if (foundCategory.isEmpty()) {
            return new ResponseEntity<>(CategoryNotFoundResponse.builder().timestamp(LocalDateTime.now())
                    .status(404).error("Bad Request").message("Category not found").categories(
                    categoryRepository.findAll().stream().map(this::mapToCategoryDTO).collect(Collectors.toList())).build(),
                    HttpStatus.NOT_FOUND);
        }
        Category category = foundCategory.get();
        if (category.getIsStarting())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't delete a starting category");
        Integer defaultId = categoryRepository.findByName("default").orElseThrow().getId();
        bookRepository.updateCategoryId(id, defaultId);
        categoryRepository.deleteById(id);
        return new ResponseEntity<>(
                categoryRepository.findAll().stream().map(this::mapToCategoryDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    /**
     * Removes a category from the database by its name.
     *
     * @param name Name of a category to be deleted
     */
    public ResponseEntity<?> deleteCategoryByName(String name) {
        Optional<Category> foundCategory = categoryRepository.findByName(name);
        if (foundCategory.isEmpty()) {
            return new ResponseEntity<>(CategoryNotFoundResponse.builder().timestamp(LocalDateTime.now())
                    .status(404).error("Bad Request").message("Category not found").categories(
                            categoryRepository.findAll().stream().map(this::mapToCategoryDTO).collect(Collectors.toList())).build(),
                    HttpStatus.NOT_FOUND);
        }
        Category category = foundCategory.get();
        if (category.getIsStarting())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't delete a starting category");
        Integer id = category.getId();
        Integer defaultId = categoryRepository.findByName("default").orElseThrow().getId();
        bookRepository.updateCategoryId(id, defaultId);
        categoryRepository.deleteById(id);
        return new ResponseEntity<>(
                categoryRepository.findAll().stream().map(this::mapToCategoryDTO).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryDTO.builder().id(category.getId()).name(category.getName())
                .isStarting(category.getIsStarting()).build();
    }

    private CategoryBookCountDTO mapToCategoryBookCountDTO(Category category, List<Book> books) {
        return CategoryBookCountDTO.builder().id(category.getId()).name(category.getName())
                .isStarting(category.getIsStarting()).bookCount(
                        Math.toIntExact(books.stream().filter(b -> b.getCategoryId().equals(category.getId())).count())
                ).build();
    }
}
