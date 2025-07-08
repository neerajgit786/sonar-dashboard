package com.dashboard.app;

import com.dashboard.app.model.GameReport;
import com.dashboard.app.service.impl.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private CsvService csvService;

    @GetMapping("/dashboard/reports")
    public String getReportsPage() {
        return "reports"; // Will render templates/reports.html
    }

    @ResponseBody
    @GetMapping("/dashboard/reports/data")
    public List<GameReport> getReportData() {
        return csvService.loadReports("sonar-metrics.csv");

    }
}
