package com.tacs.rest.entity;

import java.util.Date;
import java.util.List;

public class CountriesList {

	private int id;
	private String name;
	private Date creationDate;
	private List<Country> countries;
	
	public void addCountry(Country newCountry){
		countries.add(newCountry);
	}
	
	public void removeCountrie(Country exCountrie){
		countries.remove(exCountrie);
	}
	
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
	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
