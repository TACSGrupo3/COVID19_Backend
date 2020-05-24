package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import org.springframework.stereotype.Service;

@Service
public interface TelegramService {

    CountriesList countries_list(int user_id, int list_id);



}
