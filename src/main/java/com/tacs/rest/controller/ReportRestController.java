package com.tacs.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.Country;
import com.tacs.rest.services.ReportService;

@RestController
public class ReportRestController {

	@Autowired
	ReportService reportService;
	
	//EJ: http://localhost:8080/api/report?country=ARGENTINA&offset=1&country=Brasil&offset=2
    @GetMapping("/report")
    // add produces = MediaType.APPLICATION_JSON_VALUE when done
    public List<Country> getReport(@RequestParam(value="country") List<String> countries,
    		@RequestParam(value="offset") List<String>  offsets) {
    	if(countries.size() == offsets.size()) {
        //		MOCK
    	return reportService.reportData(countries, offsets);
    	}else {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar un offset para cada pais");
    	}
    }
}
