package com.dashboard.app.service.impl;

import com.dashboard.app.config.SonarQubeConfig;
import com.dashboard.app.entity.History;
import com.dashboard.app.entity.Master;
import com.dashboard.app.entity.Metrics;
import com.dashboard.app.model.Project;
import com.dashboard.app.model.Result;
import com.dashboard.app.repo.HistoryRepository;
import com.dashboard.app.repo.MasterRepository;
import com.dashboard.app.repo.MetricsRepository;
import com.dashboard.app.util.GradeCalculator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        List<String> projectKeyList = new ArrayList<>();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                Master master = null;
                if (!masterRepo.existsByKey(key)) {
                     master = new Master();
                    master.setKey(key);
                    master.setName(name);

                    master.setDate(ObjectUtils.isEmpty(project.get("lastAnalysisDate"))?LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) :
                            LocalDateTime.parse(project.get("lastAnalysisDate").asText(), formatter));
                    master.setGateStatus("NOT_CHECKED"); // Default value; will be updated by fetchAndSaveMetrics()
                    master.setReport_url(sonarQubeConfig.getSonarServerUrl() + "/dashboard?id=" + key);
                }
                else
                {
                    master = masterRepo.findByProjectKey(key);
                   master.setDate(ObjectUtils.isEmpty(project.get("lastAnalysisDate"))?LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) :
                    LocalDateTime.parse(project.get("lastAnalysisDate").asText(), formatter));
                }
                masterRepo.save(master);
                projectKeyList.add(key);
            }
        }
        return projectKeyList;
    }


    public void fetchAndSaveMetrics() {

        List<Master> masterList = masterRepo.findAll();
        List<Metrics> metricsList = new ArrayList<>();

        for (Master master : masterList) {
            String gateUrl = sonarQubeConfig.getSonarServerUrl() + "/api/qualitygates/project_status?projectKey=" + master.getKey();
            HttpHeaders headers = new HttpHeaders();

            headers.setBasicAuth(sonarQubeConfig.getUserToken(), "");
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> gateResponse = restTemplate.exchange(gateUrl, HttpMethod.GET, entity, JsonNode.class);
            JsonNode gate = gateResponse.getBody().get("projectStatus");

            String gateStatus = gate
                    .get("status").asText().equals("OK") ? "PASSED" : "FAILED";

//            master.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            master.setGateStatus(gateStatus);
            master.setReport_url(sonarQubeConfig.getSonarServerUrl() + "/dashboard?id=" + master.getKey());

            String metricsUrl = sonarQubeConfig.getSonarServerUrl() + "/api/measures/component?component=" + master.getKey() +
                    "&metricKeys=coverage,bugs,code_smells,vulnerabilities,security_hotspots,sqale_debt_ratio&additionalFields=period";

            ResponseEntity<JsonNode> metricsResponse = restTemplate.exchange(metricsUrl, HttpMethod.GET, entity, JsonNode.class);
            JsonNode measures = metricsResponse.getBody().get("component").get("measures");

            Metrics metrics = null;
            Optional<Metrics> metricsOptional = metricsRepo.findMetricsByMasterId(master.getId());
            metrics = ObjectUtils.isEmpty(metricsOptional) ? new Metrics() : metricsOptional.get();

            metrics.setMaster(master);
            metrics.setType("overall");
            metrics.setUpdatedDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            for (JsonNode measure : measures) {
                String metric = measure.get("metric").asText();
                String value = (measure.has("value") && !measure.get("value").isNull()) ?
                        measure.get("value").asText() : "0";


                try {
                    switch (metric) {
                        case "bugs" -> metrics.setBugs(Integer.parseInt(value));
                        case "code_smells" -> metrics.setCodeSmells(Integer.parseInt(value));
                        case "coverage" -> metrics.setCoverage(Double.parseDouble(value));
                        case "vulnerabilities" -> metrics.setVulnerabilities(Integer.parseInt(value));
                        case "security_hotspots" -> metrics.setSecurityHotspots(Integer.parseInt(value));
                        case "sqale_debt_ratio" -> metrics.setMaintainability(Double.parseDouble(value));
                    }
                } catch (NumberFormatException e) {
                    // Log the problem and use default values
                    switch (metric) {
                        case "bugs" -> metrics.setBugs(0);
                        case "code_smells" -> metrics.setCodeSmells(0);
                        case "coverage" -> metrics.setCoverage(0.0);
                        case "vulnerabilities" -> metrics.setVulnerabilities(0);
                        case "security_hotspots" -> metrics.setSecurityHotspots(0);
                        case "sqale_debt_ratio" -> metrics.setMaintainability(0.0);
                    }
                }
            }
            metricsList.add(metrics);
            //calculate grade for each project based on metrics
            //String grade = calculateGrade(metrics.getVulnerabilities(), metrics.getMaintainability(), metrics.getBugs());
            // Result result = GradeCalculator.calculateGradeAndRag(metrics.getCoverage(), metrics.getBugs(), metrics.getSecurityHotspots(), metrics.getVulnerabilities(), metrics.getMaintainability());
            Result result = GradeCalculator.calculateGradeAndRag(
                    metrics.getCoverage() != null ? metrics.getCoverage() : 0.0,
                    metrics.getBugs() != null ? metrics.getBugs() : 0,
                    metrics.getSecurityHotspots() != null ? metrics.getSecurityHotspots() : 0,
                    metrics.getVulnerabilities() != null ? metrics.getVulnerabilities() : 0,
                    metrics.getMaintainability() != null ? metrics.getMaintainability() : 0.0
            );
            master.setGrade(result.getGrade());
            master.setRagStatus(result.getRag());
        }

        masterRepo.saveAll(masterList);
        metricsRepo.saveAll(metricsList);
    }


    public ResponseEntity<Resource> exportToCSV() {
        List<Metrics> data = metricsRepo.findAll();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(out);
            writer.println("Game Name,Sonar Report URL,Date,Quality Gate,Grade,RAG Status,Code Coverage %,Bugs,Code Smell,Security,Vulnerabilities,Tech Debt Ratio,Game Key");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Metrics m : data) {
                Master master = m.getMaster();
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%d,%d,%d,%d,%s,%s%n",
                        escapeCsv(master.getName()),
                        escapeCsv(master.getReport_url()),
                        master.getDate() != null ? master.getDate().format(formatter) : "",
                        master.getGateStatus(),
                        master.getGrade(),
                        master.getRagStatus(),
                        String.valueOf(m.getCoverage()),
                        m.getBugs(),
                        m.getCodeSmells(),
                        m.getSecurityHotspots(),
                        m.getVulnerabilities(),
                        String.valueOf(m.getMaintainability()),
                        master.getKey()
                );
            }

            writer.flush();
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sonar-export.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    public ResponseEntity<String> fetchHistoricalMetrics() {
        try {
            List<Master> masterList = masterRepo.findAll();
            String metrics = "coverage,bugs,code_smells,vulnerabilities,security_hotspots,sqale_debt_ratio";

            for (Master master : masterList) {
                History singleHistoryRecord = historyRepository.findTopByMasterIdOrderByUpdatedDateDesc(master.getId());
                String fromDate = singleHistoryRecord != null ? singleHistoryRecord.getUpdatedDate().toLocalDate().toString() : "2025-01-01";
                String url = sonarQubeConfig.getSonarServerUrl() + "/api/measures/search_history" +
                        "?component=" + master.getKey() +
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

                        // Master master = masterRepo.findByProjectKey(master.getKey());
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
                            case "sqale_debt_ratio" -> history.setMaintainability(Double.parseDouble(valueStr));
                        }
                    }
                }
                historyRepository.saveAll(historyMap.values());
            }
        } catch (Exception e) {
            return new ResponseEntity<>("fetch failed : " + e.getMessage().toString(), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>("successful fetch :", HttpStatus.OK);


    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarQubeConfig.getUserToken(), "");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\""); // escape double quotes
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\""; // wrap in quotes if needed
        }
        return escaped;
    }

    public ResponseEntity<Void> configurationSave(List<String> addedProjects,List<String> removedProjects)
    {
        try {
            String[] projectKeysToBeAdded = addedProjects.toArray(new String[0]);
            String[] projectKeysToBeRemoved = removedProjects.toArray(new String[0]);
            List<Master> masterList = new ArrayList<Master>();
            List<Master> projectList = masterRepo.findAll();
            Map<String, Master> keyProjectMap = projectList.stream().collect(Collectors.toMap(e -> e.getKey(), e -> e));
            for (String project : projectKeysToBeAdded) {
                keyProjectMap.get(project).setDisplay(Boolean.TRUE);
                masterList.add(keyProjectMap.get(project));
            }
            for (String project : projectKeysToBeRemoved) {
                keyProjectMap.get(project).setDisplay(Boolean.FALSE);
                masterList.add(keyProjectMap.get(project));
            }
            masterRepo.saveAll(masterList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<String> getDisplayList()
    {
        List<Master> projectList = masterRepo.findByDisplay();
        List<String> projectKeys = projectList.stream().map(e->e.getKey()).collect(Collectors.toList());
        return projectKeys;
    }

}

