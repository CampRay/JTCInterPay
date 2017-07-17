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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;

import com.jtc.commons.HttpClientUtil;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.dto.Torder;
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
				Tchannel channel=channelService.loadChannelByCode(CHANNEL_CODE);
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
						responseStr=HttpClientUtil.sendPost(GetPaypalUrl(channelSetting.getSandbox()), "UTF-8",headerMap, formMap);																						   						
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
	
	
}
