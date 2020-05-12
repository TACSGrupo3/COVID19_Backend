package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String token;
    private Date lastAccess;
    private List<CountriesList> countriesList;
    private long telegram_chat_id;
    private String telephone_number;

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


    public void addList(CountriesList newList) {
        countriesList.add(newList);
    }

    public void removeList(CountriesList exList) {
        countriesList.remove(exList);
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
