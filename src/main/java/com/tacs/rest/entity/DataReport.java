package com.tacs.rest.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table (name = "public.DATA_REPORT")
public class DataReport implements Comparable<DataReport> {

    private Date date;
    private Integer deaths;
    private Integer confirmed;
    private Integer recovered;


    @Column(name = "date" , nullable = false)
    @PrimaryKeyJoinColumn
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
}
