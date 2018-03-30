package com.jtc.koolyun.reponse;

public class LoginResponse extends BaseResponse{
	private String action;
	private String responseCode;
	private String errorMsg;
	private String gradeId;
	private String aliasName;
	private String lastLoginTime;
	private String loginCount;	
	private String iposId;
	private String iposSn;
	private String payParamVersion;
	private String merchId;
	private String batchNo;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(String loginCount) {
		this.loginCount = loginCount;
	}
	public String getIposId() {
		return iposId;
	}
	public void setIposId(String iposId) {
		this.iposId = iposId;
	}
	public String getIposSn() {
		return iposSn;
	}
	public void setIposSn(String iposSn) {
		this.iposSn = iposSn;
	}
	public String getPayParamVersion() {
		return payParamVersion;
	}
	public void setPayParamVersion(String payParamVersion) {
		this.payParamVersion = payParamVersion;
	}
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
