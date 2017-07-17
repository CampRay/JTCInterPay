package com.jtc.service;

import java.util.List;

import com.jtc.dto.TadminApps;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface AdminAppsService {
	
	TadminApps getAdminAppById(int id);
	
	TadminApps getAdminApp(int appId, String adminId);
	
	List<TadminApps> getAllAppsByAdminId(String adminId);
	
	void createAdminApp(TadminApps adminApp);
	
	void updateAdminApp(TadminApps adminApp);
	
	void deleteAdminApp(TadminApps adminApp);
	
	void deleteAdminAppById(int id);
	
	void deleteAdminAppsByIds(Integer[] ids);
	
	PagingData loadAdminAppsList(DataTableParamter rdtp);
		
		
}
