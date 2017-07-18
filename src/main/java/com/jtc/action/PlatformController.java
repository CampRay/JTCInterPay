/**   
* @Title: LoginController.java 
* @Package com.uswop.action 
*
* @Description: TODO
* 
* @date Sep 10, 2014 3:06:32 PM
* @version V1.0   
*/ 
package com.jtc.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;

import com.jtc.commons.IpUtils;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConfig;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.dto.Torder;
import com.jtc.model.CheckoutModel;
import com.jtc.service.AppChannelsService;
import com.jtc.service.ApplicationService;
import com.jtc.service.ChannelService;
import com.jtc.service.OrderService;

/** 
 * @ClassName: PlatformController 
 * @Description: 
 * @author Phills Li
 * @date Sep 10, 2017 3:06:32 PM 
 *  
 */
@Controller
@RequestMapping(value="platform")
public class PlatformController extends BaseController {		
	private Logger logger = Logger.getLogger(PlatformController.class);
	@Resource
	private ChannelService channelService;
	
	@Resource
	private ApplicationService applicationService;
	
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
	 * <p>JTC支付平臺頁面</p>
	 * @Title: platform 
	 * @return ModelAndView
	 * @throws 
	 */     
    @RequestMapping(value="/result")
	public ModelAndView result(HttpServletRequest request) {
    	String msgCode=(String)request.getParameter("msgCode");    	
    	ModelAndView mav=new ModelAndView();
		mav.setViewName("platform/result");
		RequestContext requestContext = new RequestContext(request);
		mav.addObject("msg",requestContext.getMessage(msgCode));    													
		return mav;
	}
	   
	/** 
	 * <p>JTC支付平臺web接口處理方法</p>
	 * @Title: platform 
	 * @return ModelAndView
	 * @throws 
	 */     
    @RequestMapping(value="/webapi",method=RequestMethod.POST)
	public ModelAndView webapi(HttpServletRequest request,HttpServletResponse response,CheckoutModel checkoutModel) {
    	
    	String cmd=checkoutModel.getCmd();
    	if(StringTool.isEmptyOrNull(cmd)){
    		return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");
    	}
    					    	    	
    	//處理select命令
    	if("select".equalsIgnoreCase(cmd)){
    		//驗證參數
        	String currencyCode=checkoutModel.getCurrency_code();    	
        	if(StringTool.isEmptyOrNull(currencyCode)){
        		currencyCode="HKD";	
        		Locale locale=new Locale("zh","HK");
    			request.getSession().setAttribute("locale", locale);
        	}
        	else{
        		if("CNY".equals(currencyCode)){
        			Locale locale=new Locale("zh","CN");
        			request.getSession().setAttribute("locale", locale);
        		} 
        		else if("USD".equals(currencyCode)){
        			Locale locale=new Locale("en","US");     	
        			request.getSession().setAttribute("locale", locale);
        		} 
        		else {
        			if(SystemConfig.Currency_Map.containsKey(currencyCode)){
    	    			Locale locale=new Locale("zh","HK");
    	    			request.getSession().setAttribute("locale", locale);
        			}
        			else{
        				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");	    				
        			}
        		}
        	}
    				
        	Tapplication app=null;
    		if(StringTool.isEmptyOrNull(checkoutModel.getAppid())){
    			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");	 
    		}
    		else{
    			app=applicationService.getAppByAppId(checkoutModel.getAppid());			
    			if(app==null){
    				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");	 
    			}				
    		}
    		
    		
    		ModelAndView mav=new ModelAndView();
    		mav.setViewName("platform/select");	
			
			List<TappChannels> appChannelsList=appChannelsService.getAllChannelsByAppId(app.getId());
			
			Torder order=new Torder();
			
			order.setApp(app);
			order.setCurrencyCode(currencyCode);
			String countryCode=checkoutModel.getCountry_code();
	    	if(StringTool.isEmptyOrNull(countryCode)){
	    		countryCode="HK";	    		
	    	}
			order.setCountryCode(countryCode);
			order.setOrderNo(checkoutModel.getOrder_id());
			order.setOrderAmount(checkoutModel.getOrder_amount());
			order.setOrderTitle(checkoutModel.getOrder_title());
			if(StringTool.isEmptyOrNull(checkoutModel.getOrder_title())){
				order.setOrderTitle("Order "+checkoutModel.getOrder_id());		
	    	}
			
			order.setOrderDesc(checkoutModel.getOrder_desc());
			order.setCustom(checkoutModel.getCustom());			
			order.setClientIp(IpUtils.getIpAddr(request));
			order.setCharset(checkoutModel.getCharset());
			if(!StringTool.isEmptyOrNull(checkoutModel.getReturn_url())){
				order.setReturnUrl(checkoutModel.getReturn_url());
			}
			else if(!StringTool.isEmptyOrNull(app.getReturnUrl())){
				order.setReturnUrl(app.getReturnUrl());
			}
			
			if(!StringTool.isEmptyOrNull(checkoutModel.getNotify_url())){
				order.setNotifyUrl(checkoutModel.getNotify_url());
			}
			else if(!StringTool.isEmptyOrNull(app.getNotifyUrl())){
				order.setNotifyUrl(app.getNotifyUrl());
			}
			
			mav.addObject("appChannelsList", appChannelsList);
			mav.addObject("order", order);
			mav.addObject("symbols", SystemConfig.Currency_Map.get(currencyCode));	
			return mav;
    	}
    	
    	//處理payment命令
    	if("payment".equalsIgnoreCase(cmd)){
    		String currencyCode=checkoutModel.getCurrency_code();
    		String channelCode=checkoutModel.getChannel_code();
    		if(StringTool.isEmptyOrNull(channelCode)){
    			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");
    		}    		
    		Tchannel channel=channelService.loadChannelByCode(channelCode);
    		if(channel==null){
    			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");
    		}
    		    		
    		ModelMap model=new ModelMap();
	    		    
    		Tapplication app=null;
    		if(StringTool.isEmptyOrNull(checkoutModel.getAppid())){
    			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");	 
    		}
    		else{
    			app=applicationService.getAppByAppId(checkoutModel.getAppid());			
    			if(app==null){
    				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");	 
    			}				
    		}
    		
    		Torder order=new Torder();			
    		model.addAttribute("app.id", app.getId());
	    	model.addAttribute("channel.id", channel.getId());
	    	model.addAttribute("currencyCode", currencyCode);
			String countryCode=checkoutModel.getCountry_code();
	    	if(StringTool.isEmptyOrNull(countryCode)){
	    		countryCode="HK";	    		
	    	}
	    	model.addAttribute("countryCode", countryCode);
	    	
	    	model.addAttribute("orderNo", checkoutModel.getOrder_id());
	    	model.addAttribute("orderAmount", checkoutModel.getOrder_amount());
	    	model.addAttribute("orderTitle", checkoutModel.getOrder_title());
	    	model.addAttribute("orderDesc", checkoutModel.getOrder_desc());
	    	model.addAttribute("custom", checkoutModel.getCustom());
	    	model.addAttribute("clientIp", IpUtils.getIpAddr(request));
	    	model.addAttribute("charset", checkoutModel.getCharset());	    		    	    
	    				
			if(!StringTool.isEmptyOrNull(checkoutModel.getReturn_url())){
				model.addAttribute("returnUrl", checkoutModel.getReturn_url());
			}
			else if(!StringTool.isEmptyOrNull(app.getReturnUrl())){
				model.addAttribute("returnUrl", app.getReturnUrl());				
			}
			
			if(!StringTool.isEmptyOrNull(checkoutModel.getNotify_url())){
				model.addAttribute("notifyUrl", checkoutModel.getNotify_url());	
			}
			else if(!StringTool.isEmptyOrNull(app.getNotifyUrl())){
				model.addAttribute("notifyUrl", app.getNotifyUrl());					
			}
			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/pay",true,false,true),model);
    	}
    	
    	//處理result命令,向應用發送同步結果通知後，應用發送的結果查詢請求
    	if("check".equalsIgnoreCase(cmd)){
    		logger.info("=============================webapi: "+checkoutModel.getCmd());    		  	
    		String txnIdStr=checkoutModel.getTxn_id();
    		String token=checkoutModel.getToken();
    		if(StringTool.isEmptyOrNull(txnIdStr)||StringTool.isEmptyOrNull(token)){    			
    			return null;
    		}
    		
    		try{
	    		int txnId=Integer.parseInt(txnIdStr);	    		
	    		Torder dborder=orderService.getOrderById(txnId);
	    		if(dborder.getSign().equalsIgnoreCase(checkoutModel.getSign())&&dborder.getApp().getToken().equalsIgnoreCase(checkoutModel.getToken())){
	    			dborder.setNotified(true);
	    			orderService.updateOrder(dborder);
	    			response.setStatus(HttpStatus.OK.value());
	    			try {	    				
						response.getWriter().write("success");
					} catch (IOException e) {}
	    		}
	    		
    		}
    		catch(Exception ne){}
    		return null;
    	}
    	
    	//處理checkt命令,向應用發送異步結果通知後，應用發送的結果查詢請求
//    	if("check".equalsIgnoreCase(cmd)){
//    		
//    	}
		
    	return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");
	}
		
    /**
     * @throws IOException  
	 * <p>JTC支付平臺頁面</p>
	 * @Title: platform 
	 * @return ModelAndView
	 * @throws 
	 */     
    @RequestMapping(value="/pay",method=RequestMethod.POST)
	public ModelAndView pay(HttpServletRequest request,HttpServletResponse response,Torder order) throws Exception {    	    	   
    	TappChannels appChannel=appChannelsService.getAppChannel(order.getApp().getId(), order.getChannel().getId());    	    	    	
    	if(appChannel==null){
    		return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/select"));
    	}
    	else{    		
    		order.setApp(appChannel.getApp());
    		order.setChannel(appChannel.getChannel());
    		order.setCreatedTime(System.currentTimeMillis());
        	orderService.createOrder(order);
        	//如果選擇的是PayPal支付渠道
        	if("paypal_pc".equalsIgnoreCase(appChannel.getChannel().getCode())){
    	    	
    	    	ModelMap model=new ModelMap();
    	    	model.addAttribute("cmd", "_xclick");
    	    	model.addAttribute("business", appChannel.getChannelSetting().getBusinessEmail());
    	    	if(!StringTool.isEmptyOrNull(order.getOrderTitle())){
    	    		model.addAttribute("item_name", order.getOrderTitle());
    	    	}
    	    	model.addAttribute("amount", order.getOrderAmount());
    	    	model.addAttribute("custom", order.getId());
    	    	model.addAttribute("charset", order.getCharset());
    	    	model.addAttribute("no_note", 1);
    	    	model.addAttribute("currency_code", order.getCurrencyCode());
    	    	model.addAttribute("rm", 2);
//    	    	String rootPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    	    	
//    	    	model.addAttribute("return", URLEncoder.encode(rootPath+"/syncnotify/"+appChannel.getChannel().getCode(),order.getCharset()));
//    	    	model.addAttribute("notify_url", URLEncoder.encode(rootPath+"/asyncnotify/"+appChannel.getChannel().getCode(),order.getCharset()));
    	    	//POST方式提交
    	    	return new ModelAndView(new RedirectView(GetPaypalUrl(appChannel.getChannelSetting().getSandbox()),false,false,true),model);
        	}
        	else{
        		return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/select"));
        	}
	    	
    	}
    	    	    	
	}
        
}
