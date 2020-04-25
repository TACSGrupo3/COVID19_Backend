package com.tacs.rest;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

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
    
    public static void initData() {
    	
    }
}
