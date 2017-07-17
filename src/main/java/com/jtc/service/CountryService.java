package com.jtc.service;

import com.jtc.dto.Tcountry;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface CountryService {
	
	Tcountry getCountryById(int id);		
	
	void createCountry(Tcountry country);
	
	void updateCountry(Tcountry country);
	
	void deleteCountry(Tcountry country);
	
	void deleteCountryById(int id);
	
	void deleteCountrysByIds(Integer[] ids);	
	
	Tcountry loadCountryByCode(String code);
	
	PagingData loadCountrysList(DataTableParamter rdtp);
		
		
}
