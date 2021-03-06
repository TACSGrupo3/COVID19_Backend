package com.tacs.rest.servicesImpl;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramServiceImpl implements TelegramService {

    @Autowired
    private CountriesListService countriesListService;
    @Autowired
    private CountryService countryService;

    @Override
    public CountriesList countries_list(int user_id, int list_id) {

        return countriesListService.findByUserId(user_id).stream().filter(list_countries -> list_countries.getId() == list_id).findAny().orElse(null);

    }

    @Override
    public Country get_country_information(String iso) {

        return countryService.findByIso(iso).get(0);

    }

}
