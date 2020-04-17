package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.Region;
import com.tacs.rest.entity.User;

@RestController
public class CountryRestController {

//	@Autowired
//	private CountriesService countriesService;
		
	@GetMapping("/countries")
	public List<Country> listaDePaisesCercanos(@RequestParam(required = false) String near){
		
		if(near != null) {
			//retornará la lista de paises cercanos por region
//			return paisesService.findNearCountrys();
			
			//MOCK
			List<Country> countries = new ArrayList<Country>();
			Region regionAmSur = new Region();
			regionAmSur.setId(1);
			regionAmSur.setNameRegion("America del Sur");
			regionAmSur.setSubRegion("Sur");

			Country brasil = new Country();
			brasil.setId(1);
			brasil.setName("Brasil");
			brasil.setRegion(regionAmSur);
		
			Country uruguay = new Country();
			uruguay.setId(2);
			uruguay.setName("Uruguay");
			uruguay.setRegion(regionAmSur);

			Country chile = new Country();
			chile.setId(3);
			chile.setName("Chile");
			chile.setRegion(regionAmSur);
			
			countries.add(uruguay);
			countries.add(chile);
			countries.add(brasil);
			
			return countries;
		}else {
		//retornará todos los paises
//		return countriesService.findAll();
		
		//MOCK
		List<Country> countries  = new ArrayList<Country>();
		
		Country argentina = new Country();
		argentina.setId(1);
		argentina.setName("Argentina");

		Country brasil = new Country();
		brasil.setId(2);
		brasil.setName("Brasil");

		Country chile = new Country();
		chile.setId(3);
		chile.setName("Chile");

		countries.add(argentina);
		countries.add(brasil);
		countries.add(chile);
		
		return countries;
		}
		
		
		
	}
	
	@PostMapping("/listCountries")
	public List<CountriesList> addListCountries(@RequestBody User user) {
		//return countriesService.addListCountries(user);
		//Mock
		return user.getCountriesList();
	}
	
	@PutMapping("/listCountries")
	public List<CountriesList> modifyListCountries(@RequestBody User user) {
		//retornará la lista de paises cercanos
		//return countriesService.addListCountries(user);
		
		//Mock
		return user.getCountriesList();
	}
	
}
