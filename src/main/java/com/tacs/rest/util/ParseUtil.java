package com.tacs.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tacs.rest.RestApplication;
import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;

public class ParseUtil {

    public static Country parseJsonToCountry(JSONObject json) {
        Country country = new Country();
        country.setId(Integer.valueOf((String) json.get("id")));
        country.setName((String) json.get("name"));
        return country;
    }

    public static User parseJsonToUser(JSONObject json) {
        User user = new User();
        user.setId(Integer.valueOf((String) json.get("id")));
        user.setFirstName((String) json.get("firstName"));
        user.setLastName((String) json.get("lastName"));
        user.setUsername((String) json.get("username"));
        user.setPassword((String) json.get("password"));
        JSONArray jsonCountriesList = (JSONArray) json.get("countriesList");

        if (jsonCountriesList != null) {
            List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();

            for (int i = 0; i < jsonCountriesList.size(); i++) {
                listOfCountriesList.add(parseJsonToCountryList((JSONObject) jsonCountriesList.get(i)));
            }
            user.setCountriesList(listOfCountriesList);
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    public static CountriesList parseJsonToCountryList(JSONObject json) {
        CountriesList countriesList = new CountriesList();

        countriesList.setId(Integer.valueOf((String) json.get("id")));
        countriesList.setName((String) json.get("name"));
        countriesList.setCreationDate(new Date());

        JSONArray jsonCountries = (JSONArray) json.get("countries");
        List<Country> countriesOfDb = (List<Country>) RestApplication.data.get("Countries");
        List<Country> listOfCountry = new ArrayList<Country>();
        for (int i = 0; i < jsonCountries.size(); i++) {
            Country country = parseJsonToCountry((JSONObject) jsonCountries.get(i));
            for (int j = 0; j < countriesOfDb.size(); j++) {
                if (countriesOfDb.get(j).getId() == country.getId()) {
                    country = countriesOfDb.get(j);
                    listOfCountry.add(country);
                    break;
                }
            }
        }

        countriesList.setCountries(listOfCountry);
        return countriesList;
    }

    public List<String> quitarComasQueryParams(String iso) {
        String pais = "";
        List<String> paises = new ArrayList<String>();

        for (int i = 0; i < iso.length(); i++) {

            if (iso.charAt(i) != ',') {
                pais = pais.concat(String.valueOf(iso.charAt(i)));
                if (i + 1 < iso.length()) {
                    if (iso.charAt(i + 1) == ',') {
                        paises.add(pais);
                        pais = "";
                    }
                }
            }

        }
        paises.add(pais);
        return paises;

    }

    public static Country latestResponseToCountry(Covid19_latestResponse latestResponse) throws NullPointerException {

        IsoUtil iu = new IsoUtil();
        Country country = new Country();
        country.setName(latestResponse.getCountryregion());
        country.setConfirmed(latestResponse.getConfirmed());
        country.setDeaths(latestResponse.getDeaths());
        country.setRecovered(latestResponse.getRecovered());
        country.setLocation(latestResponse.getLocation());
        country.setCountryCode(latestResponse.getCountrycode());

        if (latestResponse.getCountrycode() == null) {

            Countrycode countryCode = new Countrycode();
            countryCode.setIso3(iu.getIso3(country.getName()));
            countryCode.setIso2(iu.getIso2(country.getName()));
            country.setCountryCode(countryCode);

        } else {

            country.setCountryCode(latestResponse.getCountrycode());
        }

        country.setLastupdate(latestResponse.getLastupdate());
        return country;

    }

    public String quitarUltimaLetra(String palabra) {
        String palabraSinUltimaLetra = "";
        for (int i = 0; i < palabra.length() - 1; i++) {
            palabraSinUltimaLetra = palabraSinUltimaLetra.concat(String.valueOf(palabra.charAt(i)));
        }
        return palabraSinUltimaLetra;
    }

    public static Country parseJsonToCountryTimeSeries(JSONObject object) {

        Country country = new Country();
        country.setName(object.get("countryregion").toString());
        JSONObject timeseries = (JSONObject) object.get("timeseries");
        @SuppressWarnings("rawtypes")
        Set setOfDates = timeseries.keySet();

        List<DataReport> dataReportOfCountry = new ArrayList<DataReport>();
        for (Object obj : setOfDates) {
            DataReport dataReport = new DataReport();
            try {
                dataReport.setDate(new SimpleDateFormat("dd/MM/yy").parse((String) obj));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONObject data = (JSONObject) timeseries.get(obj);
            Long recovered = (Long) data.get("recovered");
            Long confirmed = (Long) data.get("confirmed");
            Long deaths = (Long) data.get("deaths");

            dataReport.setRecovered(recovered.intValue());
            dataReport.setConfirmed(confirmed != null ? confirmed.intValue() : 0);
            dataReport.setDeaths(deaths != null ? deaths.intValue() : 0);

            dataReportOfCountry.add(dataReport);
        }

        country.setDataReport(dataReportOfCountry);

        return country;
    }


}
