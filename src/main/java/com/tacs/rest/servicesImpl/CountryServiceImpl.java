package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tacs.rest.dao.CountryDAO;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryDAO daoCountry;

	@Override
	public List<Country> findAll() {
		return (List<Country>) daoCountry.findAll();

	}

	@Override
	public Country findById(int id) {
    	return daoCountry.findById(id).orElse(null);
	}
	
	@Override 
	public List<Country> findCountriesByIds(List<Integer> countriesIds){
		
		return countriesIds.stream().map(id->this.findById(id)).collect(Collectors.toList());
	}

	@Override
	public Country findByName(String countryName) {		
		return this.daoCountry.findByName(countryName.toUpperCase());
	}
	
	@Override
	public void saveAll(List<Country> countries) {
		countries.forEach(country -> this.save(country));	
	}
	
	@Override
	public List<DataReport> getReport (int id) {
		return this.findById(id).getDataReport();
	}
		
	public List<Country> findByIso(String iso) {
		List<Country> countriesIso2 = daoCountry.findBycountryCode_Iso2(iso);
		List<Country> countriesIso3 = daoCountry.findBycountryCode_Iso3(iso);
		
	   	if (!countriesIso2.isEmpty()) {
    		return this.agregarAListCountry(countriesIso2.get(0));
    	} else if (!daoCountry.findBycountryCode_Iso3(iso).isEmpty()) {
    		return this.agregarAListCountry(countriesIso3.get(0));
    	}
    	return null; 
	}

	public List<Country> agregarAListCountry(Country country){				
		List<Country> countries = new ArrayList<Country>() ;
		countries.add(country);
		return countries;
		
	}

	@Override
	public void save(Country country) {
		daoCountry.save(country);
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Country> findNearCountries(String latitud, String longitud, String maxCountries) {
		
		List<Country> nearCountries = new ArrayList<Country>();
		List<Country> listOfCountriesList = this.findAll();

		Collections.sort(listOfCountriesList, new Comparator<Country>() {
			@Override
			public int compare(Country p1, Country p2) {
				return p1.getDistance(latitud, longitud) < p2.getDistance(latitud, longitud) ? -1 : 1;
			}
		});

		for (Country country : listOfCountriesList) {
			nearCountries.add(country);

			if (nearCountries.size() == Integer.valueOf(maxCountries)) {
				return nearCountries;
			}
		}
		return nearCountries;
	}
	
	@Override
	public boolean existsCountries (List<Country> countries){
		
		int cantCountries = countries.size();
		
		for (int i = 0; i < (int)cantCountries; i ++) {
			if (this.findById(countries.get(i).getIdCountry())==null) {
				return false;
			}
		}
		return true;
		
	}
	
	@Override
	public boolean addSameCountries(List<Country> countries) {
		int cantCountries = countries.size();
        for (int i = 0; i < cantCountries; i++) {
            for (int j = i+1; j <cantCountries ; j++) {
                if(countries.get(i).getIdCountry()==countries.get(j).getIdCountry()){
                    return true;
                }
            }
        }
        return false;		
	}
	@Override
	public List<Country> searchAndSaveCountries(List<Country> countries){
		List<Country> countriesChequeados = new ArrayList<Country>();
		for(int i = 0; i < countries.size(); i ++) {
			Country country = daoCountry.findById(countries.get(i).getIdCountry()).get();
			countriesChequeados.add(country);
			this.save(countriesChequeados.get(i));

		}
		return countriesChequeados;

	}
	@Override
	public Country findByDataReportDate(Date date){
		List<Country> countries = daoCountry.findByDataReport_date(date);
		if(!countries.isEmpty()) 
			return countries.get(0);
		else
			return null;
	}

	
}
