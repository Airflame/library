package com.library.service;

import com.library.dto.StatisticsDTO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final BookService bookService;

    private final StatisticsService statisticsService;

    public ResponseEntity<Resource> generateReport() {
        try {
            Map<String, Object> parameters = new HashMap<>();
            StatisticsDTO statistics = statisticsService.getStatistics();
            parameters.put("availableBooks", statistics.getAvailableBooks());
            parameters.put("lentBooks", statistics.getLentBooks());
            parameters.put("generated", new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()));

            InputStream booksReport
                    = getClass().getResourceAsStream("/reports/books-report.jrxml");
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(booksReport);
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bookService.getAvailableAndLentBooks());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "books-report-" + new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()) + ".pdf");

            //Resource resource = new UrlResource(Paths.get("Books_report.pdf").toUri());

            return ResponseEntity.ok()
                    //.contentType(MediaType.parseMediaType("application/pdf"))
                    //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
