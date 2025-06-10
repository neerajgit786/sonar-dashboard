package com.dashboard.app.service.impl;

import com.dashboard.app.config.SonarQubeConfig;
import com.dashboard.app.entity.Master;
import com.dashboard.app.entity.Metrics;
import com.dashboard.app.model.Project;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
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
                    master.setDate(LocalDate.now());
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

//            Master master = new Master();
//            master.setName(gameName);
            master.setDate(LocalDate.now());
//            master.setKey(projectKey);
            master.setGateStatus(gateStatus);
            master.setReport_url("http://localhost:9000/dashboard?id=" + master.getName());
//            master = masterRepo.save(master);

            String metricsUrl = sonarQubeConfig.getSonarServerUrl() + "/api/measures/component?component=" + master.getKey() +
                    "&metricKeys=coverage,bugs,code_smells,vulnerabilities,security_hotspots&additionalFields=periods";

            ResponseEntity<JsonNode> metricsResponse = restTemplate.exchange(metricsUrl, HttpMethod.GET, entity, JsonNode.class);
            JsonNode measures = metricsResponse.getBody().get("component").get("measures");

            Metrics metrics = new Metrics();
            metrics.setMaster(master);
            metrics.setType("overall");
            metrics.setUpdatedDate(LocalDate.now());

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

}

