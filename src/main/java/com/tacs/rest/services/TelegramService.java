package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;

import java.util.List;

public interface TelegramService {

    void initialization();

    public List<CountriesList> countriesList_list(User user);

    public List<Country> countries_list(int list_id);

    public String new_country(int list_id, int pais_id);

    public List<DataReport> last_data(int list_id, int pais_id);

    public CountriesList compared_data(int list_id, int days);


}
