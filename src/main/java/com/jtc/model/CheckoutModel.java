package com.jtc.model;

public class CheckoutModel {
	
	//請求指令：select, payment,  
	private String cmd;

	private String appid;
	
	private String txn_id;	
	
	private String order_id;	

	private String order_title;
	
	private String order_desc;			
	
	private Double order_amount;
	
	private String currency_code;	
	
	private String country_code;	
	
	private String custom;
	
	private String return_url;
	
	private String notify_url;
	
	private String charset="utf-8";
	
	private String token;
	
	private String sign;
	
	private String channel_code;
	
	//支付狀態：true 成功，flase 失敗
	private boolean status;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getTxn_id() {
		return txn_id;
	}

	public void setTxn_id(String txn_id) {
		this.txn_id = txn_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_title() {
		return order_title;
	}

	public void setOrder_title(String order_title) {
		this.order_title = order_title;
	}

	public String getOrder_desc() {
		return order_desc;
	}

	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}

	public Double getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(Double order_amount) {
		this.order_amount = order_amount;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

		

}
