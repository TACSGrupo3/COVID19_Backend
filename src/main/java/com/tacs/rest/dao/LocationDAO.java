package com.tacs.rest.dao;

import org.springframework.data.repository.CrudRepository;

import com.tacs.rest.apiCovid.Location;

public interface LocationDAO extends CrudRepository<Location, Integer>{

}
