package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class LendingTimelineDTO {
    private Map<String, Integer> lent;
    private Map<String, Integer> returned;
    private Map<String, Integer> totalLent;
}
