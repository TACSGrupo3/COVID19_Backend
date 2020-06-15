package com.tacs.rest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.CountriesList;

@Repository
public interface CountriesListDAO extends PagingAndSortingRepository<CountriesList, Integer> {

	@Query(value = "SELECT c.* FROM public.COUNTRIESLIST c WHERE UPPER(NAME) LIKE %:name% ", 
			countQuery = "SELECT count(*) FROM public.COUNTRIESLIST WHERE UPPER(NAME) LIKE %:name%",
			nativeQuery = true)
	List<CountriesList> findByName(String name);

}
