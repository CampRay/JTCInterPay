package com.jtc.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.jtc.commons.StringTool;
import com.jtc.model.ChannelSetting;


/**
 * The persistent class for the group_user database table.
 * 
 */
public class TappChannels implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;		

	private Tapplication app;		
	
	private Tchannel channel;
	
	private Long createdTime;
	
	private String createdTimeStr;
	
	private String setting;
	
	private ChannelSetting channelSetting=new ChannelSetting();
	

	public TappChannels() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tapplication getApp() {
		return app;
	}

	public void setApp(Tapplication app) {
		this.app = app;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedTimeStr() {		
		if(createdTime!=null){
			Date date=new Date(createdTime);
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			return sdf.format(date);		
		}else
		return this.createdTimeStr;		
	}

	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}

	public Tchannel getChannel() {
		return channel;
	}

	public void setChannel(Tchannel channel) {
		this.channel = channel;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

	public ChannelSetting getChannelSetting() {
		if(!StringTool.isEmptyOrNull(setting)){
			channelSetting = JSON.parseObject(setting,ChannelSetting.class);
		}
		return channelSetting;
	}

	public void setChannelSetting(ChannelSetting channelSetting) {		
		this.channelSetting = channelSetting;		
	}		

}