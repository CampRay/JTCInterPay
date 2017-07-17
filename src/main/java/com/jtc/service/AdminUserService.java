package com.jtc.service;

import java.util.List;

import com.jtc.dto.TadminUser;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface AdminUserService {
	
	TadminUser getAdminUserById(String userId);
		
	TadminUser getAdminUserByIdOrEmail(String userId);
	
	TadminUser getAdminUserByIdOrEmail(String userId,String email);
	
	void createAdminUser(TadminUser adminUser);
	
	void updateAdminUser(TadminUser adminUser);
	
	void updateAdminUserPassword(TadminUser adminUser);
	
	void deleteAdminUser(TadminUser adminUser);
	
	void deleteAdminUserById(int id);
	
	void deleteAdminUserByIds(String[] ids);
	
	void activateUsersByIds(String[] ids);
	
	void deactivateUsersByIds(String[] ids);
	
	public PagingData loadAdminUserList(DataTableParamter rdtp);
	
	public int getAdminUserAmount();
	
	public TadminUser getTadminUsersByEmail(String email);
	
	public TadminUser getTadminUsersByToken(String token);
	
	public List<TadminUser> loadDoctorList();
	
	public List<TadminUser> loadAllAdminUserList();
		
}
