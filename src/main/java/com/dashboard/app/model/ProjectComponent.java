
package com.dashboard.app.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ProjectComponent {

	@Override
	public String toString() {
		return "ProjectComponent [key=" + key + ", name=" + name + ", lastAnalysisDate=" + lastAnalysisDate
				+ ", qualifier=" + qualifier + "]";
	}

	@JsonSerialize(using = ToStringSerializer.class)
	private String key;

	@JsonSerialize(using = ToStringSerializer.class)
	private String name;

	@JsonSerialize(using = ToStringSerializer.class)
	private String qualifier;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private LocalDateTime lastAnalysisDate;

	public LocalDateTime getLastAnalysisDate() {
		return lastAnalysisDate;
	}

	public void setLastAnalysisDate(LocalDateTime lastAnalysisDate) {
		this.lastAnalysisDate = lastAnalysisDate;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getQualifier() {
		return qualifier;
	}

	// Setter Methods

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
}
