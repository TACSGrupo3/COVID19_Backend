package com.tacs.rest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.Country;

@Repository
public interface CountryDAO extends CrudRepository<Country, Integer>{

}
