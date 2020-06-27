package com.tacs.rest.util;

import java.util.ArrayList;
import java.util.List;

import com.tacs.rest.apiCovid.Countrycode;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.Country;
import com.tacs.rest.services.CountryService;

public class ParseUtil {


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
    

}
