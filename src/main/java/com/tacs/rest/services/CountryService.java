package com.tacs.rest.services;

import java.util.Date;
import java.util.List;

import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;


public interface CountryService {
	
	List<Country> findAll();
	
	Country findById(int id);
	
	void save(Country country);
	
	void deleteById(int id);

	List<Country> findNearCountries(String latitud, String longitud, String maxCountries);

	List<Country> findByIso(String iso);
	
	boolean existsCountries (List<Country> countries);
	
	boolean addSameCountries(List<Country> countries);
	
	public List<Country> searchAndSaveCountries(List<Country> countries);

	Country findByName(String countryName);

	void saveAll(List<Country> countries);

	List<DataReport> getReport(int id);

	List<Country> findCountriesByIds(List<Integer> countriesIds);

	Country findByDataReportDate(Date date);
	
}