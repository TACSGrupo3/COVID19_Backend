package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;

import com.tacs.rest.entity.Country;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;

@RestController
@RequestMapping("/countries") 
public class CountryRestController {

//	@Autowired
//	private CountriesService countriesService;
	
	@GetMapping("/countries")
	public List<Country> findAll(){
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
	
	
	@GetMapping("/near/{locationId}")
	public List<Country> listaDePaisesCercanos(@PathVariable int locationId){
		/**
		 * TODO: A definir: 
		 * -Cual es el input del servicio
		 * -Como ordenar por cercanía los paises
		 * 
		 * */
		//retornará la lista de paises cercanos
//		return paisesService.findNearCountrys();
		
		//MOCK
		List<Country> countries = new ArrayList<Country>();

		Country brasil = new Country();
		brasil.setId(1);
		brasil.setName("Brasil");

		Country uruguay = new Country();
		uruguay.setId(2);
		uruguay.setName("Uruguay");

		Country chile = new Country();
		chile.setId(3);
		chile.setName("Chile");

		countries.add(uruguay);
		countries.add(chile);
		countries.add(brasil);
		
		return countries;
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
