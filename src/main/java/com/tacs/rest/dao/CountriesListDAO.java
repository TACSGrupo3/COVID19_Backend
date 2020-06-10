package com.tacs.rest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.CountriesList;

@Repository
public interface CountriesListDAO extends CrudRepository<CountriesList, Integer>{

}

