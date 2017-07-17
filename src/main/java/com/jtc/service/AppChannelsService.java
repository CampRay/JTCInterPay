package com.jtc.service;

import java.util.List;

import com.jtc.dto.TappChannels;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface AppChannelsService {
	
	TappChannels getAppChannelById(int id);
	
	TappChannels getAppChannel(int appId, int channelId);
	
	List<TappChannels> getAllChannelsByAppId(int appId);
	
	void createAppChannel(TappChannels appChannel);
	
	void updateAppChannel(TappChannels appChannel);
	
	void deleteAppChannel(TappChannels appChannel);
	
	void deleteAppChannelById(int id);
	
	void deleteAppChannelsByIds(Integer[] ids);
	
	PagingData loadAppChannelsList(DataTableParamter rdtp);
		
		
}
