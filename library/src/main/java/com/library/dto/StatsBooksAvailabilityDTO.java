package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatsBooksAvailabilityDTO {
    private Integer available;
    private Integer lent;
}
