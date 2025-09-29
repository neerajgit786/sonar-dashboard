package com.dashboard.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "master")
@Data
@Getter
@Setter
public class Master {
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getReport_url() {
		return report_url;
	}
	public void setReport_url(String report_url) {
		this.report_url = report_url;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getGateStatus() {
		return gateStatus;
	}
	public void setGateStatus(String gateStatus) {
		this.gateStatus = gateStatus;
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
	@Id
    @GeneratedValue
    private Long id;

    private String name;
    @Column(unique = true)
    private String key;
    private String report_url;
    private LocalDateTime date;
    @Column(name = "gate_status")
    private String gateStatus;
    @Column(name = "grade")
    private String grade;
    @Column(name = "RAG_status")
    private String ragStatus;
    @Column(name = "display")
    private Boolean display = false;
    @Column(name = "vendor")
    private String vendor;
}
