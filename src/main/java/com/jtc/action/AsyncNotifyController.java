package com.jtc.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtc.commons.HttpClientUtil;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.dto.Torder;
import com.jtc.job.SingleNotifyTask;
import com.jtc.service.AdminAppsService;
import com.jtc.service.AdminUserService;
import com.jtc.service.AppChannelsService;
import com.jtc.service.ApplicationService;
import com.jtc.service.ChannelService;
import com.jtc.service.OrderService;

/**
 * 异步 通知消息处理器类
 * @author Phills
 *
 */
@Controller
@RequestMapping(value="asyncnotify")
public class AsyncNotifyController {
	private Logger logger = Logger.getLogger(AsyncNotifyController.class);
	private final String CHANNEL_CODE="paypal_pc";

	@Resource
	private AdminUserService adminUserService;
		
	@Resource
	private ApplicationService applicationService;
	
	@Resource
	private AdminAppsService adminAppsService;
		
	@Resource
	private ChannelService channelService;
	
	@Resource
	private AppChannelsService appChannelsService;
	
	@Resource
	private OrderService orderService;
	
	private String GetPaypalUrl(boolean isSandbox)
    {
        return isSandbox ? "https://www.sandbox.paypal.com/us/cgi-bin/webscr" :
            "https://www.paypal.com/us/cgi-bin/webscr";
    }
	
	
	
	
    /**
     * <p>Description: paypal_pc支付數據异步通知處理方法</p>
     * @param request
     * @param response
     * @param userAgent
     * @param body
     * @return
     */
	@RequestMapping(value="paypal_pc",method=RequestMethod.POST)	
	@ResponseBody
	public String paypalPc(HttpServletRequest request,HttpServletResponse response,@RequestHeader ("User-Agent") String userAgent,@RequestBody String body){				
		//获取PayPal支付订单的业务ID
		String custom=(String)request.getParameter("custom");
		double amount=Double.valueOf(request.getParameter("mc_gross"));
		String currencyCode=(String)request.getParameter("mc_currency");
		String receiver_email=(String)request.getParameter("receiver_email");
				
		if(StringTool.isEmptyOrNull(custom)){
			return "";
		}
		else{
			int orderid=Integer.parseInt(custom);	
			Torder order=orderService.getOrderById(orderid);				
			if(order==null||order.getOrderAmount()!=amount||!order.getCurrencyCode().equalsIgnoreCase(currencyCode)){				
				return "";
			}
			else{
				Tapplication app=order.getApp();
				Tchannel channel=channelService.loadChannelByCode(CHANNEL_CODE);				
				TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());								
				if(appChannel==null||!appChannel.getChannelSetting().getBusinessEmail().equals(receiver_email)){					
					return "";
				}
				else{										
					StringBuffer bs = new StringBuffer();
					bs.append(body);
					bs.append("&cmd=_notify-validate");
																														
					Map<String,String> headerMap=new HashMap<String, String>();
					headerMap.put("Content-Type", "application/x-www-form-urlencoded");
					headerMap.put("User-Agent", userAgent);
					
					String responseStr="";
					try{						
						responseStr=HttpClientUtil.sendPost(GetPaypalUrl(appChannel.getChannelSetting().getSandbox()),request.getCharacterEncoding(),headerMap, bs.toString());						
					}
					catch(MyException me){						
						return "";
					}
					//如果获取支付结果信息成功
					if("VERIFIED".equalsIgnoreCase(responseStr.trim())){								
//						String item_name=(String)request.getParameter("item_name");
//						String item_number=(String)request.getParameter("item_number");
						String status=(String)request.getParameter("payment_status");												
						String txn_id=(String)request.getParameter("txn_id");
//						String txn_type=(String)request.getParameter("txn_type");																														
												
						//处理订单支付状态
						int payStatus=0;
						if("pending".equalsIgnoreCase(status)){
							payStatus=0;
						}
						else if("processed".equalsIgnoreCase(status)||"completed".equalsIgnoreCase(status)||"canceled_reversal".equalsIgnoreCase(status)){
							payStatus=1;
							if(order.getPaidTime()==null){
								order.setPaidTime(System.currentTimeMillis());
							}
							order.setAmountSettle(amount);
						}
						else if("denied".equalsIgnoreCase(status)||"expired".equalsIgnoreCase(status)||"failed".equalsIgnoreCase(status)||"voided".equalsIgnoreCase(status)){
							payStatus=2;
							order.setFailureCode(status);
						}
						else if("refunded".equalsIgnoreCase(status)||"reversed".equalsIgnoreCase(status)){
							payStatus=3;
							order.setRefundedTime(System.currentTimeMillis());
							order.setAmountRefunded(amount);
						}
						else{
							payStatus=0;
						}
						//修改订单支付结果						
						order.setTransactionNo(txn_id);
						order.setStatus(payStatus);	
						if(StringTool.isEmptyOrNull(order.getSign())){
							order.setSign(SecurityTools.GenerateUniqueText());							
						}
						orderService.updateOrder(order);
						response.setStatus(200);

						//启动后发送通知消息							
						SingleNotifyTask snt=new SingleNotifyTask(order.getId());						
						SystemConstants.scheduledThreadPoolExecutor.schedule(snt,0,TimeUnit.SECONDS);
//							SystemConstants.scheduledThreadPoolExecutor.schedule(snt,30,TimeUnit.SECONDS);
//							SystemConstants.scheduledThreadPoolExecutor.schedule(snt,1,TimeUnit.MINUTES);
//							SystemConstants.scheduledThreadPoolExecutor.schedule(snt,3,TimeUnit.MINUTES);
//							SystemConstants.scheduledThreadPoolExecutor.schedule(snt,1,TimeUnit.HOURS);
						
						return "";						
					}
					else{
						return "";
					}
					
				}
												
			}																		
			
		}		
		
	}
}
