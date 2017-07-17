package com.jtc.service;

import java.util.List;

import com.jtc.dto.Tlanguage;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface LanguageService {

	
	Tlanguage getLanguageById(Integer Id);
	
	void createLanguage(Tlanguage setting);
	
	void updateLanguage(Tlanguage setting);
	
	void deleteLanguage(Tlanguage setting);
	
	void deleteLanguageByIds(Integer[] ids);
	
	void activeLanguageByids(Integer ids[]);
	
	public PagingData loadLanguageList(DataTableParamter rdtp);
	
	List<Tlanguage> loadAllTlanguage();
	
	Tlanguage get(String locale);
	
	Tlanguage getLanguageBylocal(String local);
}
