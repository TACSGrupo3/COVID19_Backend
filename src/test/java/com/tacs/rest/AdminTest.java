package com.tacs.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional

public class AdminTest {

	@Autowired
	UserService userService;
	User user = new User();
	@Autowired
	CountriesListService countriesListService;
	@Autowired
	CountryService countryService;
	CountriesList cl = new CountriesList();
	
	@Before 
	public void init() {
		user.setFirstName("Pepa");
		user.setLastName("Pig");
		user.setPassword("pepapig");
		user.setUsername("pepitapig");
		userService.save(user);			
	}
	
	@Test
	public void A_findAllUsers() {
		Pageable pageable = PageRequest.of(0, 10);
		Assert.assertNotNull(userService.findAllPageable(pageable));
	}
	@Test 
	public void B_findUserByFirstName() {
		Pageable pageable = PageRequest.of(0, 10);
		Assert.assertEquals(user,userService.findByFilterPageable(pageable,"Pepa").getContent().get(0));
	}
	@Test 
	public void C_findUserByLastName() {
		Pageable pageable = PageRequest.of(0, 10);
		Assert.assertEquals(user,userService.findByFilterPageable(pageable,"Pig").getContent().get(0));
	}
	@Test 
	public void D_findUserByUsername() {
		Pageable pageable = PageRequest.of(0, 10);
		Assert.assertEquals(user,userService.findByFilterPageable(pageable,"pepitapig").getContent().get(0));
	}
	
	@Test
	public void E_findAllUsers() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Assert.assertNotNull(userService.findAllPageable(pageable));
		
	}
	
	@Test
	public void F_findUserById() {
		User user2 = userService.findByUsername(user.getUsername());		
		Assert.assertEquals(user, userService.findById(user2.getId()));			
	}
	
	@Test
	public void G_NotFindUserById() { 
		int id = userService.findAll().size()+1;
		Assert.assertEquals(null, userService.findById(id));
	}

	@Test
	public void H_getCountriesListByDay() {
		Country country1 =countryService.findById(1);
		Country country9 = countryService.findById(35);
		Country country10 = countryService.findById(7);
		List<Country> countriesUser = new ArrayList <Country>();
		countriesUser.add(country1);
		countriesUser.add(country9);
		countriesUser.add(country10);
		cl.setCountries(countriesUser);
		cl.setCreationDate(new Date());
		cl.setName("Lista de Pepa Pig");
		cl.setUser(user);
		countriesListService.save(cl);
		user.addList(cl);
		userService.save(user);
		Assert.assertEquals(3, countriesListService.findAll().size());
        Date d = new Date();
        d.setDate(d.getDate() - 1); 
		Assert.assertEquals(3, countriesListService.findFilterByDate(d).size());
	}
	
	@Test
	public void J_getAllCountriesList() {
		Country country1 =countryService.findById(1);
		Country country9 = countryService.findById(35);
		Country country10 = countryService.findById(7);
		List<Country> countriesUser = new ArrayList <Country>();
		countriesUser.add(country1);
		countriesUser.add(country9);
		countriesUser.add(country10);
		cl.setCountries(countriesUser);
		cl.setCreationDate(new Date());
		cl.setName("Lista de Pepa Pig");
		cl.setUser(user);
		countriesListService.save(cl);
		user.addList(cl);
		Assert.assertEquals(3,countriesListService.findAll().size());
	}
	
	@Test
	public void K_getInterestedInACountry() throws Exception {
		Country country1 =countryService.findById(1);
		Country country9 = countryService.findById(35);
		Country country10 = countryService.findById(7);
		List<Country> countriesUser = new ArrayList <Country>();
		countriesUser.add(country1);
		countriesUser.add(country9);
		countriesUser.add(country10);
		cl.setCountries(countriesUser);
		cl.setCreationDate(new Date());
		cl.setName("Lista de Pepa Pig");
		cl.setUser(user);
		countriesListService.save(cl);
		user.addList(cl);
		Assert.assertEquals(2, countriesListService.getIntrested(7).size());
	}
	
	@Test
	public void L_cannotGetInterestedOnANotExistingCountry() throws Exception {
		try {
			countriesListService.getIntrested(800);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("El country id es inexistente"));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
