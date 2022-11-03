package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class StatisticsDTO {
    private Integer availableBooks;
    private Integer lentBooks;
    private LendingTimelineDTO lendingTimeline;
    private List<CategoryBookCountDTO> categories;
}
