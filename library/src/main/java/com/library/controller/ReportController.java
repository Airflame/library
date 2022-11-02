package com.library.controller;

import com.library.service.ReportService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Resource> generateReport() {
        return reportService.generateReport();
    }
}
