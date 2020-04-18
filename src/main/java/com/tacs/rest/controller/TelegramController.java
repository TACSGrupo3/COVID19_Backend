package com.tacs.rest.controller;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;

@RestController
public class TelegramController {

    @GetMapping("/telegram/service/state")
    public String service_state() {

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + "<estado_servicio>");

    }

    @PostMapping("/telegram/service/state")
    public String service_state(@RequestParam boolean onoff) {
        String estado_servicio = "Apagado";

        if (onoff) {
            estado_servicio = "Prendido";
        } else {
            // Register our bot
            estado_servicio = "Apagado";
        }

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + estado_servicio);
    }


//    @GetMapping("/telegram/countries")
//    public String countries_list(@RequestParam CountriesList lista_paises, Country pais) {
//
//
//        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + "<estado_servicio>");
//
//    }

}
