package com.tacs.rest.apiCovid;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lat",
        "lng"
})

@Embeddable
public class Location {

	@Transient
    private int id_Location;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
    @JsonIgnore
    @Transient
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    public int getId() {
        return id_Location;
    }

    public void setId(int id) {
        this.id_Location = id;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("lng")
    public Double getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @JsonAnyGetter
    @Transient
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}