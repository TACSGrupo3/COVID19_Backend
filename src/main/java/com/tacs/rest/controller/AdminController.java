package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;

// asegurar que me mande los parametros y enviar error si no lo hace.

@RestController
@RequestMapping("/admin") //esta sera la raiz de la url, es decir http:	//127.0.0.1:8080/adminWS/
public class AdminController {
	
	
	/*CONSIGNA
	 * Como administrador quiero poder ver los siguientes datos de un usuario:
	Usuario
	Cantidad de listas
	Cantidad de países en sus listas.
	Último acceso
	Como administrador quiero seleccionar 2 listas de usuarios diferentes y ver si tienen algún país en común.
	Como administrador quiero seleccionar un país y ver la cantidad de usuarios que se interesaron en el mismo (lo agregaron a una lista).
	Como administrador quiero conocer la cantidad total de listas registrados en el sistema
	 * 
	 * LOGICA
	 * Lo voy a buscar a la base y lo traigo
	 * 
	 * http://127.0.0.1:8080/admin/users/1
	 */
	
	@GetMapping("/user/{userId}")
	public User getUser(@PathVariable int userId){
		
//		MOCK
		User user = new User();
		user.setId(userId);
		user.setFirstName("TEST");
		user.setLastName("TEST");
		
		Country argentina = new Country();
		argentina.setId(1);
		argentina.setName("Argentina");
		
		Country uruguay = new Country();
		uruguay.setId(2);
		uruguay.setName("Uruguay");
		
		List<Country> paisesSA  = new ArrayList<Country>();
		paisesSA.add(argentina);
		paisesSA.add(uruguay);
				
		Country usa = new Country();
		usa.setId(3);
		usa.setName("Estados Unidos");
		
		Country china = new Country();
		china.setId(4);
		china.setName("China");
		
		Country rusia = new Country();
		rusia.setId(5);
		rusia.setName("Rusia");
		
		List<Country> paisesP  = new ArrayList<Country>();
		paisesP.add(usa);
		paisesP.add(china);
		
		CountriesList paisesSudAmericanos  = new CountriesList();
		paisesSudAmericanos.setId(1);
		paisesSudAmericanos.setName("Paises sudamericanos");
		paisesSudAmericanos.setCountries(paisesSA);
		
		CountriesList paisesPotencias  = new CountriesList();
		paisesPotencias.setId(2);
		paisesPotencias.setName("Paices potencias mundiales");
		paisesPotencias.setCountries(paisesP);
		
		//pruebo nueva funcion de addCountrie
		paisesPotencias.addCountrie(rusia);
		
		List<CountriesList> listaDeUsuario = new ArrayList<CountriesList>();
		listaDeUsuario.add(paisesPotencias);
		
		user.setCountriesList(listaDeUsuario);
		
		//pruebo nueva funcion de addList
		user.addList(paisesSudAmericanos);
		
		Date fecha = new Date();
		user.setLastAccess(fecha);
		
		return user;
	}
	
	
	//CONSIGNA:
	/*Como administrador quiero conocer la cantidad total de listas registrados en el sistema
		En el día de hoy
		En los últimos 3 días
		En la última semana
		En el último mes
		Desde el inicio de los tiempos
	*/

	//LOGICA
	//Me parece que con una query a la BDD se soluciona.
	//Igualmente hay que ver:
	//
	//	2)Como se recibe el parametro de hasta cuando quiere ver. Un string? parece mucho quilombo. Un id que identifique hasta cuando
	//		quiere ver? Puede ser pero deberia estar claro en algun lado. Por ahora me gusta mas esta última.
	
	//Por ahora es:
	/*
	 * 
	 *  1 -> En el día de hoy
		2 -> En los últimos 3 días
		3 -> En la última semana
		4 -> En el último mes
		5 -> Desde el inicio de los tiempos
	 * 
	 */

	//http://127.0.0.1:8080/admin/lists?IDantiguedad=2
	@GetMapping("/lists")
	public int getLists(@RequestParam int IDantiguedad){
		
		int cantListas = -1;
		
		switch(IDantiguedad){
		case 1:
			cantListas = 1;
			break;
		case 2:
			cantListas = 2;
			break;
		case 3:
			cantListas = 3;
			break;
		case 4:
			cantListas = 4;
			break;
		case 5:
			cantListas = 5;
			break;
		}

		return cantListas;
	}
	
	
	//CONSIGNA:
	//Como administrador quiero seleccionar 2 listas de usuarios diferentes y ver si tienen algún país en común.
	
	//LOGICA
	//recibo 2 id de listas, las triago de la BDD, las comparo entre si y devuelvo una lista
	//de paises en comun entre esas dos listas.


	//http://127.0.0.1:8080/admin/lists/compare?IDlistas=1,2
	//o es igual de valido:
	//http://127.0.0.1:8080/admin/lists/compare?IDlistas=1&IDlistas=2
	@GetMapping("/lists/{IDlista1}")
	public List<Country> getCountries(@PathVariable int IDlista1, @RequestParam int compare){
		
		//MOCK
		//En vez de esto va a la BDD y busca las dos listas por ID.
		
		Country argentina = new Country();
		argentina.setId(1);
		argentina.setName("Argentina");
		
		Country uruguay = new Country();
		uruguay.setId(2);
		uruguay.setName("Uruguay");
		
		Country brasil = new Country();
		brasil.setId(6);
		brasil.setName("Brasil");
		
		List<Country> paisesSA  = new ArrayList<Country>();
		paisesSA.add(argentina);
		paisesSA.add(uruguay);
		paisesSA.add(brasil);
				
		Country usa = new Country();
		usa.setId(3);
		usa.setName("Estados Unidos");
		
		Country china = new Country();
		china.setId(4);
		china.setName("China");
		
		Country rusia = new Country();
		rusia.setId(5);
		rusia.setName("Rusia");
		
		List<Country> paisesP  = new ArrayList<Country>();
		paisesP.add(usa);
		paisesP.add(china);
		paisesP.add(rusia);
		paisesP.add(brasil);
		
		CountriesList paisesSudAmericanos  = new CountriesList();
		paisesSudAmericanos.setId(IDlista1);
		paisesSudAmericanos.setName("Paises sudamericanos");
		paisesSudAmericanos.setCountries(paisesSA);
		
		CountriesList paisesPotencias  = new CountriesList();
		paisesPotencias.setId(compare);
		paisesPotencias.setName("Paices potencias mundiales");
		paisesPotencias.setCountries(paisesP);
		
		// fin del mock. a partide de aca si seria ya el comportamiento de comparar las dos listas, 
		//armar una nueva con los paises repetidos, y enviar esta nueva lista.
		
		List<Country> listaPaises1 = paisesPotencias.getCountries();
		List<Country> listaPaises2 = paisesSudAmericanos.getCountries();
		
		listaPaises1.retainAll(listaPaises2);
		
		return listaPaises1;
	}
	
	//CONSIGNA:
	/*Como administrador quiero seleccionar un país y ver la cantidad de usuarios que se interesaron en el mismo 
	 *(lo agregaron a una lista).
	*/

	//LOGICA
	//Me parece que se soluciona con una query a la BDD nomas.

	//http://127.0.0.1:8080/admin/country/3
	@GetMapping("/country/{countryId}")
	public int getInteresados(@PathVariable int countryId){

		return countryId;
	}
	
}
