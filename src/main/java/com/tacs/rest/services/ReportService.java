package com.tacs.rest.services;

import java.util.Date;
import java.util.List;

import com.tacs.rest.entity.Country;

public interface ReportService {

	List<Country> reportData(List<Integer> countries, List<String> offset) throws Exception;
}
