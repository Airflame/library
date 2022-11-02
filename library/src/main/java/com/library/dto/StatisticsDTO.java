package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class StatisticsDTO {
    private Integer availableBooks;
    private Integer lentBooks;
    private Map<String, Integer> lendingsTimeline;
    private List<CategoryBookCountDTO> categories;
}
