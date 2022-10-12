package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JPA repository for managing books in the database
 *
 *
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByAuthorAndTitle(String author, String title);

    List<Book> findByIsLent(Boolean isLent);

    List<Book> findByIdIn(Set<Integer> ids);

    @Modifying
    @Query("UPDATE Book b SET b.categoryId = :newId WHERE b.categoryId = :oldId")
    void updateCategoryId(@Param("oldId") int oldId, @Param("newId") int newId);
}
