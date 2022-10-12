package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class LendingDTO {
    protected Integer id;
    private Integer bookId;
    private String firstName;
    private String lastName;
    private LocalDate dateLent;
    private LocalDate dateReturned;
    private String comment;
}
