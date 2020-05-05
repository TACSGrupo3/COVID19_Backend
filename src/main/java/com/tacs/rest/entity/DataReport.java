package com.tacs.rest.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "public.DATA_REPORT")
public class DataReport implements Comparable<DataReport> {

    private int id;
    private Date date;
    private Integer deaths;
    private Integer confirmed;
    private Integer recovered;
    private Country country;

    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "date" , nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "deaths" , nullable = false)
    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    @Column(name = "recovered" , nullable = false)
    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    @Column(name = "confirmed" , nullable = false)
    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }


    @Override
    public int compareTo(DataReport d) {
        return this.getDate().compareTo(d.getDate());
    }

    @ManyToOne
    @JoinColumn(name="country_id", nullable=false)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
    
    
}
