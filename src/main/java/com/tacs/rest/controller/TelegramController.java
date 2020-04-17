package com.tacs.rest.controller;

import com.tacs.rest.entity.Telegram;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
public class TelegramController {

    @GetMapping("/telegram/service/state")
    public String service_state() {

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + "<estado_servicio>");

    }

    @PostMapping("/telegram/service/state")
    public String service_state(@RequestParam boolean onoff) {
        String estado_servicio = "Apagado";

        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        if (onoff) {
            // Register our bot
            try {
                botsApi.registerBot(new Telegram());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            estado_servicio = "Prendido";
        } else {
            // Register our bot
            estado_servicio = "Apagado";
        }


        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + estado_servicio);
    }

}
