package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Class containing information about the id, author, title and the category name of a given currently lent book along
 * with the first and the last name of the borrower, the date when the book has been lent and an optional comment.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class LentBookDTO {
    protected Integer id;
    private String author;
    private String title;
    private String category;
    private String firstName;
    private String lastName;
    private LocalDate dateLent;
    private String comment;
}
