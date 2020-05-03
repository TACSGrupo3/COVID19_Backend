package com.tacs.rest.controller;

import com.tacs.rest.services.TelegramService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramController {

    @GetMapping("/telegram/service/state")
    public String service_state() {

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + "<estado_servicio>");

    }

    @PostMapping("/telegram/service/state")
    public String service_state(@RequestParam boolean onoff) {
        String estado_servicio;

        if (onoff) {
            estado_servicio = "Prendido";
        } else {
            // Register our bot
            estado_servicio = "Apagado";
        }

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + estado_servicio);
    }

}
