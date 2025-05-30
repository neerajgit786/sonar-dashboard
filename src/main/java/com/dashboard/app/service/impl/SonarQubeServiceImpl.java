package com.dashboard.app.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dashboard.app.config.SonarQubeConfig;
import com.dashboard.app.model.Project;
import com.dashboard.app.service.SonarQubeService;

@Service
public class SonarQubeServiceImpl implements SonarQubeService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired 
	SonarQubeConfig sonarqubeConfig;
	
	@Override
	public  List<String> findAll(){
		
		// Set headers
        HttpHeaders headers = new HttpHeaders();
        
        headers.set("Authorization", "Bearer "+sonarqubeConfig.getUserToken());

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = sonarqubeConfig.getSonarServerUrl()+"/"+sonarqubeConfig.getProject();
        // Make a GET request with headers
        ResponseEntity<Project> response = restTemplate.exchange(url, HttpMethod.GET, entity, Project.class);
        Project project = response.getBody();
        System.out.println("Response: " + project);
        List<String> projectName =project.getComponents().stream().map(e-> e.getName()).collect(Collectors.toList());
        
        
        return projectName;
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
