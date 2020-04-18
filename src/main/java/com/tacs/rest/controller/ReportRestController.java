package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;

@RestController
public class ReportRestController {

    @GetMapping("/report/{listId}")
    // add produces = MediaType.APPLICATION_JSON_VALUE when done
    public List<CountriesList> getReport(@PathVariable("listId") int listId, @RequestParam int baseCountryId) {
        // User user = userService.findById(userId);
        // countries = user.suscribedCountries(); --> validate that baseCountryId belongs to the suscribedCountries
        // call the external api for each countries element.
        // do the correct mapping country/days(date, infected, dead, recovered)/offset (based on the baseCountryId first date)
        // return the json to front end

        //		MOCK
    	List<CountriesList> listCountries = new ArrayList<CountriesList>();
    	CountriesList countryList = new CountriesList();
    	countryList.setId(listId);
    	countryList.setCreationDate(new Date());
    	countryList.setName("Listado1");
    	
    	Country argentina = new Country();
    	argentina.setId(1);
    	argentina.setName("Argentina");
    	argentina.setOffset(10);
    	List<DataReport> listDataArgentina = new ArrayList<DataReport>();
    	
    	DataReport dataArgentina = new DataReport();
    	dataArgentina.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 11).getTime());
    	dataArgentina.setDeaths(0);
    	dataArgentina.setInfected(12);
    	dataArgentina.setCured(0);
    	listDataArgentina.add(dataArgentina);
    	dataArgentina = new DataReport();
    	dataArgentina.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 12).getTime());
    	dataArgentina.setDeaths(0);
    	dataArgentina.setInfected(14);
    	dataArgentina.setCured(0);
    	listDataArgentina.add(dataArgentina);
    	
    	Country brasil = new Country();
    	brasil.setId(2);
    	brasil.setName("Brasil");
    	brasil.setOffset(0);
    	List<DataReport> listDataBrasil = new ArrayList<DataReport>();
    	
    	DataReport dataBrasil = new DataReport();
    	dataBrasil.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
    	dataBrasil.setDeaths(1);
    	dataBrasil.setInfected(40);
    	dataBrasil.setCured(2);
    	listDataBrasil.add(dataBrasil);
    	dataBrasil = new DataReport();
    	dataBrasil.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 2).getTime());
    	dataBrasil.setDeaths(2);
    	dataBrasil.setInfected(67);
    	dataBrasil.setCured(2);
    	listDataBrasil.add(dataBrasil);
    	
    	Country chile = new Country();
    	chile.setId(3);
    	chile.setName("Chile");
    	chile.setOffset(4);
    	List<DataReport> listDataChile = new ArrayList<DataReport>();
    	
    	DataReport dataChile = new DataReport();
    	dataChile.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 5).getTime());
    	dataChile.setDeaths(6);
    	dataChile.setInfected(35);
    	dataChile.setCured(1);
    	listDataChile.add(dataChile);
    	dataChile = new DataReport();
    	dataChile.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 6).getTime());
    	dataChile.setDeaths(6);
    	dataChile.setInfected(40);
    	dataChile.setCured(2);
    	listDataChile.add(dataChile);
    	
    	argentina.setDataReport(listDataArgentina);
    	brasil.setDataReport(listDataBrasil);
    	chile.setDataReport(listDataChile);
    	
    	List<Country> listOfCountries = new ArrayList<Country>();
    	listOfCountries.add(argentina);
    	listOfCountries.add(brasil);
    	listOfCountries.add(chile);
	    	
    	countryList.setCountries(listOfCountries);
    	
    	listCountries.add(countryList);
    	
    	return listCountries;
    }
}
