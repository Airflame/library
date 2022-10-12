package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Class containing the history of all past and current lendings of a given borrower, along with information the id,
 * author, title and category names of all currently borrowed books along with the total number of them.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class LendingHistoryDTO {
    private String firstName;
    private String lastName;
    private List<LendingDTO> pastLendings;
    private List<LendingDTO> currentLendings;
    private List<BookDTO> lentBooks;
    private Integer bookCount;
}
