package com.tacs.rest.services;

import java.util.List;
import com.tacs.rest.entity.Country;
import com.tacs.rest.entity.User;

public interface AdminService {

	public User getUser(int userId);
	
	public List<Country> getCountries(List<String> IDlistas);
	
	public int getLists(int IDantiguedad);
	
	public int getInteresados(int countryId);
	
}
