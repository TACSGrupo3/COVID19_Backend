package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.Country;
import com.tacs.rest.services.CountryService;

@Service
@SuppressWarnings("unchecked")
public class CountryServiceImpl implements CountryService {

	private final double RANGO_CERCANIA = 20; 
//	@Autowired
//	private DaoCountry daoCountry;

	@Override
	public List<Country> findAll() {
		// TODO Agregar la llamada a la bd

		// MOCK
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");
		return listOfCountriesList;

	}

	@Override
	public Country findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Country> findByIso(String iso) {
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");		
		return listOfCountriesList.stream().filter(country->(country.getCountryCode().getIso2().equals(iso)||
															country.getCountryCode().getIso3().equals(iso))).
														collect(Collectors.toList());	
	}
	
	@Override
	public void save(Country country) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Country> findNearCountrys(String latitud, String longitud, String maxCountries) {
		// TODO Agregar la llamada a la base de datos : FILTRAR POR REGION Y SUBREGION

		// MOCK
		List<Country> nearCountries = new ArrayList<Country>();
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");
		for (Country country : listOfCountriesList) {
			if (country.getLocation().getLat() < (Double.valueOf(latitud) + RANGO_CERCANIA) && 
					country.getLocation().getLat() > (Double.valueOf(latitud) - RANGO_CERCANIA) &&
						country.getLocation().getLng() < (Double.valueOf(longitud) + RANGO_CERCANIA) && 
							country.getLocation().getLng() > (Double.valueOf(longitud) - RANGO_CERCANIA)) {
				nearCountries.add(country);
				
				if(nearCountries.size() == Integer.valueOf(maxCountries)) {
					break;
				}
			}
		}

		return nearCountries;
	}
}
