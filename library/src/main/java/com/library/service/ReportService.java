package com.library.service;

import com.library.dto.LendingMonthDTO;
import com.library.dto.StatisticsDTO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportService {

    private final BookService bookService;

    private final StatisticsService statisticsService;

    private final LendingService lendingService;

    public ResponseEntity<Resource> generateReport() {
        try {
            Map<String, Object> booksParameters = new HashMap<>();
            StatisticsDTO statistics = statisticsService.getStatistics();
            booksParameters.put("availableBooks", statistics.getAvailableBooks());
            booksParameters.put("lentBooks", statistics.getLentBooks());
            booksParameters.put("mostPopularBookName", bookService.getMostPopularBookName());
            booksParameters.put("generated", new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()));

//            List<LendingMonthDTO> lendingList = lendingService.countByMonthsList();
//            Map<String, Object> lendingTimelineParameters = new HashMap<>();
//            lendingTimelineParameters.put("mostLent", mostLent(lendingList));
//            lendingTimelineParameters.put("mostReturned", mostReturned(lendingList));
//            lendingTimelineParameters.put("mostTotalLent", mostTotalLent(lendingList));
//            lendingTimelineParameters.put("generated", new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()));

            InputStream booksReport
                    = getClass().getResourceAsStream("/reports/books-report.jrxml");
            JasperReport booksJasperReport
                    = JasperCompileManager.compileReport(booksReport);
            JRBeanCollectionDataSource booksSource = new JRBeanCollectionDataSource(bookService.getAvailableAndLentBooks());
            JasperPrint booksJasperPrint = JasperFillManager.fillReport(booksJasperReport, booksParameters, booksSource);
            JasperExportManager.exportReportToPdfFile(booksJasperPrint, "books-report-" + new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()) + ".pdf");

//            InputStream lendingTimelineReport
//                    = getClass().getResourceAsStream("/reports/lending-timeline-report.jrxml");
//            JasperReport lendingTimelineJasperReport
//                    = JasperCompileManager.compileReport(lendingTimelineReport);
//            JRBeanCollectionDataSource lendingTimelineSource = new JRBeanCollectionDataSource(lendingList);
//            JasperPrint lendingTimelineJasperPrint = JasperFillManager.fillReport(lendingTimelineJasperReport, lendingTimelineParameters, lendingTimelineSource);
//            JasperExportManager.exportReportToPdfFile(lendingTimelineJasperPrint, "lendings-timeline-report-" + new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new java.util.Date()) + ".pdf");

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
//
//    private String mostLent(List<LendingMonthDTO> lendingList) {
//        String mostLent = "";
//        int max = 0;
//        for (LendingMonthDTO lendingMonthDTO : lendingList) {
//            if (Integer.parseInt(lendingMonthDTO.getLent()) > max) {
//                mostLent = lendingMonthDTO.getMonth();
//                max = lendingMonthDTO.getLent();
//            }
//        }
//        return mostLent;
//    }
//
//    private String mostReturned(List<LendingMonthDTO> lendingList) {
//        String mostReturned = "";
//        int max = 0;
//        for (LendingMonthDTO lendingMonthDTO : lendingList) {
//            if (lendingMonthDTO.getReturned() > max) {
//                mostReturned = lendingMonthDTO.getMonth();
//                max = lendingMonthDTO.getReturned();
//            }
//        }
//        return mostReturned;
//    }
//
//    private String mostTotalLent(List<LendingMonthDTO> lendingList) {
//        String mostTotalLent = "";
//        int max = 0;
//        for (LendingMonthDTO lendingMonthDTO : lendingList) {
//            if (lendingMonthDTO.getTotalLent() > max) {
//                mostTotalLent = lendingMonthDTO.getMonth();
//                max = lendingMonthDTO.getTotalLent();
//            }
//        }
//        return mostTotalLent;
//    }
}
