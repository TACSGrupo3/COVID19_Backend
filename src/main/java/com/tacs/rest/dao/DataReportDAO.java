package com.tacs.rest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tacs.rest.entity.DataReport;

public interface DataReportDAO extends CrudRepository<DataReport, Integer> {

	List<DataReport> findByCountry_idCountryOrderByDateAsc(int id);
	List<DataReport> findByCountry_idCountryOrderByDateDesc(int id);
}
