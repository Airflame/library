package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LendingMonthDTO {
    private String month;
    private String lent;
    private String returned;
    private String totalLent;
}
