package com.tacs.rest.servicesImpl;

import com.tacs.rest.controller.CountryRestController;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramServiceImpl {

    @Autowired
    public CountriesListService countriesListService;


    public CountriesList countries_list(int user_id, int list_id) {

        return countriesListService.findByUserId(user_id).stream().filter(list_countries -> list_countries.getId() == list_id).findAny().orElse(null);

    }


}
