package com.dashboard.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "analysis_history")
@Data
public class History {
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGateStatus() {
		return gateStatus;
	}

	public void setGateStatus(String gateStatus) {
		this.gateStatus = gateStatus;
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

	public Double getCoverage() {
		return coverage;
	}

	public void setCoverage(Double coverage) {
		this.coverage = coverage;
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

	public Double getMaintainability() {
		return maintainability;
	}

	public void setMaintainability(Double maintainability) {
		this.maintainability = maintainability;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;

    private String type;
    @Column(name = "gate_status")
    private String gateStatus;
    private Integer bugs;
    private Integer codeSmells;
    private Double coverage;
    private Integer vulnerabilities;
    @Column(name = "security_hotspots")
    private Integer securityHotspots;
    @Column(name = "sqale_debt_ratio")
    private Double maintainability;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
