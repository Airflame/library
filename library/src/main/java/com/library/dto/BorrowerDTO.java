package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class containing information about the first name and the last name of a borrower
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class BorrowerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
}
