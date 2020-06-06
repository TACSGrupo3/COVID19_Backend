package com.tacs.rest.dao;

import org.springframework.data.repository.CrudRepository;

import com.tacs.rest.apiCovid.Countrycode;

public interface CountryCodeDAO extends CrudRepository<Countrycode, Integer> {

}
