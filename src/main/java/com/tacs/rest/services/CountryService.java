package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;

public interface CountryService {
	
	public List<Country> findAll();
	
	public Country findById(int id);
	
	public void save(Country country);
	
	public void deleteById(int id);
	
	public List<CountriesList> addListCountries(User user);
	
	public List<CountriesList> modifyListCountries(User user);

	public List<Country> findNearCountrys(String country);
}