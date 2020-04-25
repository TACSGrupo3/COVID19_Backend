package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountryService;

@Service
public class CountryServiceImpl implements CountryService{

//	@Autowired
//	private DaoCountry daoCountry;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Country> findAll() {
		// TODO Agregar la llamada a la bd 
		
		//MOCK
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");
		return listOfCountriesList;
		
	}

	@Override
	public Country findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Country country) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CountriesList> addListCountries(User user) {
		//TODO:Agregar la lista de countries en la base de datos
		
		//Mock
		List<CountriesList> listOfCountriesList = (List<CountriesList>) RestApplication.data.get("CountriesList");
		for(CountriesList countryList : user.getCountriesList()) {
			listOfCountriesList.add(countryList);
		}
		RestApplication.data.put("CountriesList",listOfCountriesList);
		
		return user.getCountriesList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CountriesList> modifyListCountries(User user) {
		//TODO:Modifica la lista de countries en la base de datos
		
		//Mock
		List<CountriesList> listOfCountriesList = (List<CountriesList>) RestApplication.data.get("CountriesList");
		for(CountriesList countryList : user.getCountriesList()) {
			boolean exists = false;
			for(int i = 0; i < listOfCountriesList.size() ; i++) {
				if(listOfCountriesList.get(i).getId() == countryList.getId()) {
					listOfCountriesList.set(i, countryList);
					exists = true;
				}
			}
			if(!exists) {
				listOfCountriesList.add(countryList);
			}
		}
		RestApplication.data.put("CountriesList",listOfCountriesList);
		
		return user.getCountriesList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> findNearCountrys(String near) {
		// TODO Agregar la llamada a la base de datos : FILTRAR POR REGION Y SUBREGION
		
		//MOCK
		List<Country> nearCountries = new ArrayList<Country>();
		List<Country> listOfCountriesList = (List<Country>) RestApplication.data.get("Countries");
		Country nearCountry = null;
		for(Country country : listOfCountriesList) {
			if(country.getName().equals(near)){
				nearCountry = country;
				break;
			}
		}
		
		//Busco los datos del pais del geolocalización
		for(Country obj : listOfCountriesList) {
			if(obj.getRegion().getSubRegion().equals(nearCountry.getRegion().getSubRegion())) {
				nearCountries.add(obj);
			}
		}
		
		for(Country obj : listOfCountriesList) {
			if(obj.getRegion().getNameRegion().equals(nearCountry.getRegion().getNameRegion())) {
				nearCountries.add(obj);
			}
		}
		
		return nearCountries;
	}
}
