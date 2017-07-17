/**   
 * @Title: ChannelServiceImpl.java 
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
import com.jtc.dao.ChannelDao;
import com.jtc.dto.Tchannel;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.ChannelService;

/** 
 * <p>Types Description</p>
 * @ClassName: ChannelServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class ChannelServiceImpl implements ChannelService {
	@Autowired
	private ChannelDao channelDao;

	
	public Tchannel getChannelById(int id) {
		return channelDao.get(id);
	}	
	
	
	public List<Tchannel> getAllChannels(){
		return channelDao.LoadAll();
	}

	
	public void createChannel(Tchannel channel) {
		channelDao.create(channel);		
	}

	
	public void updateChannel(Tchannel channel) {
		channelDao.update(channel);
	}

	
	public void deleteChannel(Tchannel channel) {
		channelDao.delete(channel);		
	}

	
	public void deleteChannelById(int id) {
		channelDao.delete(id);
	}

	
	public void deleteChannelsByIds(Integer[] ids) {
		channelDao.deleteAll(ids);
	}
		
	
	public Tchannel loadChannelByCode(String code){
		return channelDao.findUnique("code", code);
	}

	
	public PagingData loadChannelsList(DataTableParamter rdtp) {
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					criterionsList.add(Restrictions.eq(key, jsonObj.get(key)));						
				}
			}
			Criterion[] criterions=new Criterion[criterionsList.size()];
			int i=0;
			for (Criterion criterion : criterionsList) {
				criterions[i]=criterion;	
				i++;
			}
			return channelDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return channelDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
				

}
