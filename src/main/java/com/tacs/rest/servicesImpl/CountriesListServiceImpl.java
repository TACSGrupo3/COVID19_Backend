package com.tacs.rest.servicesImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.dao.CountriesListDAO;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;

@Service
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
    	return countriesListDAO.findByFilterDate(date);
    }

    @Override
    public List<CountriesList> addListCountries(String userId, List<CountriesList> countriesListToAdd) throws Exception {

        User user = userServ.findById(Integer.valueOf(userId));

        for (CountriesList listToAdd : countriesListToAdd) {

            this.validacionNombresListas(userId, listToAdd);
            CountriesList listToPersist = new CountriesList();

            listToPersist.setCreationDate(new Date());
            listToPersist.setName(listToAdd.getName());

            List<Country> countriesDelRequest = listToAdd.getCountries();


            this.validacionPaises(countriesDelRequest);
            List<Country> countries2 = countryServ.searchAndSaveCountries(countriesDelRequest);

            listToPersist.setCountries(countries2);
            listToPersist.setUser(user);
            user.addList(listToPersist);
            countriesListDAO.save(listToPersist);
            userServ.save(user);

        }
        return user.getCountriesList();
    }


    public void validacionPaises(List<Country> countriesDelRequest) throws Exception {
        if (!countryServ.existsCountries(countriesDelRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresó un país Inválido");
        }
        if (countryServ.addSameCountries(countriesDelRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresó el mismo pais 2 veces en la misma lista");
        }
    }

    public void validacionNombresListas(String userId, CountriesList listToAdd) throws Exception {

        if (userServ.sameNameList(listToAdd.getName(), Integer.valueOf(userId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este usuario ya posee una lista con igual nombre");
        }
        if (listToAdd.getName() == null || listToAdd.getName().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista no puede estar vacío.");
        }
    }

    @Override
    public CountriesList modifyListCountries(int countryListId, CountriesList list) throws Exception {

        if (list.getName() == null || list.getName().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista no puede estar vacío.");
        }

        validacionPaises(list.getCountries());
        CountriesList countriesList = countriesListDAO.findById(countryListId).orElse(null);
        if (countriesList == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del Country List es inexistente");
        }

        countriesList.setName(list.getName());

        List<Country> countriesDelRequest = list.getCountries();

        this.validacionPaises(countriesDelRequest);

        User user = userServ.userWithCountriesList(countryListId);

        List<Country> countries2 = countryServ.searchAndSaveCountries(countriesDelRequest);

        countriesList.setCountries(countries2);
        user.modifyList(countriesList);
        countriesListDAO.save(countriesList);
        userServ.save(user);
        return countriesList;

    }

    @Override
    public List<CountriesList> findByUserId(int idUser) {
        if (userServ.findById(idUser) == null) {
            return null;
        }
        return userServ.findById(idUser).getCountriesList();
    }

    @Override
    public List<CountriesList> deleteListCountries(String countriesListId) throws Exception {

        CountriesList cl = countriesListDAO.findById(Integer.valueOf(countriesListId)).orElse(null);
        if (cl == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El country list id es inexistente");
        }
        User user = cl.getUser();
        user.removeList(cl);
        userServ.save(user);
        countriesListDAO.delete(cl);
        return user.getCountriesList();

    }

    @Override
    public List<User> getIntrested(int idCountry) throws Exception {

        if (countryServ.findById(idCountry) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El country id es inexistente");
        }
        return userServ.userInterestedOnCountry(idCountry);
    }

    @Override
    public void save(CountriesList countryList) {
        countriesListDAO.save(countryList);
    }

    @Override
    public void saveAll(List<CountriesList> countriesList) {
        if (countriesList != null)
            countriesList.stream().forEach(cl -> this.save(cl));
    }

    @Override
    public List<CountriesList> findByName(String name) {
        return this.countriesListDAO.findByName(name.toUpperCase());
    }

}
