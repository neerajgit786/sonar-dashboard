package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
@Getter
@Setter
public class Metrics {
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
