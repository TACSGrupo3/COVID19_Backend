package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;

public interface TelegramService {

    public CountriesList countries_list(int user_id, int list_id);

}
