package com.dashboard.app.model;

import lombok.Data;

import java.util.List;

@Data
public class DisplayUpdateRequest {
    public List<String> getAddedProjects() {
		return addedProjects;
	}
	public void setAddedProjects(List<String> addedProjects) {
		this.addedProjects = addedProjects;
	}
	public List<String> getRemovedProjects() {
		return removedProjects;
	}
	public void setRemovedProjects(List<String> removedProjects) {
		this.removedProjects = removedProjects;
	}
	public List<VendorNode> getVendorNodesList() {
		return vendorNodesList;
	}
	public void setVendorNodesList(List<VendorNode> vendorNodesList) {
		this.vendorNodesList = vendorNodesList;
	}
	List<String> addedProjects;
    List<String> removedProjects;
    List<VendorNode> vendorNodesList;
}
