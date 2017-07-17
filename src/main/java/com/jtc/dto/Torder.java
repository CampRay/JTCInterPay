package com.jtc.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the pg_order database table.
 * 
 */
public class Torder implements Serializable {
	
	private static final long serialVersionUID = -8658472374205094291L;
	
	private int id;

	private String orderNo;	

	private String orderTitle;
	
	private String orderDesc;			
	
	private Double orderAmount;
	
	private String currencyCode;	
	
	private String countryCode;	
	
	private String custom;
	
	private Tapplication app;
	
	private Tchannel channel;
		
	private String transactionNo;		
	
	private String clientIp;
	
	private Double amountSettle=0.00;
	
	private Double amountRefunded=0.00;			
	
	private Long createdTime;
	
	private String createdTimeStr;
	
	private Long paidTime;
	
	private String paidTimeStr;
	
	private Long refundedTime;
	
	private String refundedTimeStr;
	
	private boolean refunded=false;

	//0处理中,  1 支付成功, 2 支付失败
	private int status=0;
	
	private String failureCode;
	
	private boolean notified=false;	
	
	private String returnUrl;
	
	private String notifyUrl;
	
	private String charset="utf-8";
	
	private String channelName;
		
	private String sign;
	
	public Torder() {
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
		return createdTimeStr;
	}


	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getOrderTitle() {
		return orderTitle;
	}


	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}


	public String getOrderDesc() {
		return orderDesc;
	}


	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}


	public Double getOrderAmount() {
		return orderAmount;
	}


	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}	

	public String getCustom() {
		return custom;
	}


	public void setCustom(String custom) {
		this.custom = custom;
	}


	public Tapplication getApp() {
		return app;
	}


	public void setApp(Tapplication app) {
		this.app = app;
	}


	public Tchannel getChannel() {
		return channel;
	}


	public void setChannel(Tchannel channel) {
		this.channel = channel;
	}


	public String getTransactionNo() {
		return transactionNo;
	}


	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}


	public String getClientIp() {
		return clientIp;
	}


	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}


	public Double getAmountSettle() {
		return amountSettle;
	}


	public void setAmountSettle(Double amountSettle) {
		this.amountSettle = amountSettle;
	}


	public Double getAmountRefunded() {
		return amountRefunded;
	}


	public void setAmountRefunded(Double amountRefunded) {
		this.amountRefunded = amountRefunded;
	}


	public Long getPaidTime() {
		return paidTime;
	}


	public void setPaidTime(Long paidTime) {
		this.paidTime = paidTime;
	}


	public String getPaidTimeStr() {
		if(paidTime!=null){
			Date date=new Date(paidTime);
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			return sdf.format(date);		
		}else		
		return paidTimeStr;
	}


	public void setPaidTimeStr(String paidTimeStr) {
		this.paidTimeStr = paidTimeStr;
	}


	public Long getRefundedTime() {
		return refundedTime;
	}


	public void setRefundedTime(Long refundedTime) {
		this.refundedTime = refundedTime;
	}


	public String getRefundedTimeStr() {
		if(refundedTime!=null){
			Date date=new Date(refundedTime);
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			return sdf.format(date);		
		}else	
		return refundedTimeStr;
	}


	public void setRefundedTimeStr(String refundedTimeStr) {
		this.refundedTimeStr = refundedTimeStr;
	}


	public boolean isRefunded() {
		return refunded;
	}


	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getFailureCode() {
		return failureCode;
	}


	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}


	public boolean isNotified() {
		return notified;
	}


	public void setNotified(boolean notified) {
		this.notified = notified;
	}


	public String getChannelName() {
		if(channel!=null){
			return channel.getName();
		}
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
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


	public String getCharset() {
		return charset;
	}


	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}
		
}