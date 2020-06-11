package com.tacs.rest.controller;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_briefResponse;
import com.tacs.rest.apiCovid.Covid19_latestResponse;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;


@RestController
public class CountryRestController {

    @Autowired
    private CountryService countriesService;

    @Autowired
    private CountriesListService countriesListService;


    ConnectionApiCovid apiCovid = new ConnectionApiCovid();
    Gson gson = new Gson();
    java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>() {
    }.getType();
    Collection<Covid19_latestResponse> latestResponse;

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> listPaises(@RequestParam(required = false) String latitude,
                                    @RequestParam(required = false) String longitude, @RequestParam(required = false) String maxCountries,
                                    @RequestParam(required = false) String iso) {
        if (latitude != null && longitude != null && maxCountries != null) {
        	return new ResponseEntity<List<Country>>(countriesService.findNearCountries(latitude, longitude, maxCountries), HttpStatus.OK);

        } else if (iso != null) {
        	return new ResponseEntity<List<Country>>(countriesService.findByIso(iso), HttpStatus.OK);
        } else {
        	return new ResponseEntity<List<Country>>(countriesService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/countriesList")
    public ResponseEntity<List<CountriesList>> getCountriesList() throws JsonIOException, JsonSyntaxException, URISyntaxException, IOException {
    	return new ResponseEntity<List<CountriesList>>(countriesListService.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("/countriesList/{listId}")
    public ResponseEntity<CountriesList> getCountriesListById(@PathVariable(required = true) String listId) throws JsonIOException, JsonSyntaxException, URISyntaxException, IOException {
       	if(countriesListService.findById(Integer.valueOf(listId))==null)
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dicho id de list es inexistente");
    	else  	
    		return new ResponseEntity<CountriesList>(countriesListService.findById(Integer.valueOf(listId)), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/countriesList")
    public ResponseEntity<List<CountriesList>> getCountriesListByUserId(@PathVariable String userId) throws NumberFormatException, Exception {
    	if (countriesListService.findByUserId(Integer.valueOf(userId))!= null)	
    		return new ResponseEntity<List<CountriesList>>(countriesListService.findByUserId(Integer.valueOf(userId)), HttpStatus.OK);      
    	else
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dicho user id es inexistente");
    }

    @PostMapping("/users/{userId}/countriesList")
    public ResponseEntity<List<CountriesList>> addListCountries(@RequestBody List<CountriesList> countriesList, @PathVariable String userId) throws Exception {
    		return new ResponseEntity<List<CountriesList>>(countriesListService.addListCountries(userId, countriesList), HttpStatus.OK);
    }

    @DeleteMapping("/countriesList/{countriesListId}")
    public ResponseEntity<List<CountriesList>> deleteListCountries(@PathVariable String countriesListId) throws Exception {
        return new ResponseEntity<List<CountriesList>>(countriesListService.deleteListCountries(countriesListId), HttpStatus.OK);       
    }
    
    @PutMapping("/countriesList/{countriesListId}")
    public CountriesList modifyListCountries(@PathVariable(required = true) Integer countriesListId,
                                             @RequestBody CountriesList list) throws Exception {
        CountriesList modifiedList = countriesListService.modifyListCountries(countriesListId, list);
        if (modifiedList == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe la lista a modificar");

        return modifiedList;
    }
    @Autowired
    CountryService cs;

    @PostMapping("/countriesList/prueba")
    public ResponseEntity<Country>  hacerPrueba (@Validated @RequestBody Country country) {
    	cs.save(country);
    	return new ResponseEntity<Country>(country, HttpStatus.OK);
    }


    @GetMapping("/countries/brief")
    public ResponseEntity<Covid19_briefResponse> cantidadMuertosTotales() throws JsonSyntaxException, JsonIOException, IOException, URISyntaxException {

        Covid19_briefResponse cv = gson.fromJson(apiCovid.connectionWithoutParams("brief"), Covid19_briefResponse.class);
        return new ResponseEntity<Covid19_briefResponse>(cv, HttpStatus.OK);

    }

    @GetMapping("/countries/latest")
    public ResponseEntity<Collection<Covid19_latestResponse>> latests(@RequestParam(required = false) String iso) throws JsonSyntaxException, JsonIOException, IOException, URISyntaxException {


        if (iso == null) {
            latestResponse = gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);

        } else {
            latestResponse = gson.fromJson(apiCovid.connectionWithParams("latest", iso), collectionType);
        }

        return new ResponseEntity<Collection<Covid19_latestResponse>>(latestResponse, HttpStatus.OK);
    }

}	








