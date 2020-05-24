package com.tacs.rest.servicesImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.ReportService;

@SuppressWarnings("unchecked")
@Service
public class ReportServiceImpl implements ReportService {

	@Override
	public List<Country> reportData(List<Integer> countries, List<String> offset) throws Exception {
		// llamar a la BD

		List<Country> countriesBdTimeSeries = (List<Country>) RestApplication.data.get("CountriesTimeSeries");
		List<Country> countriesBd = (List<Country>) RestApplication.data.get("Countries");

		List<Country> countriesList = new ArrayList<Country>();
		for (int i = 0; i < countries.size(); i++) {
			Country country = new Country();

			boolean exists = false;
			for (Country countryOfBd : countriesBd) {
				if (countryOfBd.getId() == countries.get(i)) {
					country.setId(countryOfBd.getId());
					country.setConfirmed(countryOfBd.getConfirmed());
					country.setDeaths(countryOfBd.getDeaths());
					country.setRecovered(countryOfBd.getRecovered());
					country.setName(countryOfBd.getName());
					country.setLocation(countryOfBd.getLocation());
					country.setLastupdate(countryOfBd.getLastupdate());
					country.setCountryCode(countryOfBd.getCountryCode());
					
					exists = true;
				}
			}
			
			if(!exists) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresó un país Inválido");
			}

			List<DataReport> dataToadd = new ArrayList<DataReport>();
			for (Country countryBd : countriesBdTimeSeries) {
				if (countryBd.getName().toLowerCase().equals(country.getName().toLowerCase())) {
					Collections.sort(countryBd.getDataReport());
					DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
					Date date;
					try {
						date = formatter.parse(offset.get(i));
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato de las fechas debe ser dd/mm/aaaa.");
					}
					
					dataToadd = countryBd.getDataReport().stream().filter(item -> {
						return item.getDate().after(date) || item.getDate().equals(date);
					}).collect(Collectors.toList());
				}
			}

			country.setDataReport(dataToadd);
			countriesList.add(country);
		}

		return countriesList;
	}

}
