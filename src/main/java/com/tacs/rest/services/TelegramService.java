package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import org.springframework.stereotype.Service;

@Service
public interface TelegramService {

    CountriesList countries_list(int user_id, int list_id);
    Country get_country_information(String name);


}
