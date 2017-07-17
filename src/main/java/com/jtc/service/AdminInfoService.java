package com.jtc.service;

import com.jtc.dto.TadminInfo;

public interface AdminInfoService{
	TadminInfo getAdminInfoById(String adminId);
	void updateAdminInfo(TadminInfo adminInfo);
	void updateAdminInfoAvatar(TadminInfo adminInfo);
	void createAdminInfo(TadminInfo adminInfo);
}
