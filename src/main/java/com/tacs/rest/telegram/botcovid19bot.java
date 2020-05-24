package com.tacs.rest.telegram;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.servicesImpl.TelegramServiceImpl;
import com.tacs.rest.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class botcovid19bot extends TelegramLongPollingBot {

    enum Stage {
        CHOOSE,
        WRITE,
        WRITE_COMPARE,
        SAVE,
    }

    Stage stage;
    String actualState = String.valueOf(Stage.CHOOSE);
    UserServiceImpl userService = new UserServiceImpl();
    @Autowired
    TelegramServiceImpl telegramService;

    private SendMessage message = new SendMessage();

    /**
     * This method is called when receiving updates via GetUpdates method
     */
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
            List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
            long chat_id = update.getMessage().getChatId();
            // Check user info
            User checkedUser = userService.findByTelegramId(chat_id);

            if (update.getMessage().getText().contains("/") && !update.getMessage().getText().equals("/start")) {
                if (update.getMessage().getText().contains("/view")) {
                    // View countries list
                    // Get string ahead from position 5
                    String list = update.getMessage().getText().substring(5);
                    // Get countries list from list of lists
                    CountriesList finalList = checkedUser.getCountriesList().stream().filter(countries -> countries.getName().equals(list)).findAny().orElse(null);
                    if (finalList != null) {
                        String countriesMessage = "Países:\n";
                        Iterator<Country> countryIterator = finalList.getCountries().iterator();
                        // Set String to show on Telegram chat
                        while (countryIterator.hasNext()) {
                            Country country = (Country) countryIterator.next();
                            countriesMessage = countriesMessage + country.getName() + "\n";
                        }
                        // Set chat id and String to send
                        message.setChatId(chat_id).setText(countriesMessage);
                        // Clear buttons
                        message.setReplyMarkup(null);
                        // Set next step
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
                    }
                } else if (update.getMessage().getText().contains("/Mod")) {
                    // Add new country to list
                    switch (update.getMessage().getText()) {
                        case "/ModLista1":
                            stage = Stage.SAVE;
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
                    String lists = update.getMessage().getText().substring(7);
                    // Get countries list from list of lists
                    CountriesList finalLists = checkedUser.getCountriesList().stream().filter(countries -> countries.getName().equals(lists)).findAny().orElse(null);
                    if (finalLists != null) {
                        message.setChatId(chat_id);
                        message.setReplyMarkup(null);
                        // Set next step
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
                        // Set HTML message to show on telegram chat
                        message.setParseMode("HTML");
                        // Get COVID-19 information
                        CountriesList countriesListData = telegramService.countries_list(checkedUser.getId(), finalLists.getId());
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
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
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
                            rowInline3.add(new InlineKeyboardButton().setText("Consultar ultimos valores por lista o pais").setCallbackData("action3"));
                            rowsInline.add(rowInline3);
                            rowInline4.add(new InlineKeyboardButton().setText("Revisar los paises de una lista").setCallbackData("action4"));
                            rowsInline.add(rowInline4);
                            // Add it to the message
                            markupInline.setKeyboard(rowsInline);
                            message.setReplyMarkup(markupInline);
                        }
                        break;
                    default:
                }
            }

            send_to_telegram(update, "", false, 0);

        } else if (update.getCallbackQuery() != null) {
            // Get buttons replies
            message.setReplyMarkup(null);
            long chat_id = update.getCallbackQuery().getFrom().getId();
            User checkedUser = userService.findByTelegramId(chat_id);
            List<CountriesList> countryList = checkedUser.getCountriesList();
            Iterator iterator = countryList.iterator();
            switch (update.getCallbackQuery().getData()) {
                case "action1":
                    set_message_listCountries(update, " /ModLista", "Listas de países:\n");
                    send_to_telegram(update, "Opción 1", true, 4);
                    stage = Stage.WRITE;
                    break;
                case "action2":
                    set_message_listCountries(update, " /Compare", "Seleccione la lista de países que desea comparar:\n");
                    send_to_telegram(update, "Opción 2", true, 4);
                    stage = Stage.WRITE_COMPARE;
                    break;
                case "action3":
                    set_message_listCountries(update, " /values", "Seleccione la lista de países de la que desea consultar los ultimos valores:\n");
                    send_to_telegram(update, "Opción 3", true, 4);
                    stage = Stage.WRITE;
                    break;
                case "action4":
                    set_message_listCountries(update, " /view", "Seleccione alguna de las siguientes listas de países:\n");
                    send_to_telegram(update, "Opción 4", true, 4);
                    stage = Stage.WRITE;
                    break;
                default:
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "botcovid19bot";
    }

    @Override
    public String getBotToken() {
        //No se envia Token hasta tratarlo correctamente
        return "";
    }

    private void set_message_listCountries(Update update, String action, String messageString) {
        // Show user's lists of countries
        long chat_id = update.getCallbackQuery().getFrom().getId();
        Iterator iterator = userService.findByTelegramId(chat_id).getCountriesList().iterator();
        while (iterator.hasNext()) {
            CountriesList listName = (CountriesList) iterator.next();
            messageString = messageString + listName.getName() + action + listName.getName() + "\n";
        }
        message.setChatId(chat_id).setText(messageString);
    }

    private void send_to_telegram(Update update, String action_text, boolean alert, int cacheTime) {
        try {
            if (alert)
                execute(new AnswerCallbackQuery().setCallbackQueryId(update.getCallbackQuery().getId()).setText(action_text).setCacheTime(cacheTime)); // Sending our message object to user
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
