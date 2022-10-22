package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatisticsDTO {
    private Integer availableBooks;
    private Integer lentBooks;
}
