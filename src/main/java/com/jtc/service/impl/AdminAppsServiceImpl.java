/**   
 * @Title: AdminAppsServiceImpl.java 
 * @Package com.jtc.service 
 * @date 6 11, 2017 7:21:25 PM
 * @version V1.0   
 */ 
package com.jtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.dao.AdminAppsDao;
import com.jtc.dto.TadminApps;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.AdminAppsService;

/** 
 * <p>Types Description</p>
 * @ClassName: AdminAppsServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class AdminAppsServiceImpl implements AdminAppsService {
	@Autowired
	private AdminAppsDao adminAppsDao;

	
	public TadminApps getAdminAppById(int id) {
		return adminAppsDao.get(id);
	}
	
	
	public TadminApps getAdminApp(int appId, String adminId) {
		Criteria criteria=adminAppsDao.createCriteria();
		criteria=criteria.add(Restrictions.eq("app.id", appId));
		criteria=criteria.add(Restrictions.eq("adminUser.adminId", adminId));
		return (TadminApps)criteria.uniqueResult();
	}

	
	public List<TadminApps> getAllAppsByAdminId(String adminId) {
		return adminAppsDao.findBy("adminUser.adminId", adminId);
	}

	
	public void createAdminApp(TadminApps adminApp) {
		adminAppsDao.create(adminApp);		
	}

	
	public void updateAdminApp(TadminApps adminApp) {
		adminAppsDao.update(adminApp);
	}

	
	public void deleteAdminApp(TadminApps adminApp) {
		adminAppsDao.delete(adminApp);		
	}

	
	public void deleteAdminAppById(int id) {
		adminAppsDao.delete(id);
	}

	
	public void deleteAdminAppsByIds(Integer[] ids) {
		adminAppsDao.deleteAll(ids);
	}

	
	public PagingData loadAdminAppsList(DataTableParamter rdtp) {		
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					if(key=="adminId"){									
						criterionsList.add(Restrictions.ge("adminUser.adminId", jsonObj.getString(key)));
					}
					else{
						criterionsList.add(Restrictions.eq(key, jsonObj.get(key)));
					}
				}
			}
			Criterion[] criterions=new Criterion[criterionsList.size()];
			int i=0;
			for (Criterion criterion : criterionsList) {
				criterions[i]=criterion;	
				i++;
			}
			return adminAppsDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return adminAppsDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
			

}
