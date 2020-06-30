package com.tacs.rest;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tacs.rest.dao.CountriesListDAO;
import com.tacs.rest.entity.CountriesList;
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
	public void test1() throws Exception {
		List<CountriesList> list = countriesListServiceImpl.findAll();
		System.out.println(list);
		Assert.assertNotNull(list);
	}
}
