package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class containing information about the id, author, title and the category name of a given book.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class BookDTO {
    protected Integer id;
    private String author;
    private String title;
    private String category;
}
