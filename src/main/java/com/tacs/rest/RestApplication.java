package com.tacs.rest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.util.IsoUtil;
import com.tacs.rest.util.ParseUtil;

@SpringBootApplication
public class RestApplication {
	
	public static HashMap<String,Object> data = new HashMap<String,Object>();

	
    public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
    	RestApplication.initData();
        SpringApplication.run(RestApplication.class, args);
        
        // todo: A realizar wrap de las proximas para Telegram
        //TELEGRAM BOT starts
        // Initialize Api Context
        ApiContextInitializer.init();
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        // Register our bot

        //No se habilita hasta tratar correctamente el Token de la API

        /*try {
            //botsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/

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
		}catch(FileNotFoundException e ) {
			try {
				obj = parser.parse(new FileReader("src/main/resources/data/data.json"));
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			} 
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		
		ConnectionApiCovid apiCovid = new ConnectionApiCovid();
		Gson gson = new Gson();
	
		java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>(){}.getType();
		Collection<Covid19_latestResponse> latestResponse;
		List<Country> listCountries = new ArrayList<Country>();
		
		IsoUtil iu = new IsoUtil();
		iu.agregarPaises();

		latestResponse= gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);
		
		int id = 1;
		for(Covid19_latestResponse response : latestResponse) {
			Country country = ParseUtil.latestResponseToCountry(response);
			country.setId(id);
			listCountries.add(country);	
			id++;
		}
		
		JSONObject jsonObject = (JSONObject) obj;
		
		JSONArray usersList = (JSONArray) jsonObject.get("users");
		List<User> listUsers = new ArrayList<User>();
		Iterator <JSONObject>iterator = usersList.iterator();
		while (iterator.hasNext()) {
			listUsers.add(ParseUtil.parseJsonToUser(iterator.next()));
		}
				
		JSONArray countriesList = (JSONArray) jsonObject.get("countriesList");
		List<CountriesList> listCountriesList = new ArrayList<CountriesList>();
		iterator = countriesList.iterator();
		while (iterator.hasNext()) {
			listCountriesList.add(ParseUtil.parseJsonToCountryList(iterator.next()));
		}
		
		
		//Persistencia de las Tablas en Memoria
		RestApplication.data.put("Countries", listCountries);
		RestApplication.data.put("Users", listUsers);
		RestApplication.data.put("CountriesList", listCountriesList);

		
    }
    
}
