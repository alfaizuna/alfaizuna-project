package com.java.alfaizunaproject.controller;

import com.java.alfaizunaproject.dto.ItemStatisticsReportDTO;
import com.java.alfaizunaproject.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ItemStatisticsReportDTO>> getItemStatisticReport() {
        return ResponseEntity.ok(reportService.getItemsReport());
    }
}
