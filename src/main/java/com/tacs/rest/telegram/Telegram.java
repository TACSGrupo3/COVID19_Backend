package com.tacs.rest.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Telegram extends TelegramLongPollingBot {
    static int contador = 0;

    public void addContador() {
        contador++;
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     *
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            switch (contador) {
                case 0:
                    // Create a message object object
                    message.setChatId(chat_id)
                            .setText("Bienvenido al Bot del TP COVID19 - Grupo 3\nIngrese su usuario");
                    contador++;
                    break;
                case 1:
                    // Create a message object object
                    message.setChatId(chat_id)
                            .setText("Ingrese su password");
                    break;
                case 2:
                    //
                    break;

                default:
                    message.setChatId(chat_id)
                            .setText(message_text);
            }

/*            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText("1 - Consultar por lista o País ").setCallbackData("update_msg_text"));
            rowInline.add(new InlineKeyboardButton().setText("1 - Comparar paises de una lista ").setCallbackData("update_msg_text"));
            rowInline.add(new InlineKeyboardButton().setText("1 - Agregar País a lista").setCallbackData("update_msg_text"));
            rowInline.add(new InlineKeyboardButton().setText("1 - Consultar lista ").setCallbackData("update_msg_text"));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            // Add it to the message
            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);*/
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return bot username of this bot
     */
    @Override
    public String getBotUsername() {
        return "botcovid19bot";
    }

    @Override
    public String getBotToken() {
        //No se envia Token hasta tratarlo correctamente
        return "";
    }
}
