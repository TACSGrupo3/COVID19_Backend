package com.tacs.rest.telegram;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.TelegramService;
import com.tacs.rest.servicesImpl.TelegramServiceImpl;
import com.tacs.rest.servicesImpl.UserServiceImpl;
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

public class Telegram extends TelegramLongPollingBot {

    enum Stage {
        CHOOSE,
        WRITE,
        WRITE_COMPARE,
        SAVE,
    }

    Stage stage;
    String actualState = String.valueOf(Stage.CHOOSE);
    UserServiceImpl userService = new UserServiceImpl();
    TelegramService telegramService = new TelegramServiceImpl();
    SendMessage message = new SendMessage();


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
                    String list = update.getMessage().getText().substring(5);
                    CountriesList finalList = checkedUser.getCountriesList().stream().filter(countries -> countries.getName().equals(list)).findAny().orElse(null);
                    if (finalList != null) {
                        String countriesMessage;
                        countriesMessage = "Países:\n";

                        Iterator countryIterator = finalList.getCountries().iterator();

                        while (countryIterator.hasNext()) {
                            Country country = (Country) countryIterator.next();
                            countriesMessage = countriesMessage + country.getName() + "\n";
                        }

                        message.setChatId(chat_id).setText(countriesMessage);
                        message.setReplyMarkup(null);
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
                    }


                } else if (update.getMessage().getText().contains("/Mod")) {
                    {
                        switch (update.getMessage().getText()) {
                            case "/ModLista1":
                                stage = Stage.SAVE;
                                actualState = "/ModLista1" + stage;
                                message.setReplyMarkup(null);
                                message.setText("Ingrese el país que desea agregar a la lista");
                                break;

                            default:
                        }
                    }
                    if (update.getMessage().getText().contains("/Compare")) {

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
                        // Create a message object object
                        message.setChatId(chat_id)
                                .setText("Bienvenido al Bot del TP COVID19 - Grupo 3\n");

                        if (checkedUser == null) {
                            message.setChatId(chat_id)
                                    .setText("No posee usuario de Telegram configurado en la aplicacion TACS-Grupo3  ");
                        } else {
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

            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.getCallbackQuery() != null) {
            String messageString;
            int commandNumber = 0;
            message.setReplyMarkup(null);
            long chat_id = update.getCallbackQuery().getFrom().getId();
            User checkedUser = userService.findByTelegramId(chat_id);
            List<CountriesList> countryList = checkedUser.getCountriesList();
            Iterator iterator = countryList.iterator();
            switch (update.getCallbackQuery().getData()) {
                case "action1":
                    messageString = "Listas de países:\n";
                    while (iterator.hasNext()) {
                        commandNumber++;
                        CountriesList listName = (CountriesList) iterator.next();
                        messageString = messageString + " " + listName.getName() + " /ModLista" + String.valueOf(commandNumber) + "\n";
                    }
                    message.setChatId(chat_id).setText(messageString);
                    try {
                        execute(new AnswerCallbackQuery().setCallbackQueryId(update.getCallbackQuery().getId()).setText("Opción 1").setCacheTime(4)); // Sending our message object to user
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    stage = Stage.WRITE;
                    break;
                case "action2":
                    messageString = "Seleccione la lista de países que desea comparar:\n";
                    while (iterator.hasNext()) {
                        CountriesList listName = (CountriesList) iterator.next();
                        messageString = messageString + listName.getName() + " /Compare" + listName.getName() + "\n";
                    }
                    message.setChatId(chat_id).setText(messageString);
                    try {
                        execute(new AnswerCallbackQuery().setCallbackQueryId(update.getCallbackQuery().getId()).setText("Opción 2").setCacheTime(4)); // Sending our message object to user
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    stage = Stage.WRITE_COMPARE;
                    break;
                case "action3":
                    break;
                case "action4":
                    messageString = "Seleccione alguna de las siguientes listas de países:\n";
                    while (iterator.hasNext()) {
                        CountriesList listName = (CountriesList) iterator.next();
                        messageString = messageString + listName.getName() + " /view" + listName.getName() + "\n";
                    }
                    message.setChatId(chat_id).setText(messageString);
                    try {
                        execute(new AnswerCallbackQuery().setCallbackQueryId(update.getCallbackQuery().getId()).setText("Opción 4").setCacheTime(4)); // Sending our message object to user
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
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
        return "1060211355:AAFkQnG5BdTrlQtbmzhevheUCHTqkk4x1Io";
    }
}
