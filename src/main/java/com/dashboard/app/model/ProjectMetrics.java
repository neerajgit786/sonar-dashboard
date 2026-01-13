package com.dashboard.app.model;

import lombok.Data;

@Data
public class ProjectMetrics {
    private String projectKey;
    private String projectName;
    private String gateStatus;
    private Double coverage;
    private Integer bugs;
    private Integer codeSmells;
    private Integer vulnerabilities;
    private Integer securityHotspots;
}
