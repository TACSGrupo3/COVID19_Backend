package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.Country;

public interface CountryService {
	
	public List<Country> findAll();
	
	public Country findById(int id);
	
	public void save(Country country);
	
	public void deleteById(int id);

	public List<Country> findNearCountrys(String country);

	public List<Country> findByIso(String iso);
}