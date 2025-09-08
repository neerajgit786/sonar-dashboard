package com.dashboard.app.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.app.service.impl.SonarService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SonarController {

    @Autowired
    private SonarService sonarService;


    @GetMapping("/")
    public void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
    }

    @RequestMapping("/sonar/projects")
    public List<String> getSonarProjects() {
        List<String> project = sonarService.fetchAndSaveProjects();
        return project;
    }

    @GetMapping("/sonar/metrics")
    public ResponseEntity<String> save() throws IOException {
        sonarService.fetchAndSaveMetrics();
        return ResponseEntity.ok("Data saved");
    }

    @GetMapping("/sonar/export")
    public ResponseEntity<Resource> export() throws IOException {
        return sonarService.exportToCSV();

    }


    @GetMapping("/sonar/history")
    public ResponseEntity<String> getMetrics() {
        return sonarService.fetchHistoricalMetrics();
    }

}