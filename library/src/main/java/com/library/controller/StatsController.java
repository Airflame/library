package com.library.controller;

import com.library.dto.StatsBooksAvailabilityDTO;
import com.library.service.StatsService;
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

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@Api("Books Management API")
public class StatsController {

    private final StatsService statsService;

    @ApiOperation(value = "Lists book availability data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data found")
    })
    @GetMapping(path = "/stats/books-availability")
    public StatsBooksAvailabilityDTO getAllBooks() {
        return statsService.getBooksAvailability();
    }
}
