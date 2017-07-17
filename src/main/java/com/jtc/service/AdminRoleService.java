package com.jtc.service;

import java.util.List;

import com.jtc.dto.TadminRole;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface AdminRoleService {
	
	TadminRole getAdminRoleById(int roleId);
	
	List<TadminRole> getAllAdminRoles();
	
	void createAdminRole(TadminRole adminRole);
	
	void updateAdminRole(TadminRole adminRole);
	
	void deleteAdminRole(TadminRole adminRole);
	
	void deleteAdminRoleById(int id);
	
	void deleteAdminRolesByIds(Integer[] ids);
	
	PagingData loadAdminRolesList(DataTableParamter rdtp);
		
}
