package com.tacs.rest.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping("/countries")
	public List<Country> listPaises(@RequestParam(required = false) String latitud,
			@RequestParam(required = false) String longitud) {
		if (latitud != null && longitud != null) {
//			retornará la lista de paises cercanos por region
//			
			String uri = "http://api.geonames.org/countryCodeJSON?lat=" + latitud + "&lng=" + longitud + "&username=tacsg3";

			RestTemplate restTemplate = new RestTemplate();
			HashMap result = restTemplate.getForObject(uri, HashMap.class);

			String country = (String) result.get("countryName");
			return countriesService.findNearCountrys(country);
		} else {
			// retornará todos los paises
			return countriesService.findAll();
		}
	}

	@GetMapping("/countriesList")
	public List<CountriesList> getCountriesList() {
		return countriesListService.findAll();
	}

	@GetMapping("/user/{userId}/countriesList")
	public List<CountriesList> getCountriesListByUserId(@PathVariable String userId) {
		return countriesListService.findByUserId(Integer.valueOf(userId));
	}

	@PostMapping("/countriesList")
	public List<CountriesList> addListCountries(@RequestBody User user) {
		return countriesListService.addListCountries(user);
	}

	@PatchMapping("/countriesList/{countriesListId}")
	public CountriesList modifyListCountries(@PathVariable(required = true) Integer countriesListId,
			@RequestBody CountriesList list) {
		CountriesList modifiedList = countriesListService.modifyListCountries(countriesListId, list);

		if (modifiedList == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe la lista a modificar");

		return modifiedList;
	}
}
