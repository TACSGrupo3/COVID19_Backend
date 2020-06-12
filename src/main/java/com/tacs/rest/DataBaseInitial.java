package com.tacs.rest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.dao.CountryDAO;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.util.IsoUtil;
import com.tacs.rest.util.ParseUtil;

@Component
public class DataBaseInitial implements ApplicationRunner{
	
	@Autowired
	CountryDAO cs;
	
	@Override
	public void run(ApplicationArguments args) throws Exception,IOException, URISyntaxException {
		
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("/data/data.json"));
        } catch (FileNotFoundException e) {
            try {
                obj = parser.parse(new FileReader("src/main/resources/data/data.json"));
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


		ConnectionApiCovid apiCovid = new ConnectionApiCovid();
        Gson gson = new Gson();

        java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>() {
        }.getType();
        Collection<Covid19_latestResponse> latestResponse;
        List<Country> listCountries = new ArrayList<Country>();

        IsoUtil iu = new IsoUtil();
        iu.agregarPaises();

        JSONObject jsonObject = (JSONObject) obj;
        Iterator<JSONObject> iterator;
         
        try {
            latestResponse = gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);

            int id =  1;
            for (Covid19_latestResponse response : latestResponse) {
                Country country = ParseUtil.latestResponseToCountry(response);
                country.setId(id);
                listCountries.add(country);
                                
                cs.save(country);
                id++;
            }
            
            RestApplication.data.put("Countries", listCountries);
         

            JSONArray countriesList = (JSONArray) jsonObject.get("countriesList");
            List<CountriesList> listCountriesList = new ArrayList<CountriesList>();
            iterator = countriesList.iterator();
            while (iterator.hasNext()) {
                listCountriesList.add(ParseUtil.parseJsonToCountryList(iterator.next()));
            }
            RestApplication.data.put("CountriesList", listCountriesList);

            JSONArray usersList = (JSONArray) jsonObject.get("users");
            List<User> listUsers = new ArrayList<User>();
            iterator = usersList.iterator();
            while (iterator.hasNext()) {
                listUsers.add(ParseUtil.parseJsonToUser(iterator.next()));
            }

            RestApplication.data.put("Users", listUsers);

            String uri = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/timeseries";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            JSONArray json = (JSONArray) parser.parse(result);

            List<Country> countriesTimeSeries = new ArrayList<Country>();
            for (int i = 0; i < json.size(); i++) {
                JSONObject object = (JSONObject) json.get(i);
                countriesTimeSeries.add(ParseUtil.parseJsonToCountryTimeSeries(object));
            }

            RestApplication.data.put("CountriesTimeSeries", countriesTimeSeries);

        } catch (UnknownHostException ex) {
            System.out.println("-------------------------ERROR AL CONECTAR CON LA API COVID 19-------------------");
            System.out.println("-------------------------VOLVIENDO A CONECTAR EN 10 SEGUNDOS-----------------------");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String[] args1 = new String[0];
            RestApplication.main(args1);
        } catch (Exception e) {
            System.out.println("Error al levantar la aplicación. Error: ");
            e.printStackTrace();
        }
		
	}

}
