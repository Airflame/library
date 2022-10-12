package com.library.repository;

import com.library.model.Lending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for managing lendings in the database
 *
 *
 */
public interface LendingRepository extends JpaRepository<Lending, Integer> {

    Optional<Lending> findByBookIdAndDateReturned(Integer bookId, LocalDate dateReturned);

    List<Lending> findByDateReturned(LocalDate dateReturned);

    List<Lending> findByBorrowerId(Integer borrowerId);

    Optional<Lending> findByBorrowerIdAndDateReturned(Integer borrowerId, LocalDate dateReturned);
}
