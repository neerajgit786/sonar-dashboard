package com.dashboard.app.model;

import lombok.Data;

@Data
public class GameReport {
    public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSonarReportUrl() {
		return sonarReportUrl;
	}

	public void setSonarReportUrl(String sonarReportUrl) {
		this.sonarReportUrl = sonarReportUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getQualityGate() {
		return qualityGate;
	}

	public void setQualityGate(String qualityGate) {
		this.qualityGate = qualityGate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRagStatus() {
		return ragStatus;
	}

	public void setRagStatus(String ragStatus) {
		this.ragStatus = ragStatus;
	}

	public String getCodeCoverage() {
		return codeCoverage;
	}

	public void setCodeCoverage(String codeCoverage) {
		this.codeCoverage = codeCoverage;
	}

	public String getBugs() {
		return bugs;
	}

	public void setBugs(String bugs) {
		this.bugs = bugs;
	}

	public String getCodeSmell() {
		return codeSmell;
	}

	public void setCodeSmell(String codeSmell) {
		this.codeSmell = codeSmell;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getVulnerabilities() {
		return vulnerabilities;
	}

	public void setVulnerabilities(String vulnerabilities) {
		this.vulnerabilities = vulnerabilities;
	}

	public String getTechDebtRatio() {
		return techDebtRatio;
	}

	public void setTechDebtRatio(String techDebtRatio) {
		this.techDebtRatio = techDebtRatio;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	private String gameName;
    private String key;
    private String sonarReportUrl;
    private String date;
    private String qualityGate;
    private String grade;
    private String ragStatus;
    private String codeCoverage;
    private String bugs;
    private String codeSmell;
    private String security;
    private String vulnerabilities;
    private String techDebtRatio;
    private Boolean display;
    private String vendor;

    public GameReport(String gameName, String key, String sonarReportUrl, String date,
                      String qualityGate, String grade, String ragStatus,
                      String codeCoverage, String bugs, String codeSmell, String security,
                      String vulnerabilities, String techDebtRatio, Boolean display, String vendor) {
        this.gameName = gameName;
        this.key = key;
        this.sonarReportUrl = sonarReportUrl;
        this.date = date;
        this.qualityGate = qualityGate;
        this.grade = grade;
        this.ragStatus = ragStatus;
        this.codeCoverage = codeCoverage;
        this.bugs = bugs;
        this.codeSmell = codeSmell;
        this.security = security;
        this.vulnerabilities = vulnerabilities;
        this.techDebtRatio = techDebtRatio;
        this.display = display;
        this.vendor = vendor;
    }

}
