package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.List;


import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Location;

public class Country {

    private int id;
    private String name;
    private Region region;
    private List<DataReport> dataReport = new ArrayList<>();
    private int offset;
    private Integer deaths;
    private Integer confirmed;
    private Integer recovered;
    private Location location;
    private Countrycode countryCode;
    private String lastupdate;

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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<DataReport> getDataReport() {
        return dataReport;
    }

    public void setDataReport(List<DataReport> dataReport) {
        this.dataReport = dataReport;
    }

    public void addDataReport(DataReport dataR) {
        dataReport.add(dataR);
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
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

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setCountryCode(Countrycode countryCode) {
        this.countryCode = countryCode;
    }

    public Countrycode getCountryCode() {
        return countryCode;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

}
