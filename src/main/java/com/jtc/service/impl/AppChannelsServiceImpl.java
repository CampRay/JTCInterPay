/**   
 * @Title: AppChannelsServiceImpl.java 
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
import com.jtc.dao.AppChannelsDao;
import com.jtc.dto.TappChannels;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.AppChannelsService;

/** 
 * <p>Types Description</p>
 * @ClassName: AppChannelsServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class AppChannelsServiceImpl implements AppChannelsService {
	@Autowired
	private AppChannelsDao appChannelsDao;

	
	public TappChannels getAppChannelById(int id) {
		return appChannelsDao.get(id);
	}
	
	
	public TappChannels getAppChannel(int appId, int channelId) {
		Criteria criteria=appChannelsDao.createCriteria();
		criteria=criteria.add(Restrictions.eq("app.id", appId));
		criteria=criteria.add(Restrictions.eq("channel.id", channelId));
		return (TappChannels)criteria.uniqueResult();
	}

	
	public List<TappChannels> getAllChannelsByAppId(int appId) {
		return appChannelsDao.findBy("app.id", appId);
	}

	
	public void createAppChannel(TappChannels appChannel) {
		appChannelsDao.create(appChannel);		
	}

	
	public void updateAppChannel(TappChannels appChannel) {
		appChannelsDao.update(appChannel);
	}

	
	public void deleteAppChannel(TappChannels appChannel) {
		appChannelsDao.delete(appChannel);		
	}

	
	public void deleteAppChannelById(int id) {
		appChannelsDao.delete(id);
	}

	
	public void deleteAppChannelsByIds(Integer[] ids) {
		appChannelsDao.deleteAll(ids);
	}

	
	public PagingData loadAppChannelsList(DataTableParamter rdtp) {		
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					if(key=="appId"){									
						criterionsList.add(Restrictions.ge("app.id", jsonObj.getInteger(key)));
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
			return appChannelsDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return appChannelsDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
			

}
