package com.tacs.rest.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @PostMapping("/Telegram/new-message")
    public void new_message(@RequestParam Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage message = new SendMessage();


        }
    }

}
