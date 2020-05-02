package com.tacs.rest.servicesImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.ReportService;

@SuppressWarnings("unchecked")
@Service
public class ReportServiceImpl implements ReportService{

	@Override
	public List<Country> reportData(List<String> countries, List<String> offset) {
		//llamar a la BD
		
		List<Country> countriesBd = (List<Country>) RestApplication.data.get("CountriesTimeSeries");
		
		List<Country> countriesList = new ArrayList<Country>();
		for(int i = 0; i < countries.size(); i++) {
			for(Country countryBd : countriesBd) {
				if(countryBd.getName().toLowerCase().equals(countries.get(i).toLowerCase())) {
					Collections.sort(countryBd.getDataReport());
					
					Country country = new Country();
					List<DataReport> dataToadd = new ArrayList<DataReport>();
					if(Integer.valueOf(offset.get(i)) != 0) {
						if(countryBd.getDataReport().size() > Integer.valueOf(offset.get(i))) {
							dataToadd = countryBd.getDataReport()
									.subList(Integer.valueOf(offset.get(i)), countryBd.getDataReport().size());
						}
					}
					country.setName(countryBd.getName());
					country.setDataReport(dataToadd);
					countriesList.add(country);
				}
			}
		}
		
		
		return countriesList;
	}

}
