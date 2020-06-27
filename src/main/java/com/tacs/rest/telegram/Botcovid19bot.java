package com.tacs.rest.telegram;

import com.neovisionaries.i18n.CountryCode;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;
import com.tacs.rest.servicesImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Botcovid19bot extends TelegramLongPollingBot {

    enum Stage {
        CHOOSE,
        WRITE,
        WRITE_COUNTRY,
        WRITE_COMPARE,
        SAVE,
        COMPARE
    }

    Stage stage;
    String actualState = String.valueOf(Stage.CHOOSE);
    String listToModify = "";
    String listToCompare = "";
    @Autowired
    UserServiceImpl userService = new UserServiceImpl();
    @Autowired
    TelegramServiceImpl telegramService;
    @Autowired
    CountriesListServiceImpl countriesListSerivce = new CountriesListServiceImpl();
    @Autowired
    CountryServiceImpl countryService = new CountryServiceImpl();
    @Autowired
    ReportServiceImpl reportService = new ReportServiceImpl();

    private final SendMessage message = new SendMessage();

    static {
        /** Telegram BOT API init */
        ApiContextInitializer.init();
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     */
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

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
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
                    }
                } else if (update.getMessage().getText().contains("/Mod")) {
                    // Add new country to list
                    this.listToModify = update.getMessage().getText().substring(9).replaceAll("_", " ");
                    stage = Stage.SAVE;
                    actualState = "/ModList" + stage;
                    message.setReplyMarkup(null);
                    message.setText("Ingrese el país que desea agregar a la lista");
                } else if (update.getMessage().getText().contains("/Compare")) {
                    // Show compare table of list of countries
                    this.listToCompare = update.getMessage().getText().substring(8).replaceAll("_", " ");
                    stage = Stage.COMPARE;
                    actualState = "/List" + stage;
                    message.setReplyMarkup(null);
                    message.setText("Ingrese la cantidad de días a comparar");
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
                    case "/ModListSAVE":
                        String countryToAdd = update.getMessage().getText();

                        Country countryBd = this.countryService.findByName(countryToAdd);
                        if (countryBd == null) {
                            message.setText("El país elegido no existe, por favor escoja otro país.");
                            break;
                        }
                        CountriesList countriesList = findCountriesList(update, this.listToModify);
                        boolean exists = false;
                        for (Country country : countriesList.getCountries()) {
                            if (country.getName().toUpperCase().equals(countryBd.getName().toUpperCase()))
                                exists = true;
                        }
                        if (exists) {
                            message.setText("El país elegido ya existe en la lista elegida.");
                            break;
                        }
                        countriesList.getCountries().add(countryBd);

                        this.listToModify = "";
                        try {
                            this.countriesListSerivce.modifyListCountries(countriesList.getId(), countriesList);
                            message.setReplyMarkup(null);
                            message.setText("Se ha agregado el pais a la lista");
                            stage = Stage.CHOOSE;
                            actualState = String.valueOf(Stage.CHOOSE);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            message.setText("Ocurrió un error al agregar el país a la lista.");
                        }

                    case "CHOOSE":
                        // Welcome message
                        message.setChatId(chat_id)
                                .setText("Bienvenido al Bot del TP COVID19 - Grupo 3\n");

                        if (checkedUser == null) {
                            // No Telegram ID for user on app
                            message.setReplyMarkup(null);
                            message.setChatId(chat_id)
                                    .setText("No posee usuario de Telegram configurado en la aplicacion TACS-Grupo3. "
                                            + "Para utilizar las funciones de Telegram: \n"
                                            + "1. Dirigite a la aplicación https://covid19tacsgrupo3.azurewebsites.net/. \n"
                                            + "2. Crea una cuenta o inicia sesión en tu cuenta. \n"
                                            + "3. Dirigete a la opción 'Datos Personales'. \n"
                                            + "4. Ingresa tu Telegram Id: " + chat_id);
                        } else {
                            // User found, show options
                            message.setReplyMarkup(loadOptions());
                        }
                        break;
                    case "WRITE_COUNTRY":
                        // Set next step
                        stage = Stage.CHOOSE;
                        actualState = String.valueOf(Stage.CHOOSE);
                        String country = update.getMessage().getText();
                        country.toUpperCase();
                        List<CountryCode> isoInformation = CountryCode.findByName(country);
                        if (!isoInformation.isEmpty()) {
                            Country pais = telegramService.get_country_information(isoInformation.get(0).getAlpha2());
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
                    case "/ListCOMPARE":
                        createTable(update);
                        break;
                    default:
                }
            }

            if (actualState != "/ListCOMPARE") {
                send_to_telegram(update, "", false, 0);
            }
        } else if (update.getCallbackQuery() != null) {
            // Get buttons replies
            message.setReplyMarkup(null);
            long chat_id = update.getCallbackQuery().getFrom().getId();
//            User checkedUser = userService.findByTelegramId(chat_id);
//            List<CountriesList> countryList = countriesListSerivce.findByUserId(checkedUser.getId());
//            Iterator iterator = countryList.iterator();
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
                    message.setChatId(chat_id).setText("Ingrese el país del que desea consultar los ultimos valores:\n");
                    send_to_telegram(update, "Opción 4", true, 4);
                    stage = Stage.WRITE_COUNTRY;
                    actualState = String.valueOf(Stage.WRITE_COUNTRY);
                    break;
                case "action5":
                    set_message_listCountries(update, " /view", "Seleccione alguna de las siguientes listas de países:\n");
                    send_to_telegram(update, "Opción 5", true, 4);
                    stage = Stage.WRITE;
                    break;
                default:
            }
        }
    }

    private InlineKeyboardMarkup loadOptions() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();

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

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private void createTable(Update update) {
        try {
            CountriesList countriesList = findCountriesList(update, this.listToCompare);
            String cantDaysString = update.getMessage().getText();
            int cantDays = 0;
            try {
                cantDays = Integer.valueOf(cantDaysString);
            } catch (Exception e) {
                if (cantDaysString.equals("/start")) {
                    message.setText("Bienvenido al Bot del TP COVID19 - Grupo 3\\n");
                    message.setReplyMarkup(loadOptions());
                } else
                    message.setText("Ingresó un número incorrecto.");

                return;
            }
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception useDefault) {
            }

            int cantColumns = countriesList.getCountries().size() * 3 + 1;
            int cantRows = cantDays + 2;
            Object[][] data = new Object[cantRows][cantColumns];
            String[] columns = new String[cantColumns];

            data[0][0] = "";
            columns[0] = "";
            for (int i = 0; i < countriesList.getCountries().size(); i++) {
                int pos = i * 3;
                data[0][pos + 1] = "";
                data[0][pos + 2] = countriesList.getCountries().get(i).getName();
                data[0][pos + 3] = "";
                data[1][pos + 1] = "Confirmados";
                data[1][pos + 2] = "Muertos";
                data[1][pos + 3] = "Recuperados";
                columns[pos + 1] = "";
                columns[pos + 2] = countriesList.getCountries().get(i).getName();
                columns[pos + 3] = "";
            }


            for (int j = 0; j < cantDays; j++) {
                for (int k = 0; k < countriesList.getCountries().size(); k++) {
                    List<DataReport> dataReport = this.reportService.findByCountryId(countriesList.getCountries().get(k).getIdCountry());
                    int pos = k * 3;
                    if (dataReport != null && dataReport.size() >= j && dataReport.get(j) != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        data[j + 2][0] = sdf.format(dataReport.get(j).getDate());

                        data[j + 2][pos + 1] = dataReport.get(j).getConfirmed();
                        data[j + 2][pos + 2] = dataReport.get(j).getDeaths();
                        data[j + 2][pos + 3] = dataReport.get(j).getRecovered();
                    } else {
                        data[j + 2][0] = "Sin Datos";
                        data[j + 2][pos + 1] = "Sin Datos";
                        data[j + 2][pos + 2] = "Sin Datos";
                        data[j + 2][pos + 3] = "Sin Datos";
                    }
                }
            }
            JTable table = new JTable(data, columns);
            JScrollPane scroll = new JScrollPane(table);

            table.getColumnModel().getColumn(1).setWidth(80);
            table.setSize(cantColumns * 80, cantRows * 20);
            JPanel p = new JPanel(new BorderLayout());
            p.add(scroll, BorderLayout.CENTER);

            JTableHeader h = table.getTableHeader();
            Dimension dH = h.getSize();
            Dimension dT = table.getSize();
            int x = (int) dT.getWidth();
            int y = (int) dT.getHeight();

            scroll.setDoubleBuffered(false);

            BufferedImage bi = new BufferedImage(
                    (int) x,
                    (int) y,
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D g = bi.createGraphics();
            h.paint(g);
            table.paint(g);
            g.dispose();

//	        JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(bi)));
//	        ImageIO.write(bi,"png",new File("table.png"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            SendPhoto message = new SendPhoto().setPhoto("SomeText", is);
            Long chatId = update.getMessage().getChatId();
            message.setChatId(chatId);
            this.execute(message);
            this.message.setText("Ingrese una nueva cantidad de días para comparar nuevamente");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "botcovid19bot";
    }

    @Override
    public String getBotToken() {
        //No se envia Token hasta tratarlo correctamente
        return "1060211355:AAG80evMXs9DovRrOrYkPT2kCqPoqoiv3Sw";
    }

    private CountriesList findCountriesList(Update update, String countriesList) {
        long chat_id = update.getMessage().getChatId();
//    	long chat_id = update.getCallbackQuery().getFrom().getId();
        List<CountriesList> list = userService.findByTelegramId(chat_id).getCountriesList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().toUpperCase().equals(countriesList.toUpperCase())) {
                return list.get(i);
            }
        }
        return null;
    }

    @Transactional
    void set_message_listCountries(Update update, String action, String messageString) {
        // Show user's lists of countries
        long chat_id = update.getCallbackQuery().getFrom().getId();
        User user = userService.findByTelegramId(chat_id);
        Iterator iterator = user.getCountriesList().iterator();
        while (iterator.hasNext()) {
            CountriesList listName = (CountriesList) iterator.next();
            messageString = messageString + listName.getName() + action + listName.getName().replaceAll("\\s+", "_") + "\n";
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
