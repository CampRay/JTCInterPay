package com.jtc.job;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.jtc.commons.MyApplicationContextUtil;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.Torder;
import com.jtc.service.OrderService;


public class SingleNotifyTask implements Runnable {
	private Logger logger = Logger.getLogger(SingleNotifyTask.class);
	
	private int num=1;
	
	private boolean stopNotify=false;
	
	private Map<String,String> headerMap=new HashMap<String, String>();
	
	private int orderId=0;		

	public SingleNotifyTask(int orderId) {
		this.orderId=orderId;
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		headerMap.put("User-Agent", "JTC IPN ( https://www.jtc.com/ipn )");
	}	
	
	/**
	 * 發送異步通知到應用的URL
	 * @param order
	 */
	private void sendNotify(Torder order){				
		
		Map<String, Object> formMap=new HashMap<String, Object>();
		formMap.put("order_amount", order.getOrderAmount());
		formMap.put("currency_code", order.getCurrencyCode());
		formMap.put("order_id", order.getOrderNo());
		formMap.put("status", SystemConstants.PAYMENT_STATUS.get(order.getStatus()));
		formMap.put("txn_id", order.getId());
		formMap.put("sign",order.getSign() );
		formMap.put("custom",order.getCustom());
		
		
		try{
			logger.info("SingleNotifyTask================sendNotify: "+order.getNotifyUrl());
			//String responseStr=HttpClientUtil.sendPost(order.getNotifyUrl(),order.getCharset(),null, formMap);
			//logger.info("SingleNotifyTask================Response： "+responseStr);					
			PrintWriter out = null;			
			try {
				URL url = new URL(order.getNotifyUrl());
				HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
				urlcon.setDoInput(true);			
				urlcon.setDoOutput(true);
				// Post 请求不能使用缓存
				urlcon.setUseCaches(false);
				urlcon.setRequestMethod("POST");
				if(headerMap!=null){
					for (String key : headerMap.keySet()) {
						urlcon.setRequestProperty(key, headerMap.get(key));
					}
				}
				
				StringBuilder sb=new StringBuilder();				
				for (Map.Entry<String, Object> entry : formMap.entrySet()) {  
					sb.append(entry.getKey()+"="+String.valueOf(entry.getValue())+"&");	                
	            } 				
				logger.info("SingleNotifyTask================content: "+sb.toString());						
				urlcon.connect();// 获取连接
				out = new PrintWriter(urlcon.getOutputStream());
				out.print(sb.toString());
				out.flush();
				logger.info("SingleNotifyTask================ResponseCode: "+urlcon.getResponseCode());
				stopNotify=(urlcon.getResponseCode()==HttpURLConnection.HTTP_OK);							
				
			} catch (IOException e) {	
				logger.error("SingleNotifyTask exception: "+e.getMessage());
				// 发生网络异常  
	            throw new MyException(1002);  			
			} finally {
				try {					
					if (null != out)
						out.close();
				} catch (Exception e2) {				
				}
			}
			
			
		}
		catch(Exception me){
			logger.error("SingleNotifyTask exception: "+me.getMessage());
		}
		finally{
			if(!stopNotify&&num<=4){				
				if(num<=3){
					SystemConstants.scheduledThreadPoolExecutor.schedule(this, num*30, TimeUnit.SECONDS);
				}
				else{
					SystemConstants.scheduledThreadPoolExecutor.schedule(this, 1, TimeUnit.HOURS);
				}
				num++;
			}
		}
	}

	
	public void run() {										
		logger.info("SingleNotifyTask================orderId: "+orderId);
		OrderService orderService=(OrderService)MyApplicationContextUtil.getContext().getBean("orderService");
		if(orderId!=0){
			Torder order=orderService.getOrderById(orderId);			
			if(order!=null&&!order.isNotified()){
				if(StringTool.isEmptyOrNull(order.getNotifyUrl())){
					order.setNotified(true);
					orderService.updateOrder(order);
				}
				else{
					if(StringTool.isEmptyOrNull(order.getSign())){
						order.setSign(SecurityTools.GenerateUniqueText());
						orderService.updateOrder(order);
					}
					//發送異步通知
					sendNotify(order);
				}
			}
		}				
		
	}

}
