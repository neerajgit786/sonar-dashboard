package com.dashboard.app.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@Data
@ToString
public class SonarQubeConfig {

	@Value("${sonarqube.host.url}")
	private String sonarServerUrl;

	@Value("${sonarqube.user.token}")
	private String userToken;

	@Value("${sonarqube.project.uri}")
	private String project;

	@Value("${sonarqube.coverage.uri}")
	private String coverage;

	@Value("${sonarqube.bugs.uri}")
	private String bugs;

	@Value("${sonarqube.qualitygate.uri}")
	private String qualitygate;

	@Value("${sonarqube.metrics.measures.uri}")
	private String measuresUrl;


}
