package com.jtc.service;

import com.jtc.dto.Tapplication;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface ApplicationService {
	
	Tapplication getAppById(int id);
	
	Tapplication getAppByAppId(String appId);
	
	void createApp(Tapplication app);
	
	void updateApp(Tapplication app);
	
	void deleteApp(Tapplication app);
	
	void deleteAppById(int id);
	
	void deleteAppsByIds(Integer[] ids);
	
	public Tapplication loadAppByName(String name);
	
	PagingData loadAppsList(DataTableParamter rdtp);
		
		
}
