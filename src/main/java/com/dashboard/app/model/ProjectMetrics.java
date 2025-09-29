package com.dashboard.app.model;

import lombok.Data;

@Data
public class ProjectMetrics {
    public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getGateStatus() {
		return gateStatus;
	}
	public void setGateStatus(String gateStatus) {
		this.gateStatus = gateStatus;
	}
	public Double getCoverage() {
		return coverage;
	}
	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}
	public Integer getBugs() {
		return bugs;
	}
	public void setBugs(Integer bugs) {
		this.bugs = bugs;
	}
	public Integer getCodeSmells() {
		return codeSmells;
	}
	public void setCodeSmells(Integer codeSmells) {
		this.codeSmells = codeSmells;
	}
	public Integer getVulnerabilities() {
		return vulnerabilities;
	}
	public void setVulnerabilities(Integer vulnerabilities) {
		this.vulnerabilities = vulnerabilities;
	}
	public Integer getSecurityHotspots() {
		return securityHotspots;
	}
	public void setSecurityHotspots(Integer securityHotspots) {
		this.securityHotspots = securityHotspots;
	}
	private String projectKey;
    private String projectName;
    private String gateStatus;
    private Double coverage;
    private Integer bugs;
    private Integer codeSmells;
    private Integer vulnerabilities;
    private Integer securityHotspots;
}
