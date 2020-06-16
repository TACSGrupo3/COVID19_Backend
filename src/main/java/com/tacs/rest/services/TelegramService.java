package com.tacs.rest.services;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
public interface TelegramService {

    void messageHandling(Update update) throws IOException;

    CountriesList countries_list(int user_id, int list_id);

    Country get_country_information(String name);

    void set_message_listCountries(Update update, String action, String messageString, SendMessage message);

    void send_to_telegram(Update update, String action_text, boolean alert, int cacheTime, SendMessage message) throws IOException;

}
