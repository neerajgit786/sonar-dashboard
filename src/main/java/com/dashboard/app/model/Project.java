package com.dashboard.app.model;

import java.util.ArrayList;

public class Project {

	Paging PagingObject;

	ArrayList<ProjectComponent> components = new ArrayList<ProjectComponent>();

	public Paging getPagingObject() {
		return PagingObject;
	}

	public void setPagingObject(Paging pagingObject) {
		PagingObject = pagingObject;
	}

	public ArrayList<ProjectComponent> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<ProjectComponent> components) {
		this.components = components;
	}

	public Paging getPaging() {
		return PagingObject;
	}

	public void setPaging(Paging pagingObject) {
		this.PagingObject = pagingObject;
	}
}

class Paging {
	private float pageIndex;
	private float pageSize;
	private float total;

	// Getter Methods

	public float getPageIndex() {
		return pageIndex;
	}

	public float getPageSize() {
		return pageSize;
	}

	public float getTotal() {
		return total;
	}

	// Setter Methods

	public void setPageIndex(float pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageSize(float pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotal(float total) {
		this.total = total;
	}
}