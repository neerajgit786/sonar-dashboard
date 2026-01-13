package com.dashboard.app.model;

import lombok.Data;

@Data
public class GameReport {
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
