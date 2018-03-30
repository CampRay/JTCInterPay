package com.jtc.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
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
import com.jtc.koolyun.pay.QrcodePay;
import com.jtc.model.ChannelSetting;
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
	
	private String getPaypalUrl(boolean isSandbox)
    {
        return isSandbox ? "https://www.sandbox.paypal.com/us/cgi-bin/webscr" :
            "https://www.paypal.com/us/cgi-bin/webscr";
    }
	
	private String getAlipayUrl(boolean isSandbox)
    {
        return isSandbox ? "https://openapi.alipaydev.com/gateway.do" :
            "https://openapi.alipay.com/gateway.do";
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
				Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_PAYPAL_PC);				
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
						responseStr=HttpClientUtil.sendPost(getPaypalUrl(appChannel.getChannelSetting().getSandbox()),request.getCharacterEncoding(),headerMap, bs.toString());						
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

						//启动后台发送异步通知消息任务							
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
	
	
	/**
     * <p>Description: alipay_pc支付數據异步通知處理方法</p>
     * @param request
     * @param response
     * @param userAgent
     * @param body
     * @return
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="alipay_pc")	
	@ResponseBody
	public String alipay_pc(HttpServletRequest request,HttpServletResponse response,@RequestHeader ("User-Agent") String userAgent){				
		//获取支付宝异步通知的所有参数
		String appId=(String)request.getParameter("app_id");//支付宝分配给开发者的应用ID		
		String signType=(String)request.getParameter("sign_type");//签名算法类型，目前支持RSA2和RSA，推荐使用RSA2		
		String charset=(String)request.getParameter("charset");//编码格式
		String amount=(String)request.getParameter("total_amount");//该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
		String tno=(String)request.getParameter("trade_no");//该交易在支付宝系统中的交易流水号。最长64位。
		String orderId=(String)request.getParameter("out_trade_no");//商户网站唯一订单号
		String resultStatus=(String)request.getParameter("trade_status");//支付交易结果				
		if(!StringTool.isEmptyOrNull(orderId)){		
			int orderid=Integer.parseInt(orderId);	
			Torder order=orderService.getOrderById(orderid);				
			if(order!=null&&order.getOrderAmount().doubleValue()==Double.valueOf(amount)&&order.getStatus()!=1){						
				Tapplication app=order.getApp();				
				Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_PAYPAL_PC);
				TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());				
				if(appChannel!=null){									
					ChannelSetting channelSetting=appChannel.getChannelSetting();				
					//将异步通知中收到的所有参数都存放到map中
					Map<String, String> paramsMap =request.getParameterMap();
					//调用SDK验证签名
					boolean signVerified;
					try {
						signVerified = AlipaySignature.rsaCheckV1(paramsMap, channelSetting.getPublicKey(), charset, signType);
						if(signVerified){
							//如果返回的支付结果状态码是支付成功或者是交易结束不可退款
							if("TRADE_SUCCESS".equalsIgnoreCase(resultStatus)||"TRADE_FINISHED".equalsIgnoreCase(resultStatus)){
								//直接关闭交易，不允许退款操作
								AlipayClient alipayClient = new DefaultAlipayClient(getAlipayUrl(channelSetting.getSandbox()),appId,channelSetting.getPrivateKey(),"json",charset,channelSetting.getPublicKey(),"RSA2");
								AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
								closeRequest.setBizContent("{" +
								"    \"trade_no\":\""+tno+"\"," +
								"    \"out_trade_no\":\""+orderId+"\"," +
								"    \"operator_id\":\"Web\"" +
								"  }");
								AlipayTradeCloseResponse closeResponse = alipayClient.execute(closeRequest);
								//调用成功
								if(closeResponse.isSuccess()){
									//修改订单为支付成功状态
									order.setPaidTime(System.currentTimeMillis());
									order.setAmountSettle(Double.valueOf(amount));
									//修改订单支付结果							
									order.setTransactionNo(tno);
									order.setStatus(1);	
									if(StringTool.isEmptyOrNull(order.getSign())){
										order.setSign(SecurityTools.GenerateUniqueText());								
									}
									orderService.updateOrder(order);
									
									//启动后台发送异步通知消息任务							
									SingleNotifyTask snt=new SingleNotifyTask(order.getId());						
									SystemConstants.scheduledThreadPoolExecutor.schedule(snt,0,TimeUnit.SECONDS);
								}
								
							}																		
						}																													
						
					} catch (AlipayApiException e) {					
						e.printStackTrace();
					} 																							
				}																					
			}
		}
		return null;
						
	}
	
	/**
     * <p>Description: 商酷的支付宝扫码支付异步通知處理方法</p>
     * @param request
     * @param response
     * @return
     */	
	@RequestMapping(value="apmp_alipay")
	@ResponseBody
	public String apmp_alipay(HttpServletRequest request,HttpServletResponse response){							
		//获取异步通知的参数		
		String sign=(String)request.getParameter("sign");//签名
		String body=(String)request.getParameter("body");//签名			
		if(!StringTool.isEmptyOrNull(body)){
			JSONObject payResponse = JSONObject.parseObject(body);			
			String odNo=payResponse.getString("odNo");//商户网站唯一订单号
			String amountStr=payResponse.getString("amount");//支付金額
			double amount=Double.valueOf(amountStr)/100.00;
			//String txnTime=payResponse.getString("txnTime");//日期时间（YYYYMMDDhhmmss）
			String txnId=payResponse.getString("txnId");//平台方唯一交易流水号			
			if(!StringTool.isEmptyOrNull(odNo)){			
				int orderid=Integer.parseInt(odNo);	
				Torder order=orderService.getOrderById(orderid);	
				order.setTransactionNo(txnId);
				order.setStatus(2);	//默认设置订单状态为失败	
				if(StringTool.isEmptyOrNull(order.getSign())){
					order.setSign(SecurityTools.GenerateUniqueText());								
				}					
				if(order!=null&&order.getOrderAmount().doubleValue()==amount){
					Tapplication app=order.getApp();				
					Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_APMP_ALIPAY);
					TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());				
					if(appChannel!=null){
						//因为这里验证签名一直失败，所以下面跳过签名验证，直接获取支付结果
						boolean verifyResult=false;
						try {
							verifyResult = QrcodePay.verify(body.getBytes("UTF-8"),sign);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}																	
						if(verifyResult){//本来是判断签名验证结果的
						
							//支付交易结果為成功
							if("SUCCESS".equals(payResponse.getString("payResult"))){
								logger.error("===============================Receive Notified,apmp_alipay Payment success ");
								//修改订单为支付成功状态
								order.setPaidTime(System.currentTimeMillis());
								order.setAmountSettle(Double.valueOf(amount));
								//修改订单支付结果													
								order.setStatus(1);																						
							}
							else{																		
								order.setStatus(2);												
							}															
						}						
					}
					
				}
				
				orderService.updateOrder(order);
				
				//启动后台发送异步通知消息任务							
				SingleNotifyTask snt=new SingleNotifyTask(order.getId());						
				SystemConstants.scheduledThreadPoolExecutor.schedule(snt,0,TimeUnit.SECONDS);
			}
		}
		return null;		
	}
	
	/**
     * <p>Description: 商酷的微信扫码支付异步通知處理方法</p>
     * @param request
     * @param response
     * @return
     */	
	@RequestMapping(value="apmp_weixin")	
	@ResponseBody
	public String apmp_weixin(HttpServletRequest request,HttpServletResponse response){							
		//获取异步通知的参数		
		String sign=(String)request.getParameter("sign");//签名
		String body=(String)request.getParameter("body");//签名								
		JSONObject payResponse = JSONObject.parseObject(body);		
		String odNo=payResponse.getString("odNo");//商户网站唯一订单号
		String amountStr=payResponse.getString("amount");//支付金額
		double amount=Double.valueOf(amountStr)/100.00;
		//String txnTime=payResponse.getString("txnTime");//日期时间（YYYYMMDDhhmmss）
		String txnId=payResponse.getString("txnId");//平台方唯一交易流水号
		if(!StringTool.isEmptyOrNull(odNo)){
			int orderid=Integer.parseInt(odNo);	
			Torder order=orderService.getOrderById(orderid);	
			order.setTransactionNo(txnId);
			order.setStatus(2);	//默认设置订单状态为失败	
			if(StringTool.isEmptyOrNull(order.getSign())){
				order.setSign(SecurityTools.GenerateUniqueText());								
			}				
			//if(order!=null&&order.getOrderAmount().doubleValue()==amount){
				Tapplication app=order.getApp();				
				Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_APMP_ALIPAY);
				TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());				
				if(appChannel!=null){	
					boolean verifyResult=false;
					try {
						verifyResult = QrcodePay.verify(body.getBytes("UTF-8"),sign);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}					
					if(verifyResult){						
						//支付交易结果為成功
						if("SUCCESS".equals(payResponse.getString("payResult"))){
							logger.error("===============================Receive Notified,apmp_weixin Payment success ");
							//修改订单为支付成功状态
							order.setPaidTime(System.currentTimeMillis());
							order.setAmountSettle(Double.valueOf(amount));
							//修改订单支付结果													
							order.setStatus(1);																						
						}
						else{																		
							order.setStatus(2);												
						}															
					}
				}
				
			//}
			
			orderService.updateOrder(order);
			
			//启动后台发送异步通知消息任务							
			SingleNotifyTask snt=new SingleNotifyTask(order.getId());						
			SystemConstants.scheduledThreadPoolExecutor.schedule(snt,0,TimeUnit.SECONDS);
		}
		return null;	
	}
	
}
