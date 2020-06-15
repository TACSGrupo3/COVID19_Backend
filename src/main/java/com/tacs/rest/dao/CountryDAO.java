package com.tacs.rest.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.Country;

@Repository
public interface CountryDAO extends PagingAndSortingRepository<Country, Integer> {

	List<Country> findBycountryCode_Iso2(String iso2);

	List<Country> findBycountryCode_Iso3(String iso3);

	List<Country> findByDataReport_date(Date date);

}
