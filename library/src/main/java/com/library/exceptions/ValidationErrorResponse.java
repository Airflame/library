package com.library.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response returned upon detecting constraints validation errors somewhere in the application. Contains the message
 * about specific affected fields and their violations.
 *
 *
 */
@Data
@Builder
public class ValidationErrorResponse {
    @JsonProperty
    private LocalDateTime timestamp;

    @JsonProperty
    private Integer status;

    @JsonProperty
    private String error;

    @JsonProperty
    private String message;
}
