package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.UserService;

// asegurar que me mande los parametros y enviar error si no lo hace.

@RestController
@RequestMapping("/admin") // esta sera la raiz de la url, es decir http: //127.0.0.1:8080/adminWS/
public class AdminController {

    @Autowired
    private CountriesListService countriesListService;

    @Autowired
    private UserService userService;

    /**
     * @return Listado de todos los usuarios
     * example:  http://127.0.0.1:8080/admin/users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Consigna: Ver los datos de un usuario
     *
     * @return Datos de un usuario en particular
     * example:  http://127.0.0.1:8080/admin/users/3
     * @param: userId
     */
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable int userId) {
        return this.userService.findById(userId);
    }

    /**
     * Consigna 1: Devuelve un listado con todos los CountriesList para filtrar comparar en el frontend
     *
     * @return Todos los CountriesList filtrados por filterLast
     * example:  http://127.0.0.1:8080/admin/lists?filterLast=3
     * @param: filterLast
     */
    @SuppressWarnings("deprecation")
    @GetMapping("/countriesList")
    public List<CountriesList> getLastLists(@RequestParam(required = false, value = "filterLast") Integer days) {

        if (days != null) {
            Date d = new Date();
            d.setDate(d.getDate() - days);
            return countriesListService.findFilterByDate(d);
        } else {
            // Return all list
            return countriesListService.findAll();
        }

    }

    /**
     * Consigna: Ver interesados en un pais
     *
     * @return Lista de Usuarios interesados en el pais que corresponde al CountryId
     * example:  http://127.0.0.1:8080/admin/countries/3
     * @param: countryId
     */
    @GetMapping("/countries/{countryId}/users")
    public List<User> getInteresados(@PathVariable int countryId) {
        List<User> users = new ArrayList<User>();
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Name1");
        user1.setLastName("LastName1");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Name2");
        user2.setLastName("LastName2");

        Country country = new Country();
        country.setId(countryId);
        country.setName("Argentina");

        List<CountriesList> countriesList = new ArrayList<CountriesList>();
        CountriesList list = new CountriesList();
        list.setName("Lista1");
        List<Country> listOfCountries = new ArrayList<Country>();
        listOfCountries.add(country);
        list.setCountries(listOfCountries);
        countriesList.add(list);
        user1.setCountriesList(countriesList);

        List<CountriesList> countriesList2 = new ArrayList<CountriesList>();
        CountriesList list2 = new CountriesList();
        list2.setName("Lista2");
        List<Country> listOfCountries2 = new ArrayList<Country>();
        listOfCountries2.add(country);
        list2.setCountries(listOfCountries2);
        countriesList2.add(list2);
        user2.setCountriesList(countriesList2);

        users.add(user1);
        users.add(user2);
        return users;
    }

}
