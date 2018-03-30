package com.jtc.koolyun.request;

public class CSBPayRequest {
	//固定值: msc/txn/request
	private String action;
	//支付活动号
	private String paymentId;
	//交易类型
	private String transType;
	//批次号（6位数字，登录时由后台返回）
	private String batchNo;
	//流水号（6位数字，登录时重置为“000000”,之后每次交易+1）
	private String traceNo;
	//日期时间（YYYYMMDDhhmmss）
	private String transTime;
	//金额（单位分）；BSC和CSB必选，撤销可选
	private String transAmount;
	//订单号（商户端保证唯一），必要是据此号来查交易结果
	private String odNo;
	//订单描述（后台交易查询、导出时以备注Description的形式体现）
	private String odDesc;
	//C扫B交易请求时传入，用来接收交易结果通知
	private String notifyUrl;
	//交易币种，使用login返回参数stl_cur（见字典6.8）	常用的交易币种国际代码(注意：退款时上传必须与原交易一致)	156：CNY ,344：HKD, 702：SGD
	private String currency;
	//以下payType,请求内容，值为JSON对象序列号后的字符串
	private String data;
	private String payType;
	private String dataAction;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTraceNo() {
		return traceNo;
	}
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getOdNo() {
		return odNo;
	}
	public void setOdNo(String odNo) {
		this.odNo = odNo;
	}
	public String getOdDesc() {
		return odDesc;
	}
	public void setOdDesc(String odDesc) {
		this.odDesc = odDesc;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDataAction() {
		return dataAction;
	}
	public void setDataAction(String dataAction) {
		this.dataAction = dataAction;
	}
	
}
