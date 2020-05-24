package com.tacs.rest.services;

import java.util.Date;
import java.util.List;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface CountriesListService {
	
	public List<CountriesList> findAll();
	
	public List<CountriesList> findFilterByDate(Date date);
	
	public List<CountriesList> findByUserId(int userId);
	
	public CountriesList findById(int id);
	
	public CountriesList modifyListCountries(int countryListId, CountriesList list) throws Exception;

	public List<CountriesList> addListCountries(String userId, List<CountriesList> countriesList) throws Exception;

	public void deleteListCountries(String countriesListId);

	public List<User> getIntrested(int countryId);
}