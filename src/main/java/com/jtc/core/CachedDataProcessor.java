/**   
 * @Title: CachedDataProcessor.java 
 * @Package com.bps.core 
 *
 * @Description: User Points Management System
 * 
 * @date Nov 13, 2014 5:23:42 PM
 * @version V1.0   
 */ 
package com.jtc.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.jtc.job.RestartNotifyTask;
import com.jtc.service.AdminNodesService;
import com.jtc.service.CurrencyService;
import com.jtc.service.SystemSettingService;

/** 
 * <p>Sping容器初始化完成后的缓存甚而数据的处理方法</p>
 * @ClassName: CachedDataProcessor 
 * @author Phills Li 
 * 
 */
public class CachedDataProcessor implements ApplicationListener<ContextRefreshedEvent> {
	
    public void onApplicationEvent(ContextRefreshedEvent event) {
		//root application context
    	if(event.getApplicationContext().getParent() == null){
    		AdminNodesService adminNodesService=(AdminNodesService) event.getApplicationContext().getBean("adminNodesService");
    		
    		SystemSettingService systemSettingService =(SystemSettingService)event.getApplicationContext().getBean("systemSettingService");
    		
    		if(adminNodesService!=null){
    			adminNodesService.cachedNodesData();    			
    		}
    		if(systemSettingService != null){
    			try {
					systemSettingService.cachedSystemSettingData();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		CurrencyService currencyService =(CurrencyService)event.getApplicationContext().getBean("currencyService");
    		if(currencyService != null){
    			currencyService.cachedData();
    		}
    		
    		//启动后发送通知消息
    		RestartNotifyTask restartNotifyTask=new RestartNotifyTask(); 
    		restartNotifyTask.run();
    		
    	}
    	else{//projectName-servlet  context
    		
    	}
    }
}
