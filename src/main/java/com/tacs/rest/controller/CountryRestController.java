package com.tacs.rest.controller;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.RestApplication;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.apiCovid.Location;
import com.tacs.rest.apiCovid.Covid19_briefResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	
	@GetMapping("/countries/brief")
	public ResponseEntity<Covid19_briefResponse> cantidadMuertosTotales() throws JsonSyntaxException, JsonIOException, IOException{
		
		ConnectionApiCovid apiCovid = new ConnectionApiCovid();
 
		Gson gson = new Gson();
		Covid19_briefResponse cv= gson.fromJson(apiCovid.connectionWithoutParams("brief"), Covid19_briefResponse.class);
		return new ResponseEntity<Covid19_briefResponse> (cv,HttpStatus.OK);	
				
	}
	
	@GetMapping("/countries/latest")
	public ResponseEntity<Collection<Covid19_latestResponse>>  latests(@RequestParam(required=false)String iso, @RequestParam(required=false)Boolean onlyCountries) throws JsonSyntaxException, JsonIOException, IOException, URISyntaxException{
	
		ConnectionApiCovid apiCovid = new ConnectionApiCovid();

		Gson  gson = new Gson();
		java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>(){}.getType();
		
		Collection<Covid19_latestResponse> latestResponse;
		if(iso ==null && onlyCountries == null) {
			latestResponse= gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);
			
		}
		else {
			latestResponse = gson.fromJson(apiCovid.connectionWithParams("latest",iso, onlyCountries), collectionType);
		}
				
		return new ResponseEntity<Collection<Covid19_latestResponse>>(latestResponse,HttpStatus.OK);
	}

	

	
	

}	
	
	
	

