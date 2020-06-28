package com.tacs.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.CountryService;

public class ParseUtil {

	static SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd"); 
	
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
    
    public static Country updateCountries (Covid19_latestResponse latestResponse, CountryService countryService) throws NullPointerException {
    	
    	Country country = countryService.findByName(latestResponse.getCountryregion());
    	country.setConfirmed(latestResponse.getConfirmed());
    	country.setDeaths(latestResponse.getDeaths());
    	country.setRecovered(latestResponse.getRecovered());
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
    public static Country parseJsonToCountry(JSONObject json) {
        Country country = new Country();
        country.setIdCountry(Integer.valueOf((String) json.get("id")));
        country.setName((String) json.get("name"));

        return country;
    }

    public static User parseJsonToUser(JSONObject json, List<Country> countriesBD) {
        User user = new User();
        user.setFirstName((String) json.get("firstName"));
        user.setLastName((String) json.get("lastName"));
        String username = (String) json.get("username");
        user.setUsername(username.toLowerCase());
        user.setPassword((String) json.get("password"));
        user.setUserRole((String) json.get("userRole"));
        if (json.get("telegram_chat_id") != null) {
            user.setTelegram_chat_id(Long.parseLong((String) json.get("telegram_chat_id")));
        }

        JSONArray jsonCountriesList = (JSONArray) json.get("countriesList");
        List<CountriesList> listOfCountriesList = new ArrayList<CountriesList>();
        if (jsonCountriesList != null) {
        	for (int i = 0; i < jsonCountriesList.size(); i++) {
            	CountriesList countriesList = parseJsonToCountryList((JSONObject) jsonCountriesList.get(i), countriesBD);
            	countriesList.setUser(user);
            	listOfCountriesList.add(countriesList);
            }
        }
        user.setCountriesList(listOfCountriesList);
        return user;
    }

	public static CountriesList parseJsonToCountryList(JSONObject json, List<Country> countriesBD) {
        CountriesList countriesList = new CountriesList();
        countriesList.setName((String) json.get("name"));
         
        try {
			countriesList.setCreationDate(json.get("creationDate") != null ? 
					formatter.parse((String) json.get("creationDate")) : null);
		} catch (ParseException e) {
			countriesList.setCreationDate(null);
		}

        JSONArray jsonCountries = (JSONArray) json.get("countries");

        List<Country> listOfCountry = new ArrayList<Country>();

        	for (int i = 0; i < jsonCountries.size(); i++) {
        		Country country = parseJsonToCountry((JSONObject) jsonCountries.get(i));
        		for (int j = 0; j < countriesBD.size(); j++) {
        			if (countriesBD.get(j).getIdCountry() == country.getIdCountry()) {
        				country = countriesBD.get(j);
        				listOfCountry.add(country);
        			}
        		}
        	}

        countriesList.setCountries(listOfCountry);
        return countriesList;
    }
	public static Country parseJsonToCountryTimeSeries(JSONObject object, CountryService cs) {

        Country country = cs.findByName(object.get("countryregion").toString());
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
