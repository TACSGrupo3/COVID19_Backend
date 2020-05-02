package com.tacs.rest.services;

import java.util.Date;
import java.util.List;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;

public interface CountriesListService {
	
	public List<CountriesList> findAll();
	
	public List<CountriesList> findFilterByDate(Date date);
	
	public List<CountriesList> findByUserId(int userId);
	
	public CountriesList findById(int id);
	
	public void save(CountriesList countriesList);
	
	public void deleteById(int id);

	public CountriesList modifyListCountries(int countryListId, CountriesList list);

	public List<CountriesList> addListCountries(String userId, List<CountriesList> countriesList);
}