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

	public String getSonarServerUrl() {
		return sonarServerUrl;
	}

	public void setSonarServerUrl(String sonarServerUrl) {
		this.sonarServerUrl = sonarServerUrl;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getBugs() {
		return bugs;
	}

	public void setBugs(String bugs) {
		this.bugs = bugs;
	}

	public String getQualitygate() {
		return qualitygate;
	}

	public void setQualitygate(String qualitygate) {
		this.qualitygate = qualitygate;
	}

	public String getMeasuresUrl() {
		return measuresUrl;
	}

	public void setMeasuresUrl(String measuresUrl) {
		this.measuresUrl = measuresUrl;
	}

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
