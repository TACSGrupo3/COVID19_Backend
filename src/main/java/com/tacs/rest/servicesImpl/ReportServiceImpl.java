package com.tacs.rest.servicesImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.dao.DataReportDAO;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	DataReportDAO drDAO;
	@Autowired
	CountryService countryServ;
	
	@Override
	public List<Country> reportData(List<Integer> countries, List<String> offset) throws Exception {
		
		List<Country>  countriesPedidos = countryServ.findCountriesByIds(countries);
		if(countriesPedidos.stream().anyMatch(c-> c== null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresó un país Inválido");
		}

		List<DataReport> dataToadd = new ArrayList<DataReport>();
		List<Country> countriesToShow = new ArrayList<Country>();
		
			for (int i = 0 ; i <countriesPedidos.size(); i ++) {
				
					Country country = countriesPedidos.get(i);
					List<DataReport> dataReprt = drDAO.findByCountry_idCountryOrderByDateAsc(country.getIdCountry());
					
					country.setDataReport(dataReprt);
					DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
					Date date;
					try {
						date = formatter.parse(offset.get(i));
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato de las fechas debe ser dd/mm/aaaa.");
					}
					
					dataToadd = country.getDataReport().stream().filter(item -> {
						return item.getDate().after(date) || item.getDate().getTime() == date.getTime();
					}).collect(Collectors.toList());
					
					country.setDataReport(dataToadd);
					countriesToShow.add(country);
			}
	
			return countriesToShow;	
	}
	
	@Override
	public void saveAll(List<DataReport> dataReports) {
		dataReports.forEach(dr->drDAO.save(dr));
	}
	@Override
	public void saveReportService(DataReport dataReport) {
		drDAO.save(dataReport);
		
	}
	
	@Override
	public void deleteAll() {
		drDAO.deleteAll();
	}
	@Override
	public List<DataReport> findByCountryId(int countryId) {
		return this.drDAO.findByCountry_idCountryOrderByDateDesc(countryId);
	}
	
	
	

}
