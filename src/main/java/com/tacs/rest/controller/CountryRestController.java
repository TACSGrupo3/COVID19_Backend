package com.tacs.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountryService;

@RestController
public class CountryRestController {

	@Autowired
	private CountryService countriesService;
	
	@GetMapping("/countries")
	public List<Country> listaDePaisesCercanos(@RequestParam(required = false) String near){
		if(near != null) {
//			retornará la lista de paises cercanos por region
			return countriesService.findNearCountrys(near);
		}else {
			//retornará todos los paises
			return countriesService.findAll();
		}	
	}
	
	@PostMapping("/listCountries")
	public List<CountriesList> addListCountries(@RequestBody User user) {
		return countriesService.addListCountries(user);
	}
	
	@PutMapping("/listCountries")
	public List<CountriesList> modifyListCountries(@RequestBody User user) {
		return countriesService.modifyListCountries(user);
	}
	
}
