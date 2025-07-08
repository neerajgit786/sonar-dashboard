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
}
