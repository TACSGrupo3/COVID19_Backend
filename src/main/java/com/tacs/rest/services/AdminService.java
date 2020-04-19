package com.tacs.rest.services;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;

public interface AdminService {

	public User getUser(int userId);
	
	public int getLists(int IDantiguedad);
	
	public List<Country> getCountries(int IDlista1, int compare);
	
	public int getInteresados(int countryId);
	
}
