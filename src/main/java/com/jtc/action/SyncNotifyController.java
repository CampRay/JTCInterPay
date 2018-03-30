package com.jtc.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jtc.commons.HttpClientUtil;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.dto.Torder;
import com.jtc.koolyun.pay.QrcodePay;
import com.jtc.koolyun.reponse.PayQueryResponse;
import com.jtc.koolyun.request.PayQueryRequest;
import com.jtc.model.ChannelSetting;
import com.jtc.service.AdminAppsService;
import com.jtc.service.AdminUserService;
import com.jtc.service.AppChannelsService;
import com.jtc.service.ApplicationService;
import com.jtc.service.ChannelService;
import com.jtc.service.OrderService;


@Controller
@RequestMapping(value="syncnotify")
public class SyncNotifyController {
	private Logger logger = Logger.getLogger(SyncNotifyController.class);		

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
     * <p>Description: paypal_pc支付數據同步通知處理方法</p>
     * @param request
     * @param response
     * @param userAgent
     * @param tx
     * @return
     */
	@RequestMapping(value="paypal_pc")	
	public ModelAndView paypalPc(HttpServletRequest request,HttpServletResponse response,@RequestHeader ("User-Agent") String userAgent){			
		RequestContext requestContext = new RequestContext(request);
		//获取PayPal支付订单的业务ID
		String tx=(String)request.getParameter("tx");
		String custom=(String)request.getParameter("cm");
		String status=(String)request.getParameter("st");
		String amount=(String)request.getParameter("amt");
		String currencyCode=(String)request.getParameter("cc");
		String sig=(String)request.getParameter("sig");
		
		logger.debug("Sync===========content==tx:"+tx+" cm: "+custom+"  st:"+status+"  amt:"+amount+"  cc:"+currencyCode+"  sig:"+sig);
		
		if(StringTool.isEmptyOrNull(custom)){
			//mav.addObject("errorMsg", requestContext.getMessage("error.platform.appid.empty"));
			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
		}
		else{
			int orderid=Integer.parseInt(custom);	
			Torder order=orderService.getOrderById(orderid);				
			if(order==null||order.getOrderAmount().doubleValue()!=Double.valueOf(amount)||!order.getCurrencyCode().equalsIgnoreCase(currencyCode)){				
				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
			}
			else{
				Tapplication app=order.getApp();				
				Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_PAYPAL_PC);
				TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());				
				if(appChannel==null){					
					return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
				}
				else{
					ChannelSetting channelSetting=appChannel.getChannelSetting();
					Map<String, Object> formMap=new HashMap<String, Object>();
					formMap.put("cmd", "_notify-synch");
					formMap.put("at", channelSetting.getToken());
					formMap.put("tx", tx);
//					List<Header> headerList=new ArrayList<Header>();
//					Header header=new Header("User-Agent",userAgent);
//					headerList.add(header);
					Map<String,String> headerMap=new HashMap<String, String>();
					headerMap.put("Content-Type", "application/x-www-form-urlencoded");
					//headerMap.put("User-Agent",userAgent);					
					
					String responseStr="";
					try{
						//发送包涵PayPal传来的业务编码tx和PayPal的PDT Token参数到PayPal，以便获取支付结果
						responseStr=HttpClientUtil.sendPost(getPaypalUrl(channelSetting.getSandbox()), "UTF-8",headerMap, formMap);																						   						
					}
					catch(MyException me){						
						return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.get");						
					}
					
					
					Map<String,String> values=new HashMap<String,String>();
					boolean firstLine = true, success = false;
					try {
						responseStr=URLDecoder.decode(responseStr,order.getCharset());							
					} catch (UnsupportedEncodingException e) {													
					}
					String[] respArr= responseStr.split(System.getProperty("line.separator"));
					for (String str : respArr) {											
		                String line= str.trim();																
		                if (firstLine)
		                {
		                    success = line.equalsIgnoreCase("SUCCESS");
		                    firstLine = false;
		                }
		                else
		                {
		                    int equalPox = line.indexOf('=');
		                    if (equalPox >= 0)
		                        values.put(line.substring(0, equalPox), line.substring(equalPox + 1));
		                }							
		            }
					
					//如果获取支付结果信息成功
					if(success){
						double mc_gross=Double.parseDouble(values.get("mc_gross"));//订单总金额
						String mc_currency=values.get("mc_currency");//货币代码
						String txn_id=values.get("txn_id");//PayPal的业务编号
						custom=values.get("custom");
						String payment_status=values.get("payment_status");
						//String receiver_email=values.get("receiver_email");
						//验证支付金额和订单金额是否相同
						if(!String.valueOf(order.getId()).equals(custom)||mc_gross!=order.getOrderAmount().doubleValue()||!mc_currency.equalsIgnoreCase(order.getCurrencyCode())){

							return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
						}							
													
						//处理订单支付状态
						int payStatus=0;
						String payStatusStr="";
						if("pending".equalsIgnoreCase(payment_status)){
							payStatus=0;
							payStatusStr="pending";
						}
						else if("processed".equalsIgnoreCase(payment_status)||"completed".equalsIgnoreCase(payment_status)||"canceled_reversal".equalsIgnoreCase(payment_status)){
							payStatus=1;
							payStatusStr="completed";
							order.setPaidTime(System.currentTimeMillis());
							order.setAmountSettle(mc_gross);
						}
						else if("denied".equalsIgnoreCase(payment_status)||"expired".equalsIgnoreCase(payment_status)||"failed".equalsIgnoreCase(payment_status)||"voided".equalsIgnoreCase(payment_status)){
							payStatus=2;
							payStatusStr="failed";
							order.setFailureCode(payment_status);
						}
						else if("refunded".equalsIgnoreCase(payment_status)||"reversed".equalsIgnoreCase(payment_status)){
							payStatus=3;
							payStatusStr="refunded";
							order.setRefundedTime(System.currentTimeMillis());
							order.setAmountRefunded(mc_gross);
						}
						else{
							payStatus=0;
						}
						//修改订单支付结果							
						order.setTransactionNo(txn_id);
						order.setStatus(payStatus);	
						if(StringTool.isEmptyOrNull(order.getSign())){
							//生成唯一的签名字符串,客户应用回发查询支付结果check指令时，必须设置sign参数值为此值
							order.setSign(SecurityTools.GenerateUniqueText());								
						}
						orderService.updateOrder(order);
						if(!StringTool.isEmptyOrNull(order.getReturnUrl())){							
							ModelMap model=new ModelMap();
							model.addAttribute("txn_id", order.getId());			    	    	
			    	    	model.addAttribute("order_id", order.getOrderNo());
			    	    	model.addAttribute("order_amount", order.getAmountSettle());
			    	    	model.addAttribute("currency_code", order.getCurrencyCode());
			    	    	model.addAttribute("country_code", order.getCountryCode());
			    	    	model.addAttribute("status", SystemConstants.PAYMENT_STATUS.get(order.getStatus()));
			    	    	model.addAttribute("sign", order.getSign());
			    	    	model.addAttribute("custom", order.getCustom());
			    	    	model.addAttribute("paidTime", order.getPaidTime());			    	    	
							//跳转回应用的支付结果页面
							return new ModelAndView(new RedirectView(order.getReturnUrl(),false,false,true),model);
						}
						else{
							//直接在平臺頁面顯示支付成功
							return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", requestContext.getMessage("error.platform.syncnotify.success"));
						}
																								
					}
					else{
						return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.get");
					}																						
					
				}												
			}																					
		}
		
	}
	
	
	/**
     * <p>Description: alipay_pc支付數據同步通知處理方法</p>
     * @param request
     * @param response
     * @param userAgent
     * @param tx
     * @return
     */
	@RequestMapping(value="alipay_pc")	
	public ModelAndView alipayPc(HttpServletRequest request,HttpServletResponse response,@RequestHeader ("User-Agent") String userAgent){			
		//获取支付宝支付订单的业务ID
		String appId=(String)request.getParameter("app_id");//支付宝分配给开发者的应用ID
		String method=(String)request.getParameter("method");//接口名称
		String charset=(String)request.getParameter("charset");//编码格式
		String signType=(String)request.getParameter("sign_type");//签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
		String sign=(String)request.getParameter("sign");//支付宝对本次支付结果的签名，开发者必须使用支付宝公钥验证签名
		String amount=(String)request.getParameter("total_amount");//该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
		String tno=(String)request.getParameter("trade_no");//该交易在支付宝系统中的交易流水号。最长64位。
		String orderId=(String)request.getParameter("out_trade_no");//商户网站唯一订单号
			
		
		if(StringTool.isEmptyOrNull(orderId)){
			//mav.addObject("errorMsg", requestContext.getMessage("error.platform.appid.empty"));
			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
		}
		else{
			
			int orderid=Integer.parseInt(orderId);	
			Torder order=orderService.getOrderById(orderid);				
			if(order==null||order.getOrderAmount().doubleValue()!=Double.valueOf(amount)){				
				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
			}
			else{
				Tapplication app=order.getApp();				
				Tchannel channel=channelService.loadChannelByCode(SystemConstants.CHANNEL_CODE_PAYPAL_PC);
				TappChannels appChannel=appChannelsService.getAppChannel(app.getId(), channel.getId());				
				if(appChannel==null){					
					return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
				}
				else{
					ChannelSetting channelSetting=appChannel.getChannelSetting();						
					
					//调用接口查询交易结果状态
					AlipayClient alipayClient = new DefaultAlipayClient(getAlipayUrl(channelSetting.getSandbox()),appId,channelSetting.getPrivateKey(),"json",charset,channelSetting.getPublicKey(),"RSA2");
					AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
					queryRequest.setBizContent("{" +
					"    \"trade_no\":\""+tno+"\"," +
					"    \"out_trade_no\":\""+orderId+"\"" +					
					"  }");
					AlipayTradeQueryResponse queryResponse;
					try {
						queryResponse = alipayClient.execute(queryRequest);
						//调用成功
						if(queryResponse.isSuccess()){							
							String resultStatus=queryResponse.getTradeStatus();
							//如果返回的支付结果状态码是支付成功或者是交易结束不可退款
							if("TRADE_SUCCESS".equalsIgnoreCase(resultStatus)||"TRADE_FINISHED".equalsIgnoreCase(resultStatus)){
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
							}
						}
						
					} catch (AlipayApiException e) {}
					
					//如果订单有同步返回通知URL，则返回结果到调用此支付平台的网站										
					if(!StringTool.isEmptyOrNull(order.getReturnUrl())){							
						ModelMap model=new ModelMap();
						model.addAttribute("txn_id", order.getId());			    	    	
		    	    	model.addAttribute("order_id", order.getOrderNo());
		    	    	model.addAttribute("order_amount", order.getAmountSettle());
		    	    	model.addAttribute("currency_code", order.getCurrencyCode());
		    	    	model.addAttribute("country_code", order.getCountryCode());
		    	    	model.addAttribute("status", SystemConstants.PAYMENT_STATUS.get(order.getStatus()));
		    	    	model.addAttribute("sign", order.getSign());
		    	    	model.addAttribute("custom", order.getCustom());
		    	    	model.addAttribute("paidTime", order.getPaidTime());			    	    	
						//跳转回应用的支付结果页面
						return new ModelAndView(new RedirectView(order.getReturnUrl(),false,false,true),model);
					}
					else{
						RequestContext requestContext = new RequestContext(request);
						//直接在平臺頁面顯示支付成功
						return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", requestContext.getMessage("error.platform.syncnotify.success"));
					}
																										
					
				}												
			}											
		}
	}
	
	
	/**
     * <p>Description: 商酷支付數據同步通知處理方法(二维码页面Ajax轮询调用)</p>
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value="apmp",method=RequestMethod.POST)
	@ResponseBody
	public String apmp(HttpServletRequest request,HttpServletResponse response){	
		JSONObject respJson = new JSONObject();
		respJson.put("status", false);
		String txnId=(String)request.getParameter("txnId");
		String orderId=(String)request.getParameter("orderId");
		String batchNo=(String)request.getParameter("batchNo");
		String traceNo=(String)request.getParameter("traceNo");
		String transType=(String)request.getParameter("transType");
		String apiUrl=(String)request.getParameter("apiUrl");
							
		if(!StringTool.isEmptyOrNull(orderId)){			
			int orderid=Integer.parseInt(orderId);	
			Torder order=orderService.getOrderById(orderid);				
			if(order!=null){				
				TappChannels appChannel=appChannelsService.getAppChannel(order.getApp().getId(), order.getChannel().getId());												
				if(appChannel!=null){										
					ChannelSetting channelSetting=appChannel.getChannelSetting();						
					PayQueryRequest queryRequest = new PayQueryRequest();
					queryRequest.setAction("msc/txn/payQuery");		
					//平台方唯一交易流水号（退款与撤销订单传入退款、撤销订单流水号而非原订单流水号）
					queryRequest.setTxnId(txnId);
					//如发起交易后无返回（即获取不到txnId），则用批次号、流水号、订单号组合交易结果
					queryRequest.setBatchNo(batchNo);
					queryRequest.setTraceNo(traceNo);
					queryRequest.setOdNo(orderId);
					queryRequest.setTransType(transType);
					//String apiUrl=channelSetting.getSandbox() ? "http://aop.koolyun.cn:8080/apmp/rest/":"https://aop.koolyun.cn:443/apmp/rest/";
				    PayQueryResponse qureryResponse = QrcodePay.Query(queryRequest,apiUrl,channelSetting.getPublicKey(),channelSetting.getPrivateKey(),channelSetting.getLoginId());
				    if("0".equalsIgnoreCase(qureryResponse.getResponseCode())){
        				if("0".equalsIgnoreCase(qureryResponse.getStatusCode())){
        					if(order.getStatus()!=1){
	        					//修改订单为支付成功状态
	    						order.setPaidTime(System.currentTimeMillis());
	    						order.setAmountSettle(order.getOrderAmount());
	        					//保存商酷支付接口返回的交易流水ID，以后用于查询结果
	        					order.setTransactionNo(qureryResponse.getTxnId());
	        					order.setStatus(1);
	        					if(StringTool.isEmptyOrNull(order.getSign())){
									order.setSign(SecurityTools.GenerateUniqueText());								
								}
	        					orderService.updateOrder(order);
        					}		
        					respJson.put("status", true);	        					
            			}	        				
        			}
																																				
				}				
			}											
		}
		return JSON.toJSONString(respJson);
	}
	
	
	/**
     * <p>Description: 商酷支付數據同步通知處理方法</p>
     * @param request
     * @param response
     * @param userAgent
     * @param tx
     * @return
     */
	@RequestMapping(value="apmp_return",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView apmp_return(HttpServletRequest request,HttpServletResponse response,int orderId){	
		RequestContext requestContext = new RequestContext(request);								
		if(orderId==0){
			//mav.addObject("errorMsg", requestContext.getMessage("error.platform.appid.empty"));
			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
		}
		else{						
			Torder order=orderService.getOrderById(orderId);				
			if(order==null){				
				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.syncnotify.unmatched");
			}
			else{
				//如果订单有同步返回通知URL，则返回结果到调用此支付平台的网站										
				if(!StringTool.isEmptyOrNull(order.getReturnUrl())){							
					ModelMap model=new ModelMap();
					model.addAttribute("txn_id", order.getId());					
	    	    	model.addAttribute("order_id", order.getOrderNo());
	    	    	model.addAttribute("order_amount", order.getAmountSettle());
	    	    	model.addAttribute("currency_code", order.getCurrencyCode());
	    	    	model.addAttribute("country_code", order.getCountryCode());
	    	    	model.addAttribute("status", SystemConstants.PAYMENT_STATUS.get(order.getStatus()));
	    	    	model.addAttribute("sign", order.getSign());
	    	    	model.addAttribute("custom", order.getCustom());
	    	    	model.addAttribute("paidTime", order.getPaidTime());			    	    	
					//跳转回应用的支付结果页面
					return new ModelAndView(new RedirectView(order.getReturnUrl(),false,false,true),model);					
				}
				else{
					//直接在平臺頁面顯示支付成功
					return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", requestContext.getMessage("error.platform.syncnotify.success"));
				}											
			}											
		}		
	}
	
}
