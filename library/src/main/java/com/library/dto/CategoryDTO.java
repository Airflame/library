package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class containing information about the given category
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class CategoryDTO {
    protected Integer id;
    private String name;
    private Boolean isStarting;
}
