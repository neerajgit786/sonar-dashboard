package com.dashboard.app.model;

import lombok.Data;

@Data
public class VendorNode{
    public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	private String key;
    private String vendorName;
}
