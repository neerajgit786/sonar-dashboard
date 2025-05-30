package com.dashboard.app.service;

import java.util.List;

import com.dashboard.app.model.Project;

public interface SonarQubeService {

	public String getProject(String productKey);

	public void saveAll(List<Project> projectList);

	public void save(Project project);

	public List<String> findAll();

}
