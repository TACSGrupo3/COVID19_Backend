package com.tacs.rest;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.util.ParseUtil;

@SpringBootApplication
public class RestApplication {
	
	public static HashMap<String,Object> data = new HashMap<String,Object>();
	
    public static void main(String[] args) {
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
    public static void initData() {
    	JSONParser parser = new JSONParser();
    	
    	try {
			Object obj = parser.parse(new FileReader("src/main/resources/data/data.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray companyList = (JSONArray) jsonObject.get("countries");
			
			List<Country> listCountries = new ArrayList<Country>();
			Iterator<JSONObject> iterator = companyList.iterator();
			while (iterator.hasNext()) {
				listCountries.add(ParseUtil.parseJsonToCountry(iterator.next()));
			}
			
			
			JSONArray usersList = (JSONArray) jsonObject.get("users");
			List<User> listUsers = new ArrayList<User>();
			iterator = usersList.iterator();
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
