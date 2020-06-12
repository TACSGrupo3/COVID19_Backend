package com.tacs.rest.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.UserService;

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
    public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
    }

    /**
     * Consigna: Ver los datos de un usuario
     *
     * @return Datos de un usuario en particular
     * example:  http://127.0.0.1:8080/admin/users/3
     * @param: userId
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId) {
    	if(userService.findById(userId)!=null)
    		return new ResponseEntity<User>(userService.findById(userId), HttpStatus.OK);
    	else
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dicho user id es inexistente");
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
    public ResponseEntity<List<CountriesList>> getLastLists(@RequestParam(required = false, value = "filterLast") Integer days) {

        if (days != null) {
            Date d = new Date();
            d.setDate(d.getDate() - days);            
            return new ResponseEntity<List<CountriesList>>(countriesListService.findFilterByDate(d), HttpStatus.OK);
        } else {
            // Return all list
            return new ResponseEntity<List<CountriesList>>(countriesListService.findAll(), HttpStatus.OK);
        }

    }

    /**
     * Consigna: Ver interesados en un pais
     *
     * @return Lista de Usuarios interesados en el pais que corresponde al CountryId
     * example:  http://127.0.0.1:8080/admin/countries/3
     * @param: countryId
     * @throws Exception 
     */
    @GetMapping("/countries/{countryId}/users")
    public ResponseEntity<List<User>> getInteresados(@PathVariable int countryId) throws Exception {
    	if(countriesListService.getIntrested(countryId)!=null)
    		return new ResponseEntity<List<User>>(countriesListService.getIntrested(countryId), HttpStatus.OK);
    	else
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dicho country id es inexistente");
    }

}
