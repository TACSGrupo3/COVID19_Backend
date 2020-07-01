package com.tacs.rest;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.servicesImpl.CountriesListServiceImpl;
import com.tacs.rest.servicesImpl.CountryServiceImpl;

import org.junit.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class CountryListTest {
	
	@Autowired
	private CountriesListServiceImpl countriesListServiceImpl;

	@Autowired
	private CountryServiceImpl countriesServiceImpl;
	
	@BeforeEach
	void initUseCase() {
	}

	@Test
	public void A_findAllTest() throws Exception {
		List<CountriesList> list = countriesListServiceImpl.findAll();
		Assert.assertNotNull(list);
	}
	
	@Test
	public void B_test_lista_sin_nombre() {
		CountriesList countriesList = new CountriesList();
		List<Country> listOfCountries = new ArrayList<Country>();
		countriesList.setCountries(listOfCountries);
		
		List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
		
		listOfCountriesList.add(countriesList);
		List<CountriesList> result = null;
		
		
		try {
			result = this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("El nombre de la lista no puede estar vacío."));
		}
		
	}
	
	@Test
	public void C_test_pais_invalido() {
		CountriesList countriesList = new CountriesList();
		Country country = new Country();
		country.setName("País Inválido");
		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries.add(country);
		countriesList.setCountries(listOfCountries);
		
		List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
		
		listOfCountriesList.add(countriesList);
		List<CountriesList> result = null;
		
		listOfCountriesList.get(0).setName("Lista con país inválido");
		
		try {
			result = this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("Ingresó un país Inválido"));
		}
		
	}
	
	@Test
	public void D_test_lista_con_pais_duplicado() {
		
		Country countryId1 = this.countriesServiceImpl.findById(1);
		CountriesList countriesList = new CountriesList();
		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries.add(countryId1);
		listOfCountries.add(countryId1);
		countriesList.setCountries(listOfCountries);
		
		List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
		
		listOfCountriesList.add(countriesList);
		listOfCountriesList.get(0).setName("Lista con duplicados");
		
		List<CountriesList> result = null;

		try {
			result = this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("Ingresó el mismo pais 2 veces en la misma lista"));
		}
		
	}
	
	public List<CountriesList> crearLista() {
		Country countryId1 = this.countriesServiceImpl.findById(1);
		CountriesList countriesList = new CountriesList();
		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries.add(countryId1);
		countriesList.setCountries(listOfCountries);
		countriesList.setName("Nombre de Prueba");
		List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
		
		listOfCountriesList.add(countriesList);
		listOfCountriesList.get(0).setName("Nombre de Prueba");
		
		return listOfCountriesList;
	}
	@Test
	public void E_test_correcto() {
		
		List<CountriesList> result = null;

		try {
			result = this.countriesListServiceImpl.addListCountries("1", crearLista());
		} catch (Exception e) {
			Assert.fail();
		}
		
		Assert.assertNotNull(result);
		
	}
	
	@Test
	public void F_test_usuario_con_lista_existente() {
		
		List<CountriesList> listOfCountriesList = crearLista();
		try {
			this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
			this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("Este usuario ya posee una lista con igual nombre"));
		}
	}
	
	@Test
	public void G_test_modificar_lista() {
		
		List<CountriesList> listOfCountriesList = crearLista();
		try {
			this.countriesListServiceImpl.addListCountries("1", listOfCountriesList);
		} catch (Exception e) {
			Assert.fail();
		}
		
		CountriesList countriesList = listOfCountriesList.get(0);
		countriesList.setName("Cambio de Nombre");		
		try {
			this.countriesListServiceImpl.modifyListCountries(1, countriesList);
		} catch (Exception e) {
			Assert.fail();
		}
		
		Assert.assertEquals(countriesList.getName(), countriesList.getName());
		
	}
	
	@Test
	public void H_test_eliminar_lista() {

		CountriesList list = null;
		try {
			this.countriesListServiceImpl.deleteListCountries("1");
			list = this.countriesListServiceImpl.findById(1);
		}catch (Exception e) {
			Assert.fail();
		}
		
		Assert.assertNull(list);
		
	}
}
