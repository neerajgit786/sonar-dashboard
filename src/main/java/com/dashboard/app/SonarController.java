package com.dashboard.app;

import java.io.IOException;
import java.util.List;

import com.dashboard.app.model.ProjectMetrics;
import com.dashboard.app.service.impl.SonarDbService;
import com.dashboard.app.service.impl.SonarService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.app.service.SonarQubeService;

@RestController
public class SonarController {
	@Autowired
	private SonarQubeService sonarQubeService;
	@Autowired
	private SonarService sonarService;
	@Autowired
	private SonarDbService sonarDbService;

	@RequestMapping("/")
	String hello() {
		return "Welcome to sonar dashboard custom export Application!";
	}

	@RequestMapping("/getProjects")
	public List<String> getSonarProjects() {
		List<String> project = sonarService.fetchAndSaveProjects();
		return project;
	}

	@GetMapping("/sonar/save")
	public ResponseEntity<String> save() throws IOException {
		sonarService.fetchAndSaveMetrics();
		return ResponseEntity.ok("Data saved");
	}

	@GetMapping("/sonar/export")
	public ResponseEntity<String> export() throws IOException {
		sonarService.exportToCSV("sonar-metrics.csv");
		return ResponseEntity.ok("Exported to CSV.");
	}

//	@GetMapping("/api/metrics-db")
//		public List<ProjectMetrics> getMetrics() {
//			return sonarDbService.getAllMetricsFromDb();
//		}

	@GetMapping("/api/history")
	public JsonNode getMetrics(@RequestParam String project_key) {
		return sonarService.fetchHistoricalMetrics(project_key);
	}

}