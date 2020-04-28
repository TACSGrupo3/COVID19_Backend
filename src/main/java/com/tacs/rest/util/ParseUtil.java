package com.tacs.rest.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.Region;
import com.tacs.rest.entity.User;

public class ParseUtil {
	
	public static Country parseJsonToCountry(JSONObject json) {
    	Country country = new Country();
		country.setId(Integer.valueOf((String) json.get("id")));
		country.setName((String) json.get("name"));
		Region region = new Region();
		JSONObject jsonRegion = (JSONObject) json.get("region");
		
		region.setId(Integer.valueOf((String) jsonRegion.get("id")));
		region.setNameRegion((String) jsonRegion.get("nameRegion"));
		region.setSubRegion((String) jsonRegion.get("subRegion"));
		
		country.setRegion(region);
		
		return country;
    }
    
    public static User parseJsonToUser(JSONObject json) {
    	User user = new User();
		user.setId(Integer.valueOf((String) json.get("id")));
		user.setFirstName((String) json.get("firstName"));
		user.setLastName((String) json.get("lastName"));
		user.setUsername((String) json.get("username"));
		user.setPassword((String) json.get("password"));
		JSONArray jsonCountriesList = (JSONArray) json.get("countriesList");
		
		List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
		
		for(int i = 0; i < jsonCountriesList.size(); i++) {
			listOfCountriesList.add(parseJsonToCountryList((JSONObject) jsonCountriesList.get(i)));
		}
		user.setCountriesList(listOfCountriesList);
		
		return user;
    }
    
    public static CountriesList parseJsonToCountryList(JSONObject json) {
    	CountriesList countriesList = new CountriesList();
		
		countriesList.setId(Integer.valueOf((String) json.get("id")));
		countriesList.setName((String) json.get("name"));
		countriesList.setCreationDate(new Date());
		
		JSONArray jsonCountries = (JSONArray) json.get("countries");
		
		List<Country> listOfCountry = new ArrayList<Country>();
		for(int i = 0; i < jsonCountries.size(); i++) {
			listOfCountry.add(parseJsonToCountry((JSONObject) jsonCountries.get(i)));
		}
		
		countriesList.setCountries(listOfCountry);
		return countriesList;
    }
}
