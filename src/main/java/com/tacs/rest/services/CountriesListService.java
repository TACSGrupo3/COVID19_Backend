package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CountriesListService {

    List<CountriesList> findAll();

    List<CountriesList> findFilterByDate(Date date);

    List<CountriesList> findByUserId(int userId);

    CountriesList findById(int id);

    CountriesList modifyListCountries(int countryListId, CountriesList list) throws Exception;

    List<CountriesList> addListCountries(String userId, List<CountriesList> countriesList) throws Exception;

    List<CountriesList> deleteListCountries(String countriesListId) throws Exception;

    List<User> getIntrested(int countryId) throws Exception;

    void save(CountriesList countryList);

    void saveAll(List<CountriesList> countriesList);

    List<CountriesList> findByName(String name);
}