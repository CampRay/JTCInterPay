package com.jtc.model;

import java.io.Serializable;

public class ChannelSetting implements Serializable {
	
	private static final long serialVersionUID = -2631333858782507428L;
	//是否使用测试模式
	private boolean sandbox=true;
	//支付宝应用ID
	private String appId;
	//商家账号
	private String businessEmail;
	//数据传输口令
	private String token;
	
	//公钥
	private String publicKey;
	//私钥
	private String privateKey;
	
	//登录ID(商酷接口需要)
	private String loginId;
	//登录密码(商酷接口需要)
	private String loginPassword;
	
	
	//支付网关异步通知处理
	private String ipnURL;
	
	private String returnURL;
		

	public boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getIpnURL() {
		return ipnURL;
	}

	public void setIpnURL(String ipnURL) {
		this.ipnURL = ipnURL;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}	
	
	
		
}
