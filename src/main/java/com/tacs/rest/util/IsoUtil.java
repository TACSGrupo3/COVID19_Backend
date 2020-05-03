package com.tacs.rest.util;

import java.util.HashMap;

import com.tacs.rest.RestApplication;

public class IsoUtil {
    public static HashMap<String, String> CountriesWithoutIso = new HashMap<String, String>();


    public void agregarPaises() {
        CountriesWithoutIso.put("Bahamas", "BMH");
        CountriesWithoutIso.put("Cabo Verde", "CVE");
        CountriesWithoutIso.put("Congo (Brazzaville)", "CBO");
        CountriesWithoutIso.put("Congo (Kinshasa)", "CKO");
        CountriesWithoutIso.put("Czechia", "CZE");
        CountriesWithoutIso.put("Eswatini", "EWS");
        CountriesWithoutIso.put("Gambia", "GMA");
        CountriesWithoutIso.put("Holy See", "HSE");
        CountriesWithoutIso.put("West Bank and Gaza", "WBG");
        CountriesWithoutIso.put("Kosovo", "KSV");
        CountriesWithoutIso.put("Burma", "BMA");
        CountriesWithoutIso.put("MS Zaandam", "MSZ");
        CountriesWithoutIso.put("Taiwan*", "TWT");
    }

    public String getIso3(String country) {
        return IsoUtil.CountriesWithoutIso.get(country);
    }

    public String getIso2(String country) {
        String iso3 = this.getIso3(country);
        StringBuilder nuevoIsoModificable = new StringBuilder(iso3);
        nuevoIsoModificable.setLength(iso3.length() - 1);
        return nuevoIsoModificable.toString();
    }

}
