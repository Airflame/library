package com.library.service;

import com.library.dto.StatisticsDTO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final DataSource dataSource;

    private final BookService bookService;

    private final StatisticsService statisticsService;

    public void generateReport() {
        try {
            Map<String, Object> parameters = new HashMap<>();
            StatisticsDTO statistics = statisticsService.getStatistics();
            parameters.put("availableBooks", statistics.getAvailableBooks());
            parameters.put("lentBooks", statistics.getLentBooks());

            InputStream booksReport
                    = getClass().getResourceAsStream("/reports/books-report.jrxml");
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(booksReport);
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bookService.getAvailableAndLentBooks());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "Books_report.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
