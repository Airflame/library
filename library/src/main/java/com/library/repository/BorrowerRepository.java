package com.library.repository;

import com.library.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {

    Optional<Borrower> findByFirstNameAndLastName(String firstName, String lastName);
}
