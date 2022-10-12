package com.library.repository;

import com.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JPA repository for managing categories in the database
 *
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    List<Category> findByIdIn(Set<Integer> ids);
}
