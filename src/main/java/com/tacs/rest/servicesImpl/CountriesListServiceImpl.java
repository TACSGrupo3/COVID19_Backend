package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.dao.CountriesListDAO;
import com.tacs.rest.dao.UserDAO;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;

@Service
@SuppressWarnings("unchecked")
public class CountriesListServiceImpl implements CountriesListService {
	
	@Autowired
	CountriesListDAO countriesListDAO;
	@Autowired
	UserServiceImpl userServ;
	@Autowired
	CountryServiceImpl countryServ;
	
	@Override
	public List<CountriesList> findAll() {
		return (List<CountriesList>) countriesListDAO.findAll();
	}

	@Override
	public CountriesList findById(int id) {

		return countriesListDAO.findById(id).orElse(null);	
	}

	@Override
	public List<CountriesList> findFilterByDate(Date date) {
		List<CountriesList> filterByDate = new ArrayList<CountriesList>();
		long cantCountriesList = countriesListDAO.count();
		for (int i = 0; i < (int)cantCountriesList; i ++) {
			CountriesList elem = this.findById(i+1);
			if(elem.getCreationDate().getTime()>date.getTime()) {
				filterByDate.add(elem);
			}
		}
		return filterByDate;
	}

	@Override
	public List<CountriesList> addListCountries(String userId, List<CountriesList> countriesListToAdd)throws Exception {

		User user = userServ.findById(Integer.valueOf(userId));
		if(userServ.findById(Integer.valueOf(userId)) == null) {
			throw new Exception ("Este user id es inexistente");
		}
		
		for (CountriesList listToAdd : countriesListToAdd) {
			
			if (userServ.sameNameList(listToAdd.getName(),Integer.valueOf(userId))) {
				throw new Exception ("Este usuario ya posee una lista con igual nombre");
			}
			if (listToAdd.getName() == null || listToAdd.getName().equals("")) {
				throw new Exception("El nombre de la lista no puede estar vacío.");
			}
			CountriesList listToPersist = new CountriesList();
			
			listToPersist.setCreationDate(new Date());
			listToPersist.setName(listToAdd.getName());

			List <Country> countriesDelRequest = listToAdd.getCountries();
			
			
			if (countryServ.existsCountries(countriesDelRequest)==false) {
				throw new Exception("Ingresó un país Inválido");
			}
			if (countryServ.addSameCountries(countriesDelRequest)==true) {
				throw new Exception("Ingresó el mismo pais 2 veces en la misma lista");
			}
			List<Country> countries2 = countryServ.searchCountries(countriesDelRequest);
			
			for(int i = 0; i <countries2.size(); i ++) {
				//countries2.get(i).addCountriesList(listToPersist);
				countryServ.save(countries2.get(i));
			}
			
			listToPersist.setCountries(countries2);			
			listToPersist.setUser(user);
			user.addList(listToPersist);
			countriesListDAO.save(listToPersist);
			userServ.save(user);
			
		
		}
		return user.getCountriesList();

}		

	@Override
	public CountriesList modifyListCountries(int countryListId, CountriesList list) throws Exception {
		// TODO:Modifica la lista de countries en la base de datos

		// Mock
		if (list.getName() == null || list.getName().equals("")) {
			throw new Exception("El nombre de la lista no puede estar vacío.");
		}

		boolean exists = false;
		List<CountriesList> countriesList = (List<CountriesList>) RestApplication.data.get("CountriesList");
		List<User> users = (List<User>) RestApplication.data.get("Users");
		List<Country> countries = (List<Country>) RestApplication.data.get("Countries");

		for (int k = 0; k < users.size(); k++) {
			if (users.get(k).getCountriesList() != null) {
				for (int j = 0; j < users.get(k).getCountriesList().size(); j++) {
					if (users.get(k).getCountriesList().get(j).getId() == countryListId) {

						List<Country> countriesToPersist = new ArrayList<Country>();
						for (Country countryToPut : list.getCountries()) {
							// Valido que ingresó un país válido
							Boolean countryExists = false;
							for (Country countryBd : countries) {
								if (countryToPut.getId() == countryBd.getId()) {
									countriesToPersist.add(countryBd);
									countryExists = true;
									break;
								}
							}

							if (!countryExists) {
								throw new Exception("Ingresó un país Inválido");
							}
						}

						list.setId(users.get(k).getCountriesList().get(j).getId());
						list.setCreationDate(users.get(k).getCountriesList().get(j).getCreationDate());
						list.setCountries(countriesToPersist);
						users.get(k).getCountriesList().set(j, list);
						exists = true;
						break;
					}
				}
			}
		}

		if (exists) {

			for (int i = 0; i < countriesList.size(); i++) {
				if (countriesList.get(i).getId() == countryListId) {
					countriesList.set(i, list);
				}
			}

			RestApplication.data.put("CountriesList", countriesList);
			RestApplication.data.put("Users", users);
			return list;
		}

		return null;
	}

	@Override
	public List<CountriesList> findByUserId(int idUser) {		
		User user = userServ.findById(idUser);
		if(user == null) {
			return null;
		}
		return user.getCountriesList();
	}

	@Override
	public List<CountriesList> deleteListCountries(String countriesListId) throws Exception {
		
		CountriesList cl = countriesListDAO.findById(Integer.valueOf(countriesListId)).orElse(null);
		if(cl == null) {
			throw new Exception("El countries list id es inexistente");
		}
		User user = cl.getUser();
		user.removeList(cl);
		userServ.save(user);
		countriesListDAO.delete(cl);	
		return user.getCountriesList();
		
	}

	@Override
	public List<User> getIntrested(int countryId) {
		// TODO PASAR A BD

		List<User> users = (List<User>) RestApplication.data.get("Users");

		List<User> usersIntrested = new ArrayList<User>();

		for (User user : users) {
			if (user.getCountriesList() != null) {
				for (CountriesList countriesList : user.getCountriesList()) {
					if (countriesList.getCountries() != null) {
						for (Country country : countriesList.getCountries()) {
							if (country.getId() == countryId) {
								User userToAdd = new User(user.getId(), user.getUsername(), user.getFirstName(),
										user.getLastName(), user.getUserRole());
								usersIntrested.add(userToAdd);
							}
						}
					}
				}
			}
		}
		return usersIntrested;
	}

}
