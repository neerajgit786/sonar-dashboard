package com.dashboard.app.model;

import lombok.Data;

import java.util.List;

@Data
public class DisplayUpdateRequest {
    List<String> addedProjects;
    List<String> removedProjects;
    List<VendorNode> vendorNodesList;
}
