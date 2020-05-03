package com.tacs.rest.apiCovid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.utils.URIBuilder;

public class ConnectionApiCovid {

    String urlCon = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/";


    public Reader connectionWithoutParams(String path) throws IOException, URISyntaxException {

        URL con;
        urlCon = urlCon.concat(path);
        if (path == "brief") {
            con = new URL(urlCon);
        } else {
            URI u = new URIBuilder(urlCon).addParameter("onlyCountries", "true").build();
            con = u.toURL();
        }

        URLConnection connection = con.openConnection();
        connection.setDoOutput(true);
        urlCon = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/";

        return new InputStreamReader(connection.getInputStream());


    }

    public Reader connectionWithParams(String path, String iso) throws URISyntaxException, IOException {

        urlCon = urlCon.concat(path);


        URI u = new URIBuilder(urlCon).addParameter(this.tipoIso(iso), iso).build();

        URL c = u.toURL();

        URLConnection connection = c.openConnection();
        connection.setDoOutput(true);

        return new InputStreamReader(connection.getInputStream());

    }


    public String tipoIso(String iso) {

        if (iso.length() == 2) {
            return "iso2";
        }
        return "iso3";

    }


}
