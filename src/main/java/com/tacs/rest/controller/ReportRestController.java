package com.tacs.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.Country;
import com.tacs.rest.services.ReportService;

@RestController
public class ReportRestController {

    @Autowired
    ReportService reportService;

    //EJ: http://localhost:8080/api/report?country=ARGENTINA&offset=1&country=Brasil&offset=2
    @GetMapping("/report")
    // add produces = MediaType.APPLICATION_JSON_VALUE when done
    public ResponseEntity<List<Country>> getReport(@RequestParam(value = "country") List<Integer> countries,
                                   @RequestParam(value = "offset") List<String> offsets) throws Exception {
    	return new ResponseEntity<List<Country>>(reportService.reportData(countries, offsets), HttpStatus.OK);

    }
}
