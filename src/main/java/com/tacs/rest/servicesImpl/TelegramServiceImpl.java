package com.tacs.rest.servicesImpl;

import com.tacs.rest.controller.CountryRestController;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.Region;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.TelegramService;
import com.tacs.rest.telegram.Telegram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class TelegramServiceImpl implements TelegramService {

    @Autowired
    private CountryRestController countriesController;

    @Override
    public void initialization() {
        // Initialize Api Context
        ApiContextInitializer.init();
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        // Register our bot
        try {
            botsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<CountriesList> countriesList_list(User user) {
        return user.getCountriesList();
    }

    public CountriesList countries_list(String user_id, int list_id) {

        List<CountriesList> countries = countriesController.getCountriesListByUserId(user_id);
        CountriesList countryData = countries.stream().filter(list_countries -> list_countries.getId() == list_id).findAny().orElse(null);
        /*URL url = new URL("http://127.0.0.1:8080/api/countries");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        Region lat = new Region();
        lat.setId(1);
        lat.setNameRegion("LATINOAMERICA");
        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        argentina.setRegion(lat);
        Country brasil = new Country();
        brasil.setId(2);
        brasil.setName("Brasil");
        brasil.setRegion(lat);
        while ((output = br.readLine()) != null) {
            output = output.replace("[", "").replace("]", "");
            String[] split = output.split(",");
            List<String> list = Arrays.asList(split);
            //list.forEach(pais -> countries.add(new Country(2,"Argentina")));
            countries.add(argentina);
            countries.add(brasil);
        }
        conn.disconnect();*/
        return countryData;

    }

    public String new_country(int list_id, int pais_id) {

        List<Country> countries = new ArrayList<Country>();
        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        countries.add(argentina);

        return argentina.getName() + " se agrego a lista " + Integer.toString(list_id);

    }

    public CountriesList last_data(int list_id) {
        CountriesList reportePaises = null;

        return reportePaises;
    }

    public CountriesList compared_data(int list_id, int days) {

        // TODO: Generar logica que compare los paises de una lista para los ultimos x dias recibidos como parametro

        List<Country> lista_countries = new ArrayList<Country>();
        CountriesList countries = new CountriesList();
        countries.setCreationDate(new GregorianCalendar(2020, Calendar.APRIL, 1).getTime());
        countries.setCountries(lista_countries);
        countries.setId(1);
        countries.setName("Lista1");

        Region latinoamerica = new Region();
        latinoamerica.setId(1);
        latinoamerica.setNameRegion("LATINOAMERICA");

        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        argentina.setRegion(latinoamerica);

        Country brasil = new Country();
        brasil.setId(2);
        brasil.setName("Brasil");
        brasil.setRegion(latinoamerica);

        countries.addCountry(argentina);
        countries.addCountry(brasil);

        return countries;

    }

}
