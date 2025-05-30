package com.dashboard.app.model;

import java.util.ArrayList;

public class ProjectQualityGate {
	ProjectStatus ProjectStatusObject;

	public ProjectStatus getProjectStatus() {
		return ProjectStatusObject;
	}

	public void setProjectStatus(ProjectStatus projectStatusObject) {
		this.ProjectStatusObject = projectStatusObject;
	}
}

class ProjectStatus {
	private String status;
	ArrayList<Object> conditions = new ArrayList<Object>();
	private boolean ignoredConditions;
	private String caycStatus;

	public String getStatus() {
		return status;
	}

	public boolean getIgnoredConditions() {
		return ignoredConditions;
	}

	public String getCaycStatus() {
		return caycStatus;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIgnoredConditions(boolean ignoredConditions) {
		this.ignoredConditions = ignoredConditions;
	}

	public void setCaycStatus(String caycStatus) {
		this.caycStatus = caycStatus;
	}
}