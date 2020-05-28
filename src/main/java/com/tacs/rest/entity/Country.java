package com.tacs.rest.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Location;

@Entity
@Table (name = "public.COUNTRY")
public class Country {

    private int id;
    private String name;
    private List<DataReport> dataReport = new ArrayList<>();
    private Integer deaths;
    private Integer confirmed;
    private Integer recovered;
    private Location location;
    private Countrycode countryCode;
    private String lastupdate;
    private CountriesList countriesList;

    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name" , nullable = false)
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

    @Column(name = "confirmed" , nullable = false)
    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    @Column(name = "deaths" , nullable = false)
    public Integer getDeaths() {
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code_id")
    public Countrycode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Countrycode countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "last_update" , nullable = false)
    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    @ManyToOne
    @JoinColumn(name="countries_list_id", nullable=false)
	public CountriesList getCountriesList() {
		return countriesList;
	}

    public double getDistance(final String latitude, String longitude) {
        final double dx = this.getLocation().getLat() - Double.valueOf(latitude);
        final double dy = this.getLocation().getLng() - Double.valueOf(longitude);

        return Math.sqrt(dx*dx + dy*dy);
    }

	public void setCountriesList(CountriesList countriesList) {
		this.countriesList = countriesList;
	}


}
