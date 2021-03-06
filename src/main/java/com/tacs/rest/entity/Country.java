package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Location;

@Entity

@Table(name = "public.COUNTRY")
public class Country {

    @Id
    @Column(name = "id_Country")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCountry;
    @Column(name = "name", nullable = false)
    private String name;
    
    
    @OneToMany(mappedBy = "country", cascade = CascadeType.MERGE)
    private List<DataReport> dataReport = new ArrayList<>();
    
    @ManyToMany(mappedBy = "countries")
    List<CountriesList> countriesList;
    
    @Column(name = "deaths", nullable = false)
    private Integer deaths;
    @Column(name = "confirmed", nullable = false)
    private Integer confirmed;
    @Column(name = "recovered", nullable = false)
    private Integer recovered;
    
    @Embedded
    @AttributeOverrides({
    	  @AttributeOverride( name = "lat", column = @Column(name = "lat")),
    	  @AttributeOverride( name = "lng", column = @Column(name = "lng"))})
    private Location location;
    

    @Embedded
    @AttributeOverrides({
  	  @AttributeOverride( name = "iso2", column = @Column(name = "iso2")),
  	  @AttributeOverride( name = "iso3", column = @Column(name = "iso3"))})
    private Countrycode countryCode;
    
    @Column(name = "last_update", nullable = true)
    private String lastupdate;
    
    public int getIdCountry() {
		return idCountry;
	}

	public void setIdCountry(int idCountry) {
		this.idCountry = idCountry;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Countrycode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Countrycode countryCode) {
        this.countryCode = countryCode;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public double getDistance(final String latitude, String longitude) {
        final double dx = this.getLocation().getLat() - Double.valueOf(latitude);
        final double dy = this.getLocation().getLng() - Double.valueOf(longitude);

        return Math.sqrt(dx * dx + dy * dy);
    }

}