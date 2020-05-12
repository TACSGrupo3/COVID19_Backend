package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;

@Service
@SuppressWarnings("unchecked")
public class CountriesListServiceImpl implements CountriesListService {

	@Override
	public List<CountriesList> findAll() {
		// TODO: Agregar llamada a la BD

		// Mock
		List<User> users = (List<User>) RestApplication.data.get("Users");

		List<CountriesList> countriesList = new ArrayList<CountriesList>();
		for (User user : users) {
			if (user.getCountriesList() != null && !user.getCountriesList().isEmpty()) {
				countriesList.addAll(user.getCountriesList());
			}
		}
		return countriesList;
	}

	@Override
	public CountriesList findById(int id) {
		// TODO: Agregar llamada a la BD

		// Mock
		List<User> users = (List<User>) RestApplication.data.get("Users");

		for (User user : users) {
			if (user.getCountriesList() != null && !user.getCountriesList().isEmpty()) {
				for (CountriesList list : user.getCountriesList()) {
					if (list.getId() == id)
						return list;
				}
			}
		}
		return null;
	}

	@Override
	public List<CountriesList> findFilterByDate(Date date) {
		// TODO: Agregar llamada a la BD

		// Mock
		List<CountriesList> result = this.findAll();
		List<CountriesList> filteredList = new ArrayList<CountriesList>();
		for(CountriesList item : result) {
			if(item.getCreationDate().getTime() > date.getTime()) {
				filteredList.add(item);
			}
		}
		return filteredList;
	}

	@Override
	public List<CountriesList> addListCountries(String userId, List<CountriesList> countriesListToAdd) {
		// TODO:Agregar la lista de countries en la base de datos
		
		
		// Mock
		List<User> users = (List<User>) RestApplication.data.get("Users");
		List<CountriesList> countriesList = (List<CountriesList>) RestApplication.data.get("CountriesList");
		User user = new User(); 
		for (User userbd : users) {
			if (userbd.getId() == Integer.valueOf(userId)) {
				for (CountriesList listToPersist : countriesListToAdd) {
					listToPersist.setId(countriesList.size() + 1);
					listToPersist.setCreationDate(new Date());
					userbd.getCountriesList().add(listToPersist);
					countriesList.add(listToPersist);
				}
				user = userbd;
				break;
			}
		}

		RestApplication.data.put("CountriesList", countriesList);
		RestApplication.data.put("Users", users);

		return user.getCountriesList();
	}

	@Override
	public CountriesList modifyListCountries(int countryListId, CountriesList list) {
		// TODO:Modifica la lista de countries en la base de datos

		// Mock
		boolean exists = false;
		List<CountriesList> countriesList = (List<CountriesList>) RestApplication.data.get("CountriesList");
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for (int k = 0; k < users.size(); k++) {
			if (users.get(k).getCountriesList() != null) {
				for (int j = 0; j < users.get(k).getCountriesList().size(); j++) {
					if (users.get(k).getCountriesList().get(j).getId() == countryListId) {
						users.get(k).getCountriesList().set(j, list);
						exists = true;
						break;
					}
				}
			}
		}

		for (int i = 0; i < countriesList.size(); i++) {
			if (countriesList.get(i).getId() == countryListId) {
				countriesList.set(i, list);
			}
		}

		if (exists) {
			RestApplication.data.put("CountriesList", countriesList);
			RestApplication.data.put("Users", users);
			return list;
		}

		return null;
	}

	@Override
	public List<CountriesList> findByUserId(int id) {
		// TODO: Llamar a la BD
//		User user = daoUser.findById(id);

		// MOCK
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for (User user : users) {
			if (user.getId() == id) {
				return user.getCountriesList();
			}
		}
		return null;
	}

	@Override
	public void deleteListCountries(String countriesListId) {

		boolean exists = false;
		List<CountriesList> countriesListBd = (List<CountriesList>) RestApplication.data.get("CountriesList");
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for(User user: users) {
			if (user.getCountriesList() != null) {
				for (int j = 0; j < user.getCountriesList().size(); j++) {
					if (user.getCountriesList().get(j).getId() == Integer.valueOf(countriesListId)) {
						user.getCountriesList().remove(j);
						exists = true;
						break;
					}
				}
				
				if(exists) break;
			}
		}
		
		for(int i = 0; i < countriesListBd.size(); i++) {
			if(countriesListBd.get(i).getId() == Integer.valueOf(countriesListId)) {
				countriesListBd.remove(i);
				break;
			}
		}
		
		return;
	}

}
