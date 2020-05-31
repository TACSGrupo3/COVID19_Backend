package com.tacs.rest.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "public.DATAREPORT")
public class DataReport implements Comparable<DataReport> {

    @Id
    @Column(name = "date")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date date;
    private Integer deaths;
    private Integer confirmed;
    private Integer recovered;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }


    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }


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
}
