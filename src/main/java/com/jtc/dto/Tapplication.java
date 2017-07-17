package com.jtc.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jtc.commons.SecurityTools;


/**
 * The persistent class for the group database table.
 * 
 */
public class Tapplication implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;	
	//生成的唯一16位字符串
	private String appId;	

	private String name;		
	
	private String desc;
	
	private Long createdTime;
	
	private String createdTimeStr;
	
	private String returnUrl;
	
	private String notifyUrl;
	
	//系統自動生成的訪問令牌，應用程序向JTC支付平臺發送URL請求時必需提供此令牌
	private String token;

	public Tapplication() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getToken() {	
		token=SecurityTools.SHA(this.id+"");
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
		

}