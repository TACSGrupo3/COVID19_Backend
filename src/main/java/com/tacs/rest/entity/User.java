package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "public.USER")
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;
   
    @Column(name = "userName", nullable = false)
    private String username;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    private String token;
    @Column(name = "lastAccess", nullable = true)
    private Date lastAccess;
    @Column(name = "userRole", nullable = true)
    private String userRole;
     
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(FetchMode.SELECT)
    private List<CountriesList> countriesList;
    
    @Column(name = "telegram_chat_id", nullable = true)
    private long telegramChatId;
    @Column(name = "telephone", nullable = true)
    private String telephone_number;

    public User() {
        this.countriesList = new ArrayList<CountriesList>();
    }

    public User(int id, String username, String firstName, String lastName, String userRole) {
        super();
        this.id_user = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = null;
        this.userRole = userRole;
        this.countriesList = new ArrayList<CountriesList>();
    }

    public User(int id, String username, String firstName, String lastName, String password, String userRole) {
        super();
        this.id_user = id;
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
        this.id_user = id;
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
        this.id_user = id;
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
    
    public void modifyList(CountriesList list) {
    	for(int i = 0; i <countriesList.size(); i++) {
    		
    		if(countriesList.get(i).getId()==list.getId()) {
    			countriesList.get(i).setCountries(list.getCountries());
    			countriesList.get(i).setName(list.getName());
    		}
    	}    	
    }

    public int getId() {
        return id_user;
    }

    public void setId(int id) {
        this.id_user = id;
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
        return telegramChatId;
    }

    public void setTelegram_chat_id(long telegram_chat_id) {
        this.telegramChatId = telegram_chat_id;
    }


    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }
    
    public boolean hasCountry (int id_Country) {
    	return this.countriesList.stream()
    			.anyMatch(list -> list.hasCountry(id_Country));
    }
    
    
}
