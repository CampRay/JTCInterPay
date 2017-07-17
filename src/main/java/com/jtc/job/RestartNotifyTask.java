package com.jtc.job;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.jtc.commons.MyApplicationContextUtil;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.Torder;
import com.jtc.service.OrderService;


public class RestartNotifyTask{
	private final Logger logger = Logger.getLogger(RestartNotifyTask.class);					
	
	public RestartNotifyTask() {
		
	}	
	
	public void run() {						
		OrderService orderService=(OrderService)MyApplicationContextUtil.getContext().getBean("orderService");
		List<Torder> orderList=orderService.loadOrderByNotified(false);
		for (Torder order : orderList) {			
			try {
				//启动后发送通知消息				
				SingleNotifyTask snt=new SingleNotifyTask(order.getId());				
				SystemConstants.scheduledThreadPoolExecutor.schedule(snt,0,TimeUnit.SECONDS);								
			} catch (Exception e) {
				logger.error("RestartNotifyTask Exception:"+e.getMessage());
			}
		}
	}

}
