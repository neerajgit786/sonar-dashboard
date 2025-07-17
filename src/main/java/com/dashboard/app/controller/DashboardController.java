package com.dashboard.app.controller;

import com.dashboard.app.model.DisplayUpdateRequest;
import com.dashboard.app.model.GameReport;
import com.dashboard.app.model.Project;
import com.dashboard.app.service.impl.CsvService;
import com.dashboard.app.service.impl.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/dashboard/display/save")
    @ResponseBody
    public ResponseEntity<Void> configurationSave(@RequestBody DisplayUpdateRequest request)
    {
        return sonarService.configurationSave(request.getAddedProjects() , request.getRemovedProjects());


    }

    @GetMapping("/dashboard/display-list")
    @ResponseBody
    public ResponseEntity<List<String>> getDisplayList()
    {
        List<String> response =  sonarService.getDisplayList();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
