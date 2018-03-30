package com.jtc.koolyun.reponse;

public class CancelResponse extends BaseResponse{
	private String action;
	private String responseCode;
	private String errorMsg;
	private String data;
	private String cancelResult;
	private String reultMsg;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCancelResult() {
		return cancelResult;
	}
	public void setCancelResult(String cancelResult) {
		this.cancelResult = cancelResult;
	}
	public String getReultMsg() {
		return reultMsg;
	}
	public void setReultMsg(String reultMsg) {
		this.reultMsg = reultMsg;
	}
	
}
