package com.jtc.service;

import java.util.List;

import com.jtc.dto.Tchannel;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface ChannelService {
	
	Tchannel getChannelById(int id);
	
	List<Tchannel> getAllChannels();
	
	void createChannel(Tchannel channel);
	
	void updateChannel(Tchannel channel);
	
	void deleteChannel(Tchannel channel);
	
	void deleteChannelById(int id);
	
	void deleteChannelsByIds(Integer[] ids);	
	
	Tchannel loadChannelByCode(String code);
	
	PagingData loadChannelsList(DataTableParamter rdtp);
		
		
}
