package com.tacs.rest.servicesImpl;

import com.neovisionaries.i18n.CountryCode;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.TelegramService;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TelegramServiceImpl implements TelegramService {

    enum Stage {
        CHOOSE,
        WRITE,
        WRITE_COUNTRY,
        WRITE_COMPARE,
        SAVE,
    }

    TelegramServiceImpl.Stage stage;
    String actualState = String.valueOf(TelegramServiceImpl.Stage.CHOOSE);
    private final SendMessage message = new SendMessage();

    @Autowired
    private CountriesListService countriesListService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public CountriesList countries_list(int user_id, int list_id) {
        return countriesListService.findByUserId(user_id).stream().filter(list_countries -> list_countries.getId() == list_id).findAny().orElse(null);
    }

    @Override
    public Country get_country_information(String iso) {
        return countryService.findByIso(iso).get(0);
    }

    @Override
    public void set_message_listCountries(Update update, String action, String messageString, SendMessage message) {
        // Show user's lists of countries
        long chat_id = update.getCallbackQuery().getFrom().getId();
        Iterator iterator = userService.findByTelegramId(chat_id).getCountriesList().iterator();
        while (iterator.hasNext()) {
            CountriesList listName = (CountriesList) iterator.next();
            messageString = messageString + listName.getName() + action + listName.getName().replaceAll("\\s+", "_") + "\n";
        }
        message.setChatId(chat_id).setText(messageString);
    }

    @Override
    public void send_to_telegram(Update update, String action_text, boolean alert, int cacheTime, SendMessage message) throws IOException {

        HttpPost post = new HttpPost("https://api.telegram.org/bot1060211355:AAG80evMXs9DovRrOrYkPT2kCqPoqoiv3Sw/sendMessage");
        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("chat_id", message.getChatId()));
        urlParameters.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(update.getMessage().getMessageId())));
        if (alert) {
            urlParameters.add(new BasicNameValuePair("text", "Hello"));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
            }
        }

        urlParameters.add(new BasicNameValuePair("text", message.getText()));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
        }
    }

    @Override
    public void messageHandling(Update update) throws IOException {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline5 = new ArrayList<>();
            long chat_id = update.getMessage().getChatId();
            // Check user info
            User checkedUser = userService.findByTelegramId(chat_id);

            if (update.getMessage().getText().contains("/") && !update.getMessage().getText().equals("/start")) {
                if (update.getMessage().getText().contains("/view")) {
                    // View countries list
                    // Get string ahead from position 5
                    String list = update.getMessage().getText().substring(5).replaceAll("_", " ");
                    // Get countries list from list of lists
                    CountriesList finalList = checkedUser.getCountriesList().stream().filter(countries -> countries.getName().equals(list)).findAny().orElse(null);
                    if (finalList != null) {
                        String countriesMessage = "Países:\n";
                        Iterator<Country> countryIterator = finalList.getCountries().iterator();
                        // Set String to show on Telegram chat
                        while (countryIterator.hasNext()) {
                            Country country = countryIterator.next();
                            countriesMessage = countriesMessage + country.getName() + "\n";
                        }
                        // Set chat id and String to send
                        message.setChatId(chat_id).setText(countriesMessage);
                        // Clear buttons
                        message.setReplyMarkup(null);
                        // Set next step
                        stage = TelegramServiceImpl.Stage.CHOOSE;
                        actualState = String.valueOf(TelegramServiceImpl.Stage.CHOOSE);
                    }
                } else if (update.getMessage().getText().contains("/Mod")) {
                    // Add new country to list
                    switch (update.getMessage().getText()) {
                        case "/ModLista1":
                            stage = TelegramServiceImpl.Stage.SAVE;
                            actualState = "/ModLista1" + stage;
                            message.setReplyMarkup(null);
                            message.setText("Ingrese el país que desea agregar a la lista");
                            break;

                        default:
                    }
                } else if (update.getMessage().getText().contains("/Compare")) {
                    // Show compare table of list of countries
                } else if (update.getMessage().getText().contains("/values")) {
                    // Show lasts values of COVID-19 belong to the countries of the list
                    // Get string ahead from position 7
                    String lists = update.getMessage().getText().substring(7).replaceAll("_", " ");
                    // Get countries list from list of lists
                    CountriesList finalLists = checkedUser.getCountriesList().stream().filter(countries -> countries.getName().equals(lists)).findAny().orElse(null);
                    if (finalLists != null) {
                        message.setChatId(chat_id);
                        message.setReplyMarkup(null);
                        // Set next step
                        stage = TelegramServiceImpl.Stage.CHOOSE;
                        actualState = String.valueOf(TelegramServiceImpl.Stage.CHOOSE);
                        // Set HTML message to show on telegram chat
                        message.setParseMode("HTML");
                        // Get COVID-19 information
                        CountriesList countriesListData = this.countries_list(checkedUser.getId(), finalLists.getId());
                        Iterator countryDataIterator = countriesListData.getCountries().iterator();

                        String htmlMessage = "\\------------------------------------------------\n" +
                                "Pais               Infectados   Curados   Muertos\n" +
                                "------------------------------------------------\\\n";
                        while (countryDataIterator.hasNext()) {
                            Country country = (Country) countryDataIterator.next();
                            int offset = 19 - country.getName().length();
                            int offset2 = 13 - country.getConfirmed().toString().length();
                            int offset3 = 10 - country.getRecovered().toString().length();
                            htmlMessage += country.getName() + (IntStream.range(0, offset).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    country.getConfirmed() + (IntStream.range(0, offset2).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    country.getRecovered() + (IntStream.range(0, offset3).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    country.getDeaths() + " \n";
                        }

                        message.setText(htmlMessage);
                    }
                }

            } else {
                switch (actualState) {
                    case "/ModLista1SAVE":
                        message.setReplyMarkup(null);
                        message.setText("Se ha agregado el pais a la lista");
                        stage = TelegramServiceImpl.Stage.CHOOSE;
                        actualState = String.valueOf(TelegramServiceImpl.Stage.CHOOSE);
                        break;
                    case "CHOOSE":
                        // Welcome message
                        message.setChatId(chat_id)
                                .setText("Bienvenido al Bot del TP COVID19 - Grupo 3\n");

                        if (checkedUser == null) {
                            // No Telegram ID for user on app
                            message.setChatId(chat_id)
                                    .setText("No posee usuario de Telegram configurado en la aplicacion TACS-Grupo3  ");
                        } else {
                            // User found, show options
                            rowInline1.add(new InlineKeyboardButton().setText("Agregar un pais a lista").setCallbackData("action1"));
                            rowsInline.add(rowInline1);
                            rowInline2.add(new InlineKeyboardButton().setText("Consultar tabla de comparacion").setCallbackData("action2"));
                            rowsInline.add(rowInline2);
                            rowInline3.add(new InlineKeyboardButton().setText("Consultar ultimos valores por lista").setCallbackData("action3"));
                            rowsInline.add(rowInline3);
                            rowInline4.add(new InlineKeyboardButton().setText("Consultar ultimos valores por pais").setCallbackData("action4"));
                            rowsInline.add(rowInline4);
                            rowInline5.add(new InlineKeyboardButton().setText("Revisar los paises de una lista").setCallbackData("action5"));
                            rowsInline.add(rowInline5);
                            // Add it to the message
                            markupInline.setKeyboard(rowsInline);
                            message.setReplyMarkup(markupInline);
                        }
                        break;
                    case "WRITE_COUNTRY":
                        // Set next step
                        stage = TelegramServiceImpl.Stage.CHOOSE;
                        actualState = String.valueOf(TelegramServiceImpl.Stage.CHOOSE);
                        String country = update.getMessage().getText();
                        country.toUpperCase();
                        List<CountryCode> isoInformation = CountryCode.findByName(country);
                        if (!isoInformation.isEmpty()) {
                            Country pais = this.get_country_information(isoInformation.get(0).getAlpha2());
                            message.setChatId(chat_id);
                            message.setReplyMarkup(null);

                            // Set HTML message to show on telegram chat
                            message.setParseMode("HTML");
                            // Get COVID-19 information
                            String htmlMessage = "\\-------------------------------------------\n" +
                                    "Pais               Infectados   Curados   Muertos\n" +
                                    "-------------------------------------------\\\n";
                            int offset = 19 - pais.getName().length();
                            int offset2 = 13 - pais.getConfirmed().toString().length();
                            int offset3 = 10 - pais.getRecovered().toString().length();
                            htmlMessage += pais.getName() + (IntStream.range(0, offset).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    pais.getConfirmed() + (IntStream.range(0, offset2).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    pais.getRecovered() + (IntStream.range(0, offset3).mapToObj(i -> " ").collect(Collectors.joining(""))) +
                                    pais.getDeaths() + " \n";

                            message.setText(htmlMessage);
                        } else {
                            message.setText("No se ha encontrado el pais");
                        }
                        break;
                    default:
                }
            }

            this.send_to_telegram(update, "", false, 0, message);

        } else if (update.getCallbackQuery() != null) {
            // Get buttons replies
            message.setReplyMarkup(null);
            long chat_id = update.getCallbackQuery().getFrom().getId();
            User checkedUser = userService.findByTelegramId(chat_id);
            List<CountriesList> countryList = checkedUser.getCountriesList();
            Iterator iterator = countryList.iterator();
            switch (update.getCallbackQuery().getData()) {
                case "action1":
                    this.set_message_listCountries(update, " /ModLista", "Listas de países:\n", message);
                    this.send_to_telegram(update, "Opción 1", true, 4, message);
                    stage = TelegramServiceImpl.Stage.WRITE;
                    break;
                case "action2":
                    this.set_message_listCountries(update, " /Compare", "Seleccione la lista de países que desea comparar:\n", message);
                    this.send_to_telegram(update, "Opción 2", true, 4, message);
                    stage = TelegramServiceImpl.Stage.WRITE_COMPARE;
                    break;
                case "action3":
                    this.set_message_listCountries(update, " /values", "Seleccione la lista de países de la que desea consultar los ultimos valores:\n", message);
                    this.send_to_telegram(update, "Opción 3", true, 4, message);
                    stage = TelegramServiceImpl.Stage.WRITE;
                    break;
                case "action4":
                    message.setChatId(chat_id).setText("Ingrese el país del que desea consultar los ultimos valores:\n");
                    this.send_to_telegram(update, "Opción 4", true, 4, message);
                    stage = TelegramServiceImpl.Stage.WRITE_COUNTRY;
                    actualState = String.valueOf(TelegramServiceImpl.Stage.WRITE_COUNTRY);
                    break;
                case "action5":
                    this.set_message_listCountries(update, " /view", "Seleccione alguna de las siguientes listas de países:\n", message);
                    this.send_to_telegram(update, "Opción 5", true, 4, message);
                    stage = TelegramServiceImpl.Stage.WRITE;
                    break;
                default:
            }
        }
    }

}
