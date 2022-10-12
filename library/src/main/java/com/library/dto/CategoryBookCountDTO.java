package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class containing information about the name of a given category and the number of books assigned to it.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class CategoryBookCountDTO {
    protected Integer id;
    private String name;
    private Boolean isStarting;
    private Integer bookCount;
}
