package com.library.controller;

import com.library.dto.CategoryBookCountDTO;
import com.library.dto.CategoryDTO;
import com.library.model.Category;
import com.library.service.CategoryService;
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
 * REST controller with endpoints for managing categories
 *
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Api("Categories Management API")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Returns a lists of all categories in the database along with associated books.
     * Accepts GET requests at /api/categories/.
     *
     * @return List of all categories in the database
     */
    @ApiOperation(value = "Lists all categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Categories found")
    })
    @GetMapping(path = "/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Adds a new category to the database.
     * Accepts POST requests at /api/books.
     * Returns HTTP status code 422 if a category with a given name already exists.
     *
     * @param category New category entity
     * @return Created category entity
     */
    @ApiOperation(value = "Creates a category")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Category created"),
            @ApiResponse(code = 400, message = "Invalid provided data"),
            @ApiResponse(code = 422, message = "Category name already used")
    })
    @PostMapping(path = "/categories")
    public ResponseEntity<List<CategoryDTO>> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    /**
     * Returns a list of all categories along with their names and a number of books assigned to each one of them.
     * Accepts GET requests at /api/categories/count/
     *
     * @return List of categories with a number of associated books
     */
    @ApiOperation(value = "Lists all categories with a number of books assigned to them")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Categories found")
    })
    @GetMapping(path = "/categories/count")
    public List<CategoryBookCountDTO> getAllCategoriesWithBookCount() {
        return categoryService.getAllCategoriesWithBookCount();
    }

    /**
     *      * Removes a category from the database by its id.
     * Accepts DELETE requests at /api/categories/{id}.
     * Returns HTTP status code 404 if a category with a given id doesn't exist
     * Returns HTTP status code 422 if a category with a given id is one of the starting ones.
     *
     * @param id Id of a category to be deleted
     */
    @ApiOperation(value = "Deletes a category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category deleted"),
            @ApiResponse(code = 404, message = "Category not found"),
            @ApiResponse(code = 422, message = "Can't delete a starting category")
    })
    @DeleteMapping(path = "/categories/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
        return categoryService.deleteCategoryById(id);
    }

    /**
     * Removes a category from the database by its name.
     * Accepts DELETE requests at /api/categories/{id}.
     * Returns HTTP status code 404 if a category with a given name doesn't exist
     * Returns HTTP status code 422 if a category with a given name is one of the starting ones.
     *
     * @param name Name of a category to be deleted
     */
    @ApiOperation(value = "Deletes a category by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category deleted"),
            @ApiResponse(code = 404, message = "Category not found"),
            @ApiResponse(code = 422, message = "Can't delete a starting category")
    })
    @DeleteMapping(path = "/categories")
    public ResponseEntity<?> deleteCategoryByName(@RequestParam String name) {
        return categoryService.deleteCategoryByName(name);
    }
}
