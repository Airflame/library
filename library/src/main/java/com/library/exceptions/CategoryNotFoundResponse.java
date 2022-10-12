package com.library.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.dto.CategoryDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class returned upon throwing 404 Not Found error during category deleting, containing information about the error
 * and a list of all categories in the database.
 *
 *
 */
@Data
@Builder
public class CategoryNotFoundResponse {
    @JsonProperty
    private LocalDateTime timestamp;

    @JsonProperty
    private Integer status;

    @JsonProperty
    private String error;

    @JsonProperty
    private String message;

    @JsonProperty
    private List<CategoryDTO> categories;
}
