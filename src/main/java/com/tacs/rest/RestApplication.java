package com.tacs.rest;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;
import com.tacs.rest.servicesImpl.TelegramServiceImpl;
import com.tacs.rest.util.IsoUtil;
import com.tacs.rest.util.ParseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RestApplication {

    public static HashMap<String, Object> data = new HashMap<String, Object>();


    public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
        RestApplication.initData();
        SpringApplication.run(RestApplication.class, args);
        //TelegramServiceImpl telegram = new TelegramServiceImpl();
        //telegram.initialization();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static void initData() throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
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
            // TODO Auto-generated catch block
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


            int id = 1;
            int idRegion = 1;
            for (Covid19_latestResponse response : latestResponse) {
                Country country = ParseUtil.latestResponseToCountry(response);
                country.setId(id);
                for (DataReport data : country.getDataReport()) {
                    data.setId(idRegion);
                    idRegion++;
                }
                listCountries.add(country);
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
            String[] args = new String[0];
            RestApplication.main(args);
        } catch (Exception e) {
            System.out.println("Error al levantar la aplicación. Error: ");
            e.printStackTrace();
        }

    }

}
