package com.dashboard.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.app.model.Project;
import com.dashboard.app.service.SonarQubeService;

@RestController
public class SonarController {
	@Autowired
	private SonarQubeService sonarService;

	@RequestMapping("/")
	String hello() {
		return "Welcome to sonar dashboard custom export Application!";
	}

	@RequestMapping("/getSonarProject")
	public List<String> getSonarProjects(@RequestParam String projectkey) {

		List<String> project = sonarService.findAll();
		return project;
	}

}