package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name = "public.USERS")
public class User {

	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String token;
	private Date lastAccess;
	
	private List<CountriesList> countriesList;
	
	public User() {
		this.countriesList = new ArrayList<CountriesList>(); 
	}
	
	public User(int id, String username, String firstName, String lastName, String password) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.countriesList = new ArrayList<CountriesList>(); 
	}
	
	public void addList(CountriesList newList){
		countriesList.add(newList);
	}
	
	public void removeList(CountriesList exList){
		countriesList.remove(exList);
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "username" , nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "firstName" , nullable = false)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "lastName" , nullable = false)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "password" , nullable = false)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(mappedBy="user")
	public List<CountriesList> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<CountriesList> countriesList) {
		this.countriesList = countriesList;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Column(name = "lastAccess" , nullable = false)
	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}	

}
