package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Class containing information about the id, author, title and the category name of a given book along with information
 * about whether the book is currently being lent, the first and the last name of the borrower and the date when
 * the book has been lent.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class EveryBookDTO {
    protected Integer id;
    private String author;
    private String title;
    private String category;
    private Boolean isLent;
    private String firstName;
    private String lastName;
    private LocalDate dateLent;
}
