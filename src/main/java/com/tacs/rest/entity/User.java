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
	private String userRole;
	private List<CountriesList> countriesList;
	private long telegram_chat_id;
	private String telephone_number;

	public User() {
		this.countriesList = new ArrayList<CountriesList>();
	}

	public User(int id, String username, String firstName, String lastName, String userRole) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = null;
		this.userRole = userRole;
		this.countriesList = new ArrayList<CountriesList>();
	}
	
	public User(int id, String username, String firstName, String lastName, String password, String userRole) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.countriesList = new ArrayList<CountriesList>();
		this.userRole = userRole;
	}

	public User(int id, String username, String firstName, String lastName, String password,
			List<CountriesList> countriesList, String userRole) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.countriesList = countriesList;
		this.userRole = userRole;
	}

	public User(int id, String username, String firstName, String lastName, List<CountriesList> countriesList,
			String userRole) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = null;
		this.countriesList = countriesList;
		this.userRole = userRole;
	}

	public void addList(CountriesList newList) {
		countriesList.add(newList);
	}

	public void removeList(CountriesList exList) {
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public long getTelegram_chat_id() {
		return telegram_chat_id;
	}

	public void setTelegram_chat_id(long telegram_chat_id) {
		this.telegram_chat_id = telegram_chat_id;
	}

	public String getTelephone_number() {
		return telephone_number;
	}

	public void setTelephone_number(String telephone_number) {
		this.telephone_number = telephone_number;
	}
}
