package com.jtc.koolyun.reponse;

public class CSBPayResponse extends BaseResponse{
	private String action;
	private String responseCode;
	private String errorMsg;
	private String txnId;
	private String data;
	private String qrcodeResult;
	private String qrcodeUrl;
	private String qrcode;
	private String refNo;
	private String reultMsg;
	
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
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
	public String getQrcodeResult() {
		return qrcodeResult;
	}
	public void setQrcodeResult(String qrcodeResult) {
		this.qrcodeResult = qrcodeResult;
	}
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}
	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getReultMsg() {
		return reultMsg;
	}
	public void setReultMsg(String reultMsg) {
		this.reultMsg = reultMsg;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	
}
