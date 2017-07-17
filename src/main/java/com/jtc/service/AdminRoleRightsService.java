package com.jtc.service;

import com.jtc.dto.TadminRoleRights;

public interface AdminRoleRightsService {
	
	TadminRoleRights getAdminRoleRightsById(int roleId);
	
	void createAdminRoleRights(TadminRoleRights adminRoleRights);
	
	void saveAdminRoleRights(TadminRoleRights adminRoleRights);
	
	void updateAdminRoleRights(TadminRoleRights adminRoleRights);
	
	void deleteAdminRoleRights(TadminRoleRights adminRoleRights);
		
}
