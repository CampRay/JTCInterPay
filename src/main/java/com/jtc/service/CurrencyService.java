package com.jtc.service;

import com.jtc.dto.Tcurrency;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface CurrencyService {
	
	Tcurrency getCurrencyById(int id);		
	
	void createCurrency(Tcurrency currency);
	
	void updateCurrency(Tcurrency currency);
	
	void deleteCurrency(Tcurrency currency);
	
	void deleteCurrencyById(int id);
	
	void deleteCurrencysByIds(Integer[] ids);	
	
	Tcurrency loadCurrencyByCode(String code);
	
	PagingData loadCurrencysList(DataTableParamter rdtp);
		
	void cachedData();	
}
