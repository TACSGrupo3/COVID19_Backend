package com.tacs.rest;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tacs.rest.apiCovid.ConnectionApiCovid;
import com.tacs.rest.apiCovid.Covid19_latestResponse;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.ReportService;
import com.tacs.rest.services.UserService;
import com.tacs.rest.util.ParseUtil;

@Configuration
@EnableScheduling
public class DataBaseUpdate {

	@Autowired
	UserService userService;
	@Autowired
	CountryService countryService;
	@Autowired
	ReportService reportService;

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

	@Scheduled(fixedRate = 6000, initialDelay = 10000) // fixedRate= velocidad initalDelay= arranca a los 4 mins
	public void executeTask1() throws org.json.simple.parser.ParseException, JsonIOException, JsonSyntaxException,
			IOException, URISyntaxException, ParseException {

		try {
			// Hacer que se actualicen datos de latests
			JSONParser parser = new JSONParser();
			ConnectionApiCovid apiCovid = new ConnectionApiCovid();
			Gson gson = new Gson();

			java.lang.reflect.Type collectionType = new TypeToken<Collection<Covid19_latestResponse>>() {
			}.getType();
			Collection<Covid19_latestResponse> latestResponse;

			String uri = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/timeseries?OnlyCountries=true";

			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uri, String.class);
			JSONArray json = (JSONArray) parser.parse(result);

			List<Country> countriesToSave = new ArrayList<Country>();
			List<DataReport> dataReportsToSave = new ArrayList<DataReport>();
			List<DataReport> dataReportOfCountry = new ArrayList<DataReport>();

			latestResponse = gson.fromJson(apiCovid.connectionWithoutParams("latest"), collectionType);
			int k = 0;
			for (Covid19_latestResponse response : latestResponse) {
				Country country = ParseUtil.updateCountries(response, countryService); /// SE ACTUALIZA COUNTRY

				for (int i = 0; i < json.size(); i++) {

					JSONObject object = (JSONObject) json.get(i);
					if (object.get("countryregion").toString().equals(country.getName())) {

						JSONObject timeseries = (JSONObject) object.get("timeseries");
						@SuppressWarnings("rawtypes")
						Set setOfDates = timeseries.keySet();

						for (Object objs : setOfDates) {

							Date date = sdf.parse((String) objs);
							if (countryService.findByDataReportDate(date) == null) {
								DataReport dataReport = new DataReport();
								dataReport.setDate(date);
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
								k++;
							}
						}
					}
				}
				countriesToSave.add(country);
			}
			countryService.saveAll(countriesToSave); // se guardan los countries modificados
			reportService.saveAll(dataReportsToSave); // se agregan los nuevos dataReports

			System.out.println("La cantidad de veces modificados fueron: " + k);
			try {
				Thread.sleep(86400000); // espera 4mins para recargar
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			this.executeTask1();
		}
	}

}
