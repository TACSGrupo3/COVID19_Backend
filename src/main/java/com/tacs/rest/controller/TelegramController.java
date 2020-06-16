package com.tacs.rest.controller;

import com.tacs.rest.services.TelegramService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@RestController
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

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

    @PostMapping("/telegram/new_message")
    public void new_message(@Validated @RequestBody Update update) throws IOException {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramService.messageHandling(update);
        }
    }

}
