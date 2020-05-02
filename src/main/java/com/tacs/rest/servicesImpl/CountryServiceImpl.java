package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cglib.core.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.Country;
import com.tacs.rest.services.CountryService;

@Service
@SuppressWarnings("unchecked")
public class CountryServiceImpl implements CountryService {

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
	public List<Country> findNearCountrys(String near) {
		// TODO Agregar la llamada a la base de datos : FILTRAR POR REGION Y SUBREGION

		// MOCK
		Set<Country> nearCountries = new HashSet<Country>();
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");
		Country nearCountry = null;
		for (Country country : listOfCountriesList) {
			if (country.getName().equals(near)) {
				nearCountry = country;
				break;
			}
		}

		// Busco los datos del pais del geolocalización
		for (Country obj : listOfCountriesList) {
			if (obj.getRegion().getSubRegion().equals(nearCountry.getRegion().getSubRegion())) {
				nearCountries.add(obj);
			}
		}

		for (Country obj : listOfCountriesList) {
			if (obj.getRegion().getNameRegion().equals(nearCountry.getRegion().getNameRegion())) {
				nearCountries.add(obj);
			}
		}
		List<Country> result = new ArrayList<Country>();
		result.addAll(nearCountries);
		return result;
	}
}
