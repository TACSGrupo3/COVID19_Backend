package com.tacs.rest.apiCovid;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "provincestate",
        "countryregion",
        "lastupdate",
        "location",
        "countrycode",
        "confirmed",
        "deaths",
        "recovered"
})
public class Covid19_Latestresponse {
    @JsonProperty("provincestate")
    private String provincestate;
    @JsonProperty("countryregion")
    private String countryregion;
    @JsonProperty("lastupdate")
    private String lastupdate;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("countrycode")
    private Countrycode countrycode;
    @JsonProperty("confirmed")
    private Integer confirmed;
    @JsonProperty("deaths")
    private Integer deaths;
    @JsonProperty("recovered")
    private Integer recovered;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("provincestate")
    public String getProvincestate() {
        return provincestate;
    }

    @JsonProperty("provincestate")
    public void setProvincestate(String provincestate) {
        this.provincestate = provincestate;
    }

    @JsonProperty("countryregion")
    public String getCountryregion() {
        return countryregion;
    }

    @JsonProperty("countryregion")
    public void setCountryregion(String countryregion) {
        this.countryregion = countryregion;
    }

    @JsonProperty("lastupdate")
    public String getLastupdate() {
        return lastupdate;
    }

    @JsonProperty("lastupdate")
    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("countrycode")
    public Countrycode getCountrycode() {
        return countrycode;
    }

    @JsonProperty("countrycode")
    public void setCountrycode(Countrycode countrycode) {
        this.countrycode = countrycode;
    }

    @JsonProperty("confirmed")
    public Integer getConfirmed() {
        return confirmed;
    }

    @JsonProperty("confirmed")
    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    @JsonProperty("deaths")
    public Integer getDeaths() {
        return deaths;
    }

    @JsonProperty("deaths")
    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    @JsonProperty("recovered")
    public Integer getRecovered() {
        return recovered;
    }

    @JsonProperty("recovered")
    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}