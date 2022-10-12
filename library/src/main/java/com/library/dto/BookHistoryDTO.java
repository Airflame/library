package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Class containing information about whether a given book is currently being lent and a history of all past
 * and current lendings associated with it.
 *
 *
 */
@Data
@AllArgsConstructor
@Builder
public class BookHistoryDTO {
    protected Integer id;
    private String author;
    private String title;
    private Boolean isLent;
    private List<LendingDTO> lendings;
}
