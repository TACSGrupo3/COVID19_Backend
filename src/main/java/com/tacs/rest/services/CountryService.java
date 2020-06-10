package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.Country;

public interface CountryService {
	
	List<Country> findAll();
	
	Country findById(int id);
	
	void save(Country country);
	
	void deleteById(int id);

	List<Country> findNearCountries(String latitud, String longitud, String maxCountries);

	List<Country> findByIso(String iso);
}