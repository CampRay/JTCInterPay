/**   
 * @Title: ApplicationServiceImpl.java 
 * @Package com.jtc.service 
 * @date 6 23, 2017 7:21:25 PM
 * @version V1.0   
 */ 
package com.jtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.dao.ApplicationDao;
import com.jtc.dto.Tapplication;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.ApplicationService;

/** 
 * <p>Types Description</p>
 * @ClassName: ApplicationServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
	@Autowired
	private ApplicationDao applicationDao;

	
	public Tapplication getAppById(int id) {
		return applicationDao.get(id);
	}
	
	public Tapplication getAppByAppId(String appId) {
		return applicationDao.findUnique("appId", appId);
	}

	
	public void createApp(Tapplication app) {
		applicationDao.create(app);		
	}

	
	public void updateApp(Tapplication app) {
		applicationDao.update(app);
	}

	
	public void deleteApp(Tapplication app) {
		applicationDao.delete(app);		
	}

	
	public void deleteAppById(int id) {
		applicationDao.delete(id);
	}

	
	public void deleteAppsByIds(Integer[] ids) {
		applicationDao.deleteAll(ids);
	}
	
	
	public Tapplication loadAppByName(String name) {
		return applicationDao.findUnique("name", name);
	}

	
	public PagingData loadAppsList(DataTableParamter rdtp) {
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					if(key=="deleted"){
						criterionsList.add(Restrictions.eq(key, jsonObj.getBoolean(key)));
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
			return applicationDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return applicationDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
				

}
