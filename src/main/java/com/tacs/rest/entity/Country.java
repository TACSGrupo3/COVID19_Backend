package com.tacs.rest.entity;

import java.util.List;

public class Country {

	private int id;
	private String name;
	private Region region;
	private List<DataReport> dataReport;
	private int offset;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public List<DataReport> getDataReport() {
		return dataReport;
	}
	public void setDataReport(List<DataReport> dataReport) {
		this.dataReport = dataReport;
	}
	
}
