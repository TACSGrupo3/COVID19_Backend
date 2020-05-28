package com.tacs.rest.services;

import java.util.Date;
import java.util.List;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface CountriesListService {
	
	List<CountriesList> findAll();
	
	List<CountriesList> findFilterByDate(Date date);
	
	List<CountriesList> findByUserId(int userId);
	
	CountriesList findById(int id);
	
	CountriesList modifyListCountries(int countryListId, CountriesList list) throws Exception;

	List<CountriesList> addListCountries(String userId, List<CountriesList> countriesList) throws Exception;

	void deleteListCountries(String countriesListId);

	List<User> getIntrested(int countryId);
}