package com.tacs.rest.controller;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Country> listPaises(@RequestParam(required = false) String latitude,
                                    @RequestParam(required = false) String longitude, @RequestParam(required = false) String maxCountries,
                                    @RequestParam(required = false) String iso) {
        if (latitude != null && longitude != null && maxCountries != null) {
//			retornará la lista de paises cercanos por region
////			
//			String uri = "http://api.geonames.org/countryCodeJSON?lat=" + latitud + "&lng=" + longitud + "&username=tacsg3";
//
//			RestTemplate restTemplate = new RestTemplate();
//			HashMap result = restTemplate.getForObject(uri, HashMap.class);

//			String country = (String) result.get("countryName");
            return countriesService.findNearCountrys(latitude, longitude, maxCountries);

        } else if (iso != null) {
            return countriesService.findByIso(iso);
        } else {
            // retornará todos los paises
            return countriesService.findAll();
        }
    }

    @GetMapping("/countriesList")
    public List<CountriesList> getCountriesList(@RequestParam(required = false) String iso) throws JsonIOException, JsonSyntaxException, URISyntaxException, IOException {
        return countriesListService.findAll();
    }

    @GetMapping("/users/{userId}/countriesList")
    public List<CountriesList> getCountriesListByUserId(@PathVariable String userId) {
        return countriesListService.findByUserId(Integer.valueOf(userId));
    }

    @PostMapping("/users/{userId}/countriesList")
    public List<CountriesList> addListCountries(@RequestBody List<CountriesList> countriesList, @PathVariable String userId) throws Exception {
    	return countriesListService.addListCountries(userId, countriesList);
    }

    @DeleteMapping("/countriesList/{countriesListId}")
    public void deleteListCountries(@PathVariable String countriesListId) {
        countriesListService.deleteListCountries(countriesListId);
        return;
    }
    
    @PutMapping("/countriesList/{countriesListId}")
    public CountriesList modifyListCountries(@PathVariable(required = true) Integer countriesListId,
                                             @RequestBody CountriesList list) throws Exception {
        CountriesList modifiedList = countriesListService.modifyListCountries(countriesListId, list);

        if (modifiedList == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe la lista a modificar");

        return modifiedList;
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








