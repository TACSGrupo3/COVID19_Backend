package com.tacs.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.servicesImpl.CountriesListServiceImpl;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CountryListTest {
	
	@Autowired
	private CountriesListServiceImpl countriesListServiceImpl;

	@BeforeEach
	void initUseCase() {
	}

	@Test
	public void findAllTest() throws Exception {
		List<CountriesList> list = countriesListServiceImpl.findAll();
		Assert.assertNotNull(list);
	}
	
	@Test
	public void addCountry() {
		CountriesList countriesList = new CountriesList();
		Country country = new Country();
		country.setName("País de prueba");

		List<CountriesList> list = new ArrayList<CountriesList>();
		countriesList.getCountries().add(country);
		
		list.add(countriesList);
		try {
			this.countriesListServiceImpl.addListCountries("1", list);
		} catch (Exception e) {
		}
		
	}
}
