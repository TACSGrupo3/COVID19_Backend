package com.tacs.rest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountriesListService;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.ReportService;
import com.tacs.rest.services.UserService;
import com.tacs.rest.util.IsoUtil;
import com.tacs.rest.util.ParseUtil;

@Component
public class DataBaseInitial implements ApplicationRunner {

    @Autowired
    CountryService countryService;
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @Autowired
    CountriesListService countriesListService;

    @Override
    public void run(ApplicationArguments args) throws Exception, IOException, URISyntaxException {

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("/data/data.json"));
        } catch (FileNotFoundException e) {
            try {
                obj = parser.parse(new FileReader("src/main/resources/data/data.json"));
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ConnectionApiCovid apiCovid = new ConnectionApiCovid();
            Gson gson = new Gson();

            java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>() {
            }.getType();
            Collection<Covid19_latestResponse> latestResponse;

            IsoUtil iu = new IsoUtil();
            iu.agregarPaises();

            JSONObject jsonObject = (JSONObject) obj;

            String uri = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/timeseries?OnlyCountries=true";

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            JSONArray json = (JSONArray) parser.parse(result);

            List<Country> countriesToSave = new ArrayList<Country>();
            List<DataReport> dataReportsToSave = new ArrayList<DataReport>();
            List<DataReport> dataReportOfCountry = new ArrayList<DataReport>();

            latestResponse = gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);

            for (Covid19_latestResponse response : latestResponse) {
                Country country = ParseUtil.latestResponseToCountry(response);

                for (int i = 0; i < json.size(); i++) {

                    JSONObject object = (JSONObject) json.get(i);
                    if (object.get("countryregion").toString().equals(country.getName())) {

                        JSONObject timeseries = (JSONObject) object.get("timeseries");
                        @SuppressWarnings("rawtypes")
                        Set setOfDates = timeseries.keySet();

                        for (Object objs : setOfDates) {
                            DataReport dataReport = new DataReport();
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                                Date date = sdf.parse((String) objs);
                                dataReport.setDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONObject data = (JSONObject) timeseries.get(objs);
                            Long recovered = (Long) data.get("recovered");
                            Long confirmed = (Long) data.get("confirmed");
                            Long deaths = (Long) data.get("deaths");

                            dataReport.setRecovered(recovered.intValue());
                            dataReport.setConfirmed(confirmed != null ? confirmed.intValue() : 0);
                            dataReport.setDeaths(deaths != null ? deaths.intValue() : 0);
                            dataReport.setCountry(country);
                            dataReportOfCountry.add(dataReport);

                            dataReportsToSave.add(dataReport);
                        }
                    }
                }
                countriesToSave.add(country);
            }
            countryService.saveAll(countriesToSave);

            JSONArray usersList = (JSONArray) jsonObject.get("users");
            List<Country> countriesBD = countryService.findAll();

            List<User> usersToSave = new ArrayList<User>();
            List<CountriesList> countriesListToSave = new ArrayList<CountriesList>();

            for (int i = 0; i < usersList.size(); i++) {

                JSONObject object = (JSONObject) usersList.get(i);

                User user = ParseUtil.parseJsonToUser(object, countriesBD);

                usersToSave.add(user);

                user.getCountriesList().forEach(c -> countriesListToSave.add(c));
                user.setCountriesList(new ArrayList<CountriesList>());
            }

            //userService.saveAll(usersToSave);
            //countriesListService.saveAll(countriesListToSave);
            reportService.saveAll(dataReportsToSave);
        } catch (ResourceAccessException ex) {
            System.out.println("-------------------------ERROR AL CONECTAR CON LA API COVID 19-------------------");
            System.out.println("-------------------------VOLVIENDO A CONECTAR EN 10 SEGUNDOS-----------------------");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            this.run(null);
        } catch (UnknownHostException ex) {
            System.out.println("-------------------------ERROR AL CONECTAR CON LA API COVID 19-------------------");
            System.out.println("-------------------------VOLVIENDO A CONECTAR EN 10 SEGUNDOS-----------------------");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String[] args1 = new String[0];
            RestApplication.main(args1);
        } catch (Exception e) {

            System.out.println("Error al levantar la aplicación. Error: ");
            e.printStackTrace();
        }

    }

}
