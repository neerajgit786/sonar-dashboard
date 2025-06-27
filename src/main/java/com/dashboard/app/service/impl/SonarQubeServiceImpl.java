package com.dashboard.app.service.impl;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.dashboard.app.model.ProjectMeticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dashboard.app.config.SonarQubeConfig;
import com.dashboard.app.model.Project;
import com.dashboard.app.service.SonarQubeService;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SonarQubeServiceImpl implements SonarQubeService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SonarQubeConfig sonarqubeConfig;

    @Override
    public List<String> getListOfProjects() {

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        String auth = sonarqubeConfig.getUserToken() + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = sonarqubeConfig.getSonarServerUrl() + "/" + sonarqubeConfig.getProject();
        // Make a GET request with headers
        ResponseEntity<Project> response = restTemplate.exchange(url, HttpMethod.GET, entity, Project.class);
        Project project = response.getBody();
        System.out.println("Response: " + project);
        List<String> projectKeys = project.getComponents().stream().map(e -> e.getKey()).collect(Collectors.toList());

        getProjectMetrics(projectKeys);
        return projectKeys;

    }

    //	@Override
    public void getProjectMetrics(List<String> projectKeys) {

        String url = UriComponentsBuilder.fromHttpUrl(sonarqubeConfig.getSonarServerUrl() + "/" + sonarqubeConfig.getMeasuresUrl())
                .queryParam("metricKeys", "coverage,code_smells,bugs")
                .queryParam("component", projectKeys.get(0))
				.queryParam("additionalFields", "periods")
                .toUriString();

        // Create HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarqubeConfig.getUserToken(), ""); // token as username, empty password
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

      //  return response.getBody();
    }


@Override
public void saveAll(List<Project> projectList) {
    // TODO Auto-generated method stub

}

@Override
public void save(Project project) {
    // TODO Auto-generated method stub

}

@Override
public String getProject(String productKey) {
    // TODO Auto-generated method stub
    return null;
}

}
