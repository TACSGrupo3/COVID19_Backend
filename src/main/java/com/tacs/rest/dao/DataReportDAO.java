package com.tacs.rest.dao;

import com.tacs.rest.entity.DataReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataReportDAO extends CrudRepository<DataReport, Integer> {

    List<DataReport> findByCountry_idCountryOrderByDateAsc(int id);

    List<DataReport> findByCountry_idCountryOrderByDateDesc(int id);

    void deleteAll();
}
