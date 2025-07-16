package com.dashboard.app.controller;

import com.dashboard.app.model.GameReport;
import com.dashboard.app.service.impl.CsvService;
import com.dashboard.app.service.impl.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private CsvService csvService;

    @Autowired
    private SonarService sonarService;

    @GetMapping("/dashboard/reports")
    public String getReportsPage() {
        return "reports";
    }

    @ResponseBody
    @GetMapping("/dashboard/reports/data")
    public List<GameReport> getReportData() {
        return csvService.loadReports();

    }

    @PostMapping("/dashboard/refresh")
    @ResponseBody
    public ResponseEntity<String> refreshAction() {
        sonarService.fetchAndSaveProjects();
        sonarService.fetchAndSaveMetrics();
        return ResponseEntity.ok("Refreshed");
    }
}
