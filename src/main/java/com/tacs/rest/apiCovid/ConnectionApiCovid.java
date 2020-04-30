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
	
	
	public Reader connectionWithoutParams(String path) throws IOException {
		
		urlCon = urlCon.concat(path); 
		URL con = new URL(urlCon);
		URLConnection connection = con.openConnection();  
		connection.setDoOutput(true); 
		

		return new InputStreamReader(connection.getInputStream());
	
		
	}
	public Reader connectionWithParams(String path, String iso, Boolean onlyCountries) throws URISyntaxException, IOException {
		
		urlCon = urlCon.concat(path); 
		
	
		URI u = this.createURI(iso, onlyCountries);

		URL c = u.toURL();
		
		URLConnection connection = c.openConnection();
		connection.setDoOutput(true); 
		

		return new InputStreamReader(connection.getInputStream());
	
		
	}
	
	public URI createURI (String iso, Boolean onlyCountries) throws URISyntaxException {
		
		
		if(iso==null) {
			return new URIBuilder(urlCon).addParameter("onlyCountries", onlyCountries.toString()).build();
		}
		else if(onlyCountries == null) {
			return new URIBuilder(urlCon).addParameter(this.tipoIso(iso), iso).build();
		} else {
			return new URIBuilder(urlCon).addParameter(this.tipoIso(iso), iso).addParameter("onlyCountries", onlyCountries.toString()).build();
		}

	}
	
	
	public String tipoIso(String iso) {
		
		if(iso.length()==2) {
			return "iso2";
		}
		return "iso3";
		
	}
	
	
	
}
