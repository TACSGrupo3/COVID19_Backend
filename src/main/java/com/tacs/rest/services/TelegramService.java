package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TelegramService {

    void initialization();

    public List<CountriesList> countriesList_list(User user);

    public CountriesList countries_list(String user_id, int list_id);

    public String new_country(int list_id, int pais_id);

    public CountriesList last_data(int list_id);

    public CountriesList compared_data(int list_id, int days);


}
