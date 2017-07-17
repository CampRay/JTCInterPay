package com.jtc.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.commons.SystemConfig;
import com.jtc.commons.SystemConstants;
import com.jtc.dao.SettingDao;
import com.jtc.dto.Tsetting;
import com.jtc.model.PagingData;
import com.jtc.service.SystemSettingService;
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

	
	@Autowired
	private SettingDao settingDao;

	
	public Tsetting getSystemsettingById(Integer Id) {
		// TODO Auto-generated method stub
		return settingDao.get(Id);
	}

	
	public void createSystemsetting(Tsetting setting) {
		// TODO Auto-generated method stub
		settingDao.create(setting);
	}

	
	public void updateSystemsetting(Tsetting setting) {
		// TODO Auto-generated method stub
		settingDao.update(setting);
	}

	
	public void deleteSystemsetting(Tsetting setting) {
		// TODO Auto-generated method stub
		settingDao.delete(setting);
	}

	
	public void deleteSystemsetting(int id) {
		// TODO Auto-generated method stub
		settingDao.delete(id);
	}

	
	public void deleteSystemsettingByIds(Integer[] ids) {
		// TODO Auto-generated method stub
		settingDao.deleteAll(ids);
	}

	
	public PagingData loadSystemsetting() {
		// TODO Auto-generated method stub
		Criterion criterions = Restrictions.not(Restrictions.in("name", new Object[]{SystemConstants.RESTAURANT_NAME,SystemConstants.ACCESS_PASSWORD
				,SystemConstants.RESTAURANT_LOGO,SystemConstants.PAGE_BACKGROUND,SystemConstants.CURRENCY,SystemConstants.RESTAURANT_LOGO_File
				,SystemConstants.PAGE_BACKGROUND_File}));
		return settingDao.findPage(criterions, 0, 10);
	}


	private List<Tsetting> getAllSystemSetting() {
		// TODO Auto-generated method stub
		return settingDao.LoadAll();
	}
	
	
	public void cachedSystemSettingData() throws IOException {
		// TODO Auto-generated method stub
		List <Tsetting> setingList = getAllSystemSetting();
		String realPath = this.getClass().getResource("/").getPath();
		realPath = realPath.substring(0, realPath.indexOf("WEB-INF")).replace("%20", " ");
		
		SystemConfig.Admin_Setting_Map.clear();
		SystemConfig.TOKEN = null;
		File image;
		for(Tsetting setting:setingList){
			SystemConfig.Admin_Setting_Map.put(setting.getName(),new String(setting.getValue(),"UTF-8"));
			if(SystemConstants.RESTAURANT_LOGO_File.equals((setting.getName()))){
				image = new File(realPath,"upload"+File.separator+"store"+File.separator+"store_logo.png");
				if(image.exists()){
					image.delete();
				}
				FileUtils.copyInputStreamToFile(new ByteArrayInputStream(setting.getValue()), image);
			}else if(SystemConstants.PAGE_BACKGROUND_File.equals(setting.getName())){
				image = new File(realPath,"upload"+File.separator+"store"+File.separator+"store_background.jpg");
				if(image.exists()){
					image.delete();
				}
				FileUtils.copyInputStreamToFile(new ByteArrayInputStream(setting.getValue()), image);
			}
		}
		SystemConfig.TOKEN=SystemConfig.Admin_Setting_Map.get(SystemConstants.TOKEN);
	}


	public PagingData getStoreSetting() {
		// TODO Auto-generated method stub
		Criterion criterions = Restrictions.in("name", new Object[]{SystemConstants.RESTAURANT_NAME,SystemConstants.ACCESS_PASSWORD
				,SystemConstants.RESTAURANT_LOGO,SystemConstants.PAGE_BACKGROUND,SystemConstants.CURRENCY});
		return settingDao.findPage(criterions, 0, 10);
	}


	public Tsetting getSystemSettingByName(String name) {
		// TODO Auto-generated method stub
		return settingDao.findUnique("name", name);
	}

	

}
