package com.tacs.rest.entity;

import java.util.Date;

public class DataReport {
	
	private int id;
	private Date date;
	private long deaths;
	private long infected;
	private long cured;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getDeaths() {
		return deaths;
	}
	public void setDeaths(long deaths) {
		this.deaths = deaths;
	}
	public long getInfected() {
		return infected;
	}
	public void setInfected(long infected) {
		this.infected = infected;
	}
	public long getCured() {
		return cured;
	}
	public void setCured(long cured) {
		this.cured = cured;
	}
	
}
