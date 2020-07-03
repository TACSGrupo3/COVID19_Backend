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
import com.tacs.rest.services.ReportService;
import com.tacs.rest.services.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional

public class ReportTest {
	
	@Autowired
	ReportService reportService;
	List<String> offset = new ArrayList<String>();
	List<Integer> countries = new ArrayList<Integer>();
	@Before
	public void init() {
		offset.add("01/05/2020");
		offset.add("09/12/2020");
		offset.add("07/04/2020");
		countries.add(1);
		countries.add(100);
		countries.add(34);
		
	}
	@Test
	public void A_getReports() throws Exception {
		
		Assert.assertNotNull(reportService.reportData(countries, offset));
	}
	@Test
	public void B_differentSizeListsCountriesAndOffsets() throws Exception {
		offset.add("07/06/2020");
		try {
			reportService.reportData(countries, offset);
		}
		catch(Exception e) {
			Assert.assertTrue(e.getMessage().contains("Debe ingresar un offset para cada pais"));
		}		
	}
	
	@Test
	public void C_invalidCountryInTheList() {
		offset.add("07/06/2020");
		countries.add(189);
		try {
			reportService.reportData(countries, offset);
		}
		catch(Exception e) {
			Assert.assertTrue(e.getMessage().contains("Ingresó un país inválido"));
		}	
	}
	@Test
	public void D_invalidFormatOffset() {
		offset.add("07/06/20");
		countries.add(19);
		try {
			reportService.reportData(countries, offset);
		}
		catch(Exception e) {
			Assert.assertTrue(e.getMessage().contains("El formato de las fechas debe ser dd/mm/aaaa"));
		}	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
