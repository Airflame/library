package com.library.controller;

import com.library.dto.StatisticsDTO;
import com.library.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@Api("Books Management API")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @ApiOperation(value = "Lists book availability data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data found")
    })
    @GetMapping(path = "/stats/books-availability")
    public StatisticsDTO getStatistics() {
        return statisticsService.getStatistics();
    }
}
