package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tacs.rest.dao.CountryDAO;
import com.tacs.rest.entity.Country;
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

	public List<Country> findByIso(String iso) {

    	long cantCountries = 0;
    	cantCountries = daoCountry.count();
    	for (int i = 0 ; i <(int) cantCountries; i ++) {
    		Country countryTabla = this.findById(i+1);
         	if(countryTabla.getCountryCode().getIso2().equals(iso) || countryTabla.getCountryCode().getIso3().equals(iso)) {
         		return this.agregarAListCountry(countryTabla);
         	}
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
	public boolean existsCountries (List<Country> countries) {
		
		int cantCountries = countries.size();
		
		for (int i = 0; i < (int)cantCountries; i ++) {
			if (this.findById(countries.get(i).getId())==null) {
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
                if(countries.get(i).getId()==countries.get(j).getId()){
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
			Country country = daoCountry.findById(countries.get(i).getId()).get();
			countriesChequeados.add(country);
			this.save(countriesChequeados.get(i));

		}
		return countriesChequeados;

	}

	
}
