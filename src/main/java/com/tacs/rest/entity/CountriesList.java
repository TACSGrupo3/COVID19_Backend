package com.tacs.rest.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "public.COUNTRIESLIST")
public class CountriesList {

    @Id
    @Column(name = "id_CountriesList")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_CountriesList;
    
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
    
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    User user;
       
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "CountriesList_Country", 
        joinColumns = { @JoinColumn(name = "id_CountriesList") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_Country") }
    )
    private List<Country> countries;

    @JsonIgnore
    public User getUser() {
    	return this.user;
    }
    public void setUser(User user) {
    	this.user = user;
    }
    public int getId() {
        return id_CountriesList;
    }

    public void setId(int id) {
        this.id_CountriesList = id;
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

    public void addCountry(Country newCountry) {
        countries.add(newCountry);
    }

    public void removeCountry(Country exCountry) {
        countries.remove(exCountry);
    }
    
    public boolean hasCountry (int id_Country) {
    	return this.countries.stream().anyMatch(c->c.getId()==id_Country);
    }
}
