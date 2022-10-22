package com.library.controller;

import com.library.service.ReportService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@Api("Books Management API")
public class ReportController {

    private final ReportService reportService;

    @PostMapping(path = "/reports")
    public void generateReport() {
        reportService.generateReport();
    }
}
