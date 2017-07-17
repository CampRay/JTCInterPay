package com.jtc.model;

import java.io.Serializable;

public class ChannelSetting implements Serializable {
	
	private static final long serialVersionUID = -2631333858782507428L;
	
	private boolean sandbox=true;

	private String businessEmail;
	
	private String token;
	
	private String ipnURL;
	
	private String returnURL;
		

	public boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
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
		
}
