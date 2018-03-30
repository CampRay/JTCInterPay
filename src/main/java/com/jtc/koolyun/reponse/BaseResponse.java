package com.jtc.koolyun.reponse;

public class BaseResponse {
	public final static String SUCCESS = "SUCCESS";

	public final static String FAIL = "FAIL";

	public final static String UNKNOW = "UNKNOW";
	private String requestResult;
	private String requestResultMsg;
	private String rawData;


	public String getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}

	

	public String getRequestResultMsg() {
		return requestResultMsg;
	}

	public void setRequestResultMsg(String requestResultMsg) {
		this.requestResultMsg = requestResultMsg;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	
	
}
