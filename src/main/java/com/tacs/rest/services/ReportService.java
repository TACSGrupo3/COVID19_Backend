package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.Country;

public interface ReportService {

	List<Country> reportData(List<String> countries ,List<String> offset);
}
