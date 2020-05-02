package com.tacs.rest.controller;

import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.entity.Region;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
public class TelegramController {

    @GetMapping("/telegram/service/state")
    public String service_state() {

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + "<estado_servicio>");

    }

    @PostMapping("/telegram/service/state")
    public String service_state(@RequestParam boolean onoff) {
        String estado_servicio;

        if (onoff) {
            estado_servicio = "Prendido";
        } else {
            // Register our bot
            estado_servicio = "Apagado";
        }

        return (String) JSONObject.stringToValue("Estado del bot de telegram: " + estado_servicio);
    }

    public List<Country> countries_list(@RequestParam int list_id) throws IOException {

        List<Country> countries = new ArrayList<Country>();

        URL url = new URL("http://127.0.0.1:8080/api/countries");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        Region lat = new Region();
        lat.setId(1);
        lat.setNameRegion("LATINOAMERICA");
        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        argentina.setRegion(lat);
        Country brasil = new Country();
        brasil.setId(2);
        brasil.setName("Brasil");
        brasil.setRegion(lat);
        while ((output = br.readLine()) != null) {
            output = output.replace("[", "").replace("]", "");
            String[] split = output.split(",");
            List<String> list = Arrays.asList(split);
            //list.forEach(pais -> countries.add(new Country(2,"Argentina")));
            countries.add(argentina);
            countries.add(brasil);
        }
        conn.disconnect();
        return countries;

    }

    public String new_country(@RequestParam int list_id, int pais_id) {

        List<Country> countries = new ArrayList<Country>();
        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        countries.add(argentina);

        return argentina.getName() + " se agrego a lista " + Integer.toString(list_id);

    }

    public List<DataReport> last_data(@RequestParam int list_id, int pais_id) throws IOException {
        List<DataReport> reportePaises = new ArrayList<>();

        if (list_id == 0 && pais_id > 0) {
            DataReport reporte = new DataReport();
            reporte.setId(1);
            reporte.setDate(new GregorianCalendar(2020, Calendar.APRIL, 1).getTime());
            reporte.setRecovered(100);
            reporte.setDeaths(30);
            reporte.setConfirmed(1000);
            reportePaises.add(reporte);
            
        } else if (list_id > 0 && pais_id == 0) {
            DataReport reporte1 = new DataReport();
            reporte1.setId(1);
            reporte1.setDate(new GregorianCalendar(2020, Calendar.APRIL, 1).getTime());
            reporte1.setRecovered(100);
            reporte1.setDeaths(30);
            reporte1.setConfirmed(1000);
            reportePaises.add(reporte1);
            DataReport reporte2 = new DataReport();
            reporte2.setId(2);
            reporte2.setDate(new GregorianCalendar(2020, Calendar.APRIL, 1).getTime());
            reporte2.setRecovered(1200);
            reporte2.setDeaths(20000);
            reporte2.setConfirmed(43000);
            reportePaises.add(reporte2);
        }
        return reportePaises;
    }

    public CountriesList compared_data(@RequestParam int list_id, int days) throws IOException {

        // TODO: Generar logica que compare los paises de una lista para los ultimos x dias recibidos como parametro

        List<Country> lista_countries = new ArrayList<Country>();
        CountriesList countries = new CountriesList();
        countries.setCreationDate(new GregorianCalendar(2020, Calendar.APRIL, 1).getTime());
        countries.setCountries(lista_countries);
        countries.setId(1);
        countries.setName("Lista1");

        Region latinoamerica = new Region();
        latinoamerica.setId(1);
        latinoamerica.setNameRegion("LATINOAMERICA");

        Country argentina = new Country();
        argentina.setId(1);
        argentina.setName("Argentina");
        argentina.setRegion(latinoamerica);

        Country brasil = new Country();
        brasil.setId(2);
        brasil.setName("Brasil");
        brasil.setRegion(latinoamerica);

        countries.addCountry(argentina);
        countries.addCountry(brasil);

        return countries;

    }


}
