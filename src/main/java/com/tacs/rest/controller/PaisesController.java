package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.ListaPaises;
import com.tacs.rest.entity.Pais;
import com.tacs.rest.entity.User;

@RestController
@RequestMapping("/countries") 
public class PaisesController {

//	@Autowired
//	private PaisesService paisesService;
	
	@GetMapping("/countries")
	public List<Pais> findAll(){
		//retornará todos los paises
//		return paisesService.findAll();
		
		//MOCK
		List<Pais> paises  = new ArrayList<Pais>();
		
		Pais argentina = new Pais();
		argentina.setId(1);
		argentina.setName("Argentina");

		Pais brasil = new Pais();
		brasil.setId(2);
		brasil.setName("Brasil");
		
		Pais chile = new Pais();
		chile.setId(3);
		chile.setName("Chile");
		
		paises.add(argentina);
		paises.add(brasil);
		paises.add(chile);
		
		return paises;
	}
	
	
	@GetMapping("/findNearList/{locationId}")
	public List<Pais> listaDePaisesCercanos(@PathVariable int locationId){
		/**
		 * TODO: A definir: 
		 * -Cual es el input del servicio
		 * -Como ordenar por cercanía los paises
		 * 
		 * */
		//retornará la lista de paises cercanos
//		return paisesService.findNearCountrys();
		
		//MOCK
		List<Pais> paises  = new ArrayList<Pais>();
		
		Pais brasil = new Pais();
		brasil.setId(1);
		brasil.setName("Brasil");
		
		Pais uruguay = new Pais();
		uruguay.setId(2);
		uruguay.setName("Uruguay");
		
		Pais chile = new Pais();
		chile.setId(3);
		chile.setName("Chile");
		
		paises.add(uruguay);
		paises.add(chile);
		paises.add(brasil);
		
		return paises;
	}
	
	@PostMapping("/addListCountries")
	public List<ListaPaises> addListCountries(@RequestBody User user) {
		//retornará la lista de paises cercanos
		//return paisesService.addListCountries(user);
		
		//Mock
		return user.getListados();
	}
	
	@PutMapping("/modifyListCountries")
	public List<ListaPaises> modifyListCountries(@RequestBody User user) {
		//retornará la lista de paises cercanos
		//return paisesService.addListCountries(user);
		
		//Mock
		return user.getListados();
	}
	
}
