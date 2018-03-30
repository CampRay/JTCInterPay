package com.jtc.koolyun.request;

public class LoginRequest {
	private String action;
	private String v;
	private String iposSn;
	private String merchId;
	private String operator;
	private String pwd;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getIposSn() {
		return iposSn;
	}
	public void setIposSn(String iposSn) {
		this.iposSn = iposSn;
	}
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
