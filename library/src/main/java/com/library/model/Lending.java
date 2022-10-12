package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Category entity class
 *
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "book_id")
    private Integer bookId;
    @Column(name = "borrower_id", nullable = false)
    private Integer borrowerId;
    @Column(name = "date_lent", nullable = false)
    private LocalDate dateLent;
    @Column(name = "date_returned")
    private LocalDate dateReturned;
    @Size(max = 50)
    private String comment;
}
