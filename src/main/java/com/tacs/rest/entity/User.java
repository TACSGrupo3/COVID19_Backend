package com.tacs.rest.entity;

import java.util.List;

public class User {

	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String country;
	private String city;
	private String location;
	private String address;
	private int age;
	private String password;
	private String documentNumber;
	private int active;
	
	private List<ListaPaises> listados;
	
	public User() {}
	
	public User(int id, String username, String firstName, String lastName, String password) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public int isActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}

	public List<ListaPaises> getListados() {
		return listados;
	}

	public void setListados(List<ListaPaises> listados) {
		this.listados = listados;
	}

	public int getActive() {
		return active;
	}
	
	

}
