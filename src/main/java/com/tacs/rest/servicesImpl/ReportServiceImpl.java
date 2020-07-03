package com.tacs.rest.servicesImpl;

import com.google.common.collect.Streams;
import com.tacs.rest.dao.DataReportDAO;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.DataReport;
import com.tacs.rest.services.CountryService;
import com.tacs.rest.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    DataReportDAO drDAO;
    @Autowired
    CountryService countryServ;

    @Override
    public List<Country> reportData(List<Integer> countries, List<String> offsets) {

        List<Country> countriesPedidos = countryServ.findCountriesByIds(countries);
        
        if(countries.size()!=offsets.size()) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar un offset para cada pais");
        }
        if (countriesPedidos.stream().anyMatch(c -> c == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresó un país inválido");
        }
        
        Streams.forEachPair(countriesPedidos.stream(), offsets.stream(), (country, offset) -> {
        	       	
            DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            Date date;
            try {
                date = formatter.parse(offset);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato de las fechas debe ser dd/mm/aaaa.");
            }       	
        	country.setDataReport(country.getDataReport().stream()
        			.filter(item ->!item.getDate().before(date))
        			.sorted(Comparator.comparing(DataReport::getDate)).collect(Collectors.toList()));               	
        });        
        return countriesPedidos;              
    }

    @Override
    public void saveAll(List<DataReport> dataReports) {
        dataReports.forEach(dr -> drDAO.save(dr));
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
