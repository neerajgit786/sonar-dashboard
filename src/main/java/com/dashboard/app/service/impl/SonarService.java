package com.dashboard.app.service.impl;

import com.dashboard.app.config.SonarQubeConfig;
import com.dashboard.app.entity.History;
import com.dashboard.app.entity.Master;
import com.dashboard.app.entity.Metrics;
import com.dashboard.app.model.Project;
import com.dashboard.app.repo.HistoryRepository;
import com.dashboard.app.repo.MasterRepository;
import com.dashboard.app.repo.MetricsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SonarService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MasterRepository masterRepo;
    @Autowired
    private MetricsRepository metricsRepo;
    @Autowired
    private SonarQubeConfig sonarQubeConfig;
    @Autowired
    private HistoryRepository historyRepository;

    public List<String> fetchAndSaveProjects() {
        List<String> projectKeyList= new ArrayList<>();
        String projectsUrl = sonarQubeConfig.getSonarServerUrl() + "/api/projects/search?ps=500"; // Fetch up to 500 projects
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarQubeConfig.getUserToken(), "");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(projectsUrl, HttpMethod.GET, entity, JsonNode.class);
        JsonNode body = response.getBody();
        JsonNode components = body.get("components");

        if (components != null && components.isArray()) {
            for (JsonNode project : components) {
                String key = project.get("key").asText();
                String name = project.get("name").asText();

                if (!masterRepo.existsByKey(key)) {
                    Master master = new Master();
                    master.setKey(key);
                    master.setName(name);
                    master.setDate(LocalDateTime.now());
                    master.setGateStatus("NOT_CHECKED"); // Default value; will be updated by fetchAndSaveMetrics()
                    master.setReport_url("http://localhost:9000/dashboard?id=" + key);

                    masterRepo.save(master);
                }
                projectKeyList.add(key);
            }
        }
        return projectKeyList;
    }



    public void fetchAndSaveMetrics() {

        List<Master> masterList= masterRepo.findAll();
        List<Metrics> metricsList = new ArrayList<>();

        for(Master master: masterList) {
            String gateUrl = sonarQubeConfig.getSonarServerUrl() + "/api/qualitygates/project_status?projectKey=" + master.getKey();
            HttpHeaders headers = new HttpHeaders();

            headers.setBasicAuth(sonarQubeConfig.getUserToken(), "");
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> gateResponse = restTemplate.exchange(gateUrl, HttpMethod.GET, entity, JsonNode.class);
            JsonNode gate = gateResponse.getBody().get("projectStatus");

            String gateStatus = gate
                    .get("status").equals("OK") ? "PASSED" : "FAILED";

            master.setDate(LocalDateTime.now());
            master.setGateStatus(gateStatus);
            master.setReport_url("http://localhost:9000/dashboard?id=" + master.getName());

            String metricsUrl = sonarQubeConfig.getSonarServerUrl() + "/api/measures/component?component=" + master.getKey() +
                    "&metricKeys=coverage,bugs,code_smells,vulnerabilities,security_hotspots&additionalFields=periods";

            ResponseEntity<JsonNode> metricsResponse = restTemplate.exchange(metricsUrl, HttpMethod.GET, entity, JsonNode.class);
            JsonNode measures = metricsResponse.getBody().get("component").get("measures");

            Metrics metrics = new Metrics();
            metrics.setMaster(master);
            metrics.setType("overall");
            metrics.setUpdatedDate(LocalDateTime.now());

            for (JsonNode measure : measures) {
                String metric = measure.get("metric").asText();
                String value = measure.get("value").asText();

                switch (metric) {
                    case "bugs" -> metrics.setBugs(Integer.parseInt(value));
                    case "code_smells" -> metrics.setCodeSmells(Integer.parseInt(value));
                    case "coverage" -> metrics.setCoverage(Double.parseDouble(value));
                    case "vulnerabilities" -> metrics.setVulnerabilities(Integer.parseInt(value));
                    case "security_hotspots" -> metrics.setSecurityHotspots(Integer.parseInt(value));
                }
            }
            metricsList.add(metrics);
        }
        masterRepo.saveAll(masterList);
        metricsRepo.saveAll(metricsList);
    }


    public void exportToCSV(String outputPath) throws IOException {
        List<Metrics> data = metricsRepo.findAll();

        try (PrintWriter writer = new PrintWriter(new File(outputPath))) {
            writer.println("Game Name,Sonar Report URL,Date,Quality Gate,Code Coverage %,Bugs,Code Smell,Security,Vulnerabilities");

            for (Metrics m : data) {
                Master master = m.getMaster();
                writer.printf("%s,%s,%s,%s,%s,%d,%d,%d,%d%n",
                        master.getName(),
                       master.getReport_url(),
                        master.getDate(),
                        master.getGateStatus(),
                        m.getCoverage(),
                        m.getBugs(),
                        m.getCodeSmells(),
                        m.getSecurityHotspots(),
                        m.getVulnerabilities()
                );
            }
        }
    }

    public JsonNode fetchHistoricalMetrics(String projectKey) {
        String fromDate ="2024-01-01";
        String metrics= "coverage,bugs,code_smells,vulnerabilities,security_hotspots";

        String url = sonarQubeConfig.getSonarServerUrl() + "/api/measures/search_history" +
                "?component=" + projectKey +
                "&metrics=" + metrics +
                "&from=" + fromDate;

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        Map<LocalDateTime, History> historyMap = new HashMap<>();

            JsonNode measures = response.getBody().get("measures");

            for (JsonNode metricNode : measures) {
                String metric = metricNode.get("metric").asText();
                for (JsonNode historyEntry : metricNode.get("history")) {
                    String trimmed = historyEntry.get("date").asText().substring(0, 19);  // "2025-05-15T10:51:32"
                    LocalDateTime ldt = LocalDateTime.parse(trimmed);
                    String valueStr = historyEntry.get("value").asText();

                    Master master = masterRepo.findByProjectKey(projectKey);
                    History history = historyMap.computeIfAbsent(ldt, d -> {
                        History h = new History();
                        h.setMaster(master);
                        h.setUpdatedDate(d);
                        h.setType("Overall");
                        h.setGateStatus(master.getGateStatus());
                        return h;
                    });

                    switch (metric) {
                        case "bugs" -> history.setBugs(Integer.parseInt(valueStr));
                        case "code_smells" -> history.setCodeSmells(Integer.parseInt(valueStr));
                        case "coverage" -> history.setCoverage(Double.parseDouble(valueStr));
                        case "vulnerabilities" -> history.setVulnerabilities(Integer.parseInt(valueStr));
                        case "security_hotspots" -> history.setSecurityHotspots(Integer.parseInt(valueStr));
                    }
                }
            }

            historyRepository.saveAll(historyMap.values());
            return measures;
        }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarQubeConfig.getUserToken(), "");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}

