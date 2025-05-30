package com.dashboard.app.model;

import java.util.ArrayList;

public class Component {
	@Override
	public String toString() {
		return "Component [key=" + key + ", name=" + name + ", description=" + description + ", qualifier=" + qualifier
				+ ", measures=" + measures + "]";
	}

	public ArrayList<Object> getMeasures() {
		return measures;
	}

	public void setMeasures(ArrayList<Object> measures) {
		this.measures = measures;
	}

	private String key;
	private String name;
	private String description;
	private String qualifier;
	ArrayList<Object> measures = new ArrayList<Object>();

	// Getter Methods

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
}