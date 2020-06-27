package com.tacs.rest.services;

import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;

import java.util.List;

public interface ReportService {

    public List<Country> reportData(List<Integer> countries, List<String> offset) throws Exception;

    public void saveReportService(DataReport dataReport);

    void saveAll(List<DataReport> dataReports);

    List<DataReport> findByCountryId(int countryId);

    void deleteAll();
}
