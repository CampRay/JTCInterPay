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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.persistence.Convert;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.jtc.commons.IpUtils;
import com.jtc.commons.StringTool;
import com.jtc.commons.SystemConfig;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.dto.Torder;
import com.jtc.koolyun.pay.QrcodePay;
import com.jtc.koolyun.reponse.CSBPayResponse;
import com.jtc.koolyun.reponse.LoginResponse;
import com.jtc.koolyun.request.CSBPayRequest;
import com.jtc.koolyun.request.LoginRequest;
import com.jtc.model.CheckoutModel;
import com.jtc.service.AppChannelsService;
import com.jtc.service.ApplicationService;
import com.jtc.service.ChannelService;
import com.jtc.service.OrderService;

import sun.misc.BASE64Encoder;

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
        return isSandbox ? "https://www.sandbox.paypal.com/us/cgi-bin/webscr":
            "https://www.paypal.com/us/cgi-bin/webscr";
    }
	
	private String GetAlipayUrl(boolean isSandbox)
    {
        return isSandbox ? "https://openapi.alipaydev.com/gateway.do":
            "https://openapi.alipay.com/gateway.do";
    }
	//商酷海外扫码支付
	private String GetApmpUrl(boolean isSandbox)
    {
        return isSandbox ? "http://aop.koolyun.cn:8080/apmp/rest":
            "https://aop.koolyun.com:443/apmp/rs/";
    }
	
	/** 
	 * <p>JTC支付平臺结果消息显示頁面</p>
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
	 * <p>JTC支付平臺web接口處理方法,可处理多种请求指令。如：select, payment, check</p>
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
    					    	    	
    	//處理select支付方式命令
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
			
			List<TappChannels> appChannelsList=appChannelsService.getEnableChannelsByAppId(app.getId());
			
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
			//支持货币列表
			mav.addObject("symbols", SystemConfig.Currency_Map.get(currencyCode));	
			return mav;
    	}
    	
    	//處理开始payment命令
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
    	
    	//處理检测支付结果请求命令,当平台向指定應用發送同步結果通知後，應用会请求此check接口,請求查詢支付結果
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
	    		//如果查询支付结果请求的sign参数的值和平台发出支付结果通知消息时生成的sign相同，并且参数token的值也和客户应用的token相同，则表明消息是安全的
	    		if(dborder.getSign().equalsIgnoreCase(checkoutModel.getSign())&&dborder.getApp().getToken().equalsIgnoreCase(checkoutModel.getToken())){
	    			//修改数据库订单表记录的notified的值为true，表示异步通知已完成，不需要再执行异步通知任务
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
    	
    	return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", "error.platform.appid.empty");
	}
		
    /**
     * @throws IOException  
	 * <p>JTC支付平臺确认支付后处理页面，会对应跳转到用户选择的支付网关</p>
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
        	//如果選擇的是PayPal电脑网页支付渠道
        	if(SystemConstants.CHANNEL_CODE_PAYPAL_PC.equalsIgnoreCase(appChannel.getChannel().getCode())){
    	    	
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
        	//如果選擇的是Alipay电脑网页支付渠道
        	else if(SystemConstants.CHANNEL_CODE_ALIPAY_PC.equalsIgnoreCase(appChannel.getChannel().getCode())){
        		String apiUrl=GetAlipayUrl(appChannel.getChannelSetting().getSandbox());
        		String appId=appChannel.getChannelSetting().getAppId();
        		String publicKey=appChannel.getChannelSetting().getPublicKey();
        		String privateKey=appChannel.getChannelSetting().getPrivateKey();
        		String returnUrl=appChannel.getChannelSetting().getReturnURL();
        		String ipnUrl=appChannel.getChannelSetting().getIpnURL();
        		String orderTitle="";
        		if(!StringTool.isEmptyOrNull(order.getOrderTitle())){
        			orderTitle=order.getOrderTitle();
    	    	}
        		
        		//建立支付宝SDK客户端
        		AlipayClient alipayClient = new DefaultAlipayClient(apiUrl,appId,privateKey,"json","utf-8",publicKey,"RSA2");
        		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request        		
        	    alipayRequest.setReturnUrl(returnUrl);
        	    alipayRequest.setNotifyUrl(ipnUrl);//在公共参数中设置异步回跳和通知地址        	   
        	    alipayRequest.setBizContent("{" +
        	        "    \"out_trade_no\":\""+order.getId()+"\"," +
        	        "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
        	        "    \"total_amount\":"+order.getOrderAmount()+"," +
        	        "    \"subject\":\""+orderTitle+"\"," +
        	        "    \"body\":\""+order.getOrderDesc()+"\"," +//订单描述
        	        "    \"currency\":\""+order.getCurrencyCode()+"\"" +  //货币类型，接口文档中没有此参数（此为测试是否能海外支付）      	        
        	        "  }");//填充业务参数
        	    String form="";
        	    try {
        	        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        	    } catch (AlipayApiException e) {
        	        e.printStackTrace();
        	    }
        	    response.setContentType("text/html;charset=utf-8");
        	    response.getWriter().write(form);//直接将完整的表单html输出到页面
        	    response.getWriter().flush();
        	    response.getWriter().close();
        	    return null;
        	}
        	//如果選擇的是商酷海外支付宝扫码支付渠道
        	else if(SystemConstants.CHANNEL_CODE_APMP_ALIPAY.equalsIgnoreCase(appChannel.getChannel().getCode())){
        		String apiUrl=GetApmpUrl(appChannel.getChannelSetting().getSandbox());
        		String merchId=appChannel.getChannelSetting().getAppId();
        		String operator=appChannel.getChannelSetting().getLoginId();
        		String password=appChannel.getChannelSetting().getLoginPassword();
        		String publicKey=appChannel.getChannelSetting().getPublicKey();
        		String privateKey=appChannel.getChannelSetting().getPrivateKey();
        		
        		String ipnUrl=appChannel.getChannelSetting().getIpnURL();
        		String orderTitle="";
        		if(!StringTool.isEmptyOrNull(order.getOrderTitle())){
        			orderTitle=order.getOrderTitle();
    	    	}
        		
        		//定义商酷登录接口请示对象参数
        		LoginRequest loginRequest = new LoginRequest();
        		loginRequest.setAction("msc/user/login");
        		loginRequest.setOperator(operator);
        		loginRequest.setPwd(password);
        		loginRequest.setIposSn("0000000000000000");
        		loginRequest.setMerchId(merchId);
        		loginRequest.setV("3.0");
        		logger.info("====================APMP_ALIPAY Login Begin=========");
        		//调用登录接口
        		LoginResponse loginResponse = QrcodePay.login(loginRequest,apiUrl,publicKey,privateKey);
        		logger.info("====================APMP_ALIPAY Login End=========");
        		if("0".equalsIgnoreCase(loginResponse.getResponseCode())){
        			//调用支付接口
        			CSBPayRequest csbPayrequest = new CSBPayRequest();
        			csbPayrequest.setAction("msc/txn/request");
        			csbPayrequest.setTransType(QrcodePay.TRANS_TYPE_PAY);
        			//csbPayrequest.setPaymentId("0000000113");
        			csbPayrequest.setPaymentId(QrcodePay.PAYMENTID_ALIPAY2);
        			csbPayrequest.setBatchNo(QrcodePay.getBatchNo());
        			csbPayrequest.setTransAmount((int)(order.getOrderAmount()*100)+"");
        			csbPayrequest.setTraceNo(QrcodePay.getTraceNo());
        			csbPayrequest.setTransTime(QrcodePay.getTransTime());
        			csbPayrequest.setOdNo(order.getId()+"");
        			csbPayrequest.setPayType(QrcodePay.PAYTYPE_ALIPAY);
        			//csbPayrequest.setPayType(QrcodePay.PAYTYPE_WEIXIN);
        			csbPayrequest.setDataAction(QrcodePay.ACTION_CSB);
        			csbPayrequest.setNotifyUrl(ipnUrl);        			
        			if("CNY".equals(order.getCurrencyCode())){
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_CNY);
        			}
        			else if("SGD".equals(order.getCurrencyCode())){
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_SGD);
        			}
        			else{
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_HKD);
        			}
        			
        			logger.info("====================APMP_ALIPAY Pay Begin=========");
        			CSBPayResponse csbPayresponse = QrcodePay.CSBPay(csbPayrequest,apiUrl,publicKey,privateKey,merchId);
        			logger.info("====================APMP_ALIPAY Pay End=========");
        			if("0".equalsIgnoreCase(csbPayresponse.getResponseCode())){
        				if("SUCCESS".equalsIgnoreCase(csbPayresponse.getQrcodeResult())){        					
        					//保存商酷支付接口返回的交易流水ID，以后用于查询结果
        					order.setTransactionNo(csbPayresponse.getTxnId());
        					orderService.updateOrder(order);
        					
        					//获取返回的支付二维码
        					String qrCodeUrl=csbPayresponse.getQrcode();
        					BufferedImage bufImage=QrcodePay.encodeAsBitmap(qrCodeUrl,200);
        					ByteArrayOutputStream bos=new ByteArrayOutputStream();
        					ImageIO.write(bufImage, "png", bos);
        					        					
        					ModelAndView mav=new ModelAndView();
        					mav.setViewName("platform/qrcode"); 
        					mav.addObject("amount",order.getOrderAmount()); 
        					mav.addObject("channel",SystemConstants.CHANNEL_CODE_APMP_ALIPAY);
        					mav.addObject("txnId",order.getTransactionNo()); 
        					mav.addObject("orderId",csbPayrequest.getOdNo()); 
        					mav.addObject("batchNo",csbPayrequest.getBatchNo()); 
        					mav.addObject("traceNo",csbPayrequest.getTraceNo());
        					mav.addObject("transType",QrcodePay.TRANS_TYPE_PAY); 
        					mav.addObject("apiUrl",apiUrl); 
        					
        					mav.addObject("qrcode",new BASE64Encoder().encode(bos.toByteArray()));  
        					return mav;
            			}
            			else{
            				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", csbPayresponse.getReultMsg()==null?"获取支付二维码失败!":csbPayresponse.getReultMsg());
            			}
        			}
        			else{
        				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", csbPayresponse.getRequestResultMsg()==null?csbPayresponse.getErrorMsg():csbPayresponse.getRequestResultMsg());
        			}
        			
        			
        		}
        		else{
        			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", loginResponse.getRequestResultMsg()==null?loginResponse.getErrorMsg():loginResponse.getRequestResultMsg());
        		}
        		        	    
        	}
        	//如果選擇的是商酷海外微信扫码支付渠道
        	else if(SystemConstants.CHANNEL_CODE_APMP_WEIXIN.equalsIgnoreCase(appChannel.getChannel().getCode())){
        		String apiUrl=GetApmpUrl(appChannel.getChannelSetting().getSandbox());
        		String merchId=appChannel.getChannelSetting().getAppId();
        		String operator=appChannel.getChannelSetting().getLoginId();
        		String password=appChannel.getChannelSetting().getLoginPassword();
        		String publicKey=appChannel.getChannelSetting().getPublicKey();
        		String privateKey=appChannel.getChannelSetting().getPrivateKey();
        		
        		String ipnUrl=appChannel.getChannelSetting().getIpnURL();
        		String orderTitle="";
        		if(!StringTool.isEmptyOrNull(order.getOrderTitle())){
        			orderTitle=order.getOrderTitle();
    	    	}
        		
        		//定义商酷登录接口请示对象参数
        		LoginRequest loginRequest = new LoginRequest();
        		loginRequest.setAction("msc/user/login");
        		loginRequest.setOperator(operator);
        		loginRequest.setPwd(password);
        		loginRequest.setIposSn("0000000000000000");
        		loginRequest.setMerchId(merchId);
        		loginRequest.setV("3.0");
        		//调用登录接口
        		LoginResponse loginResponse = QrcodePay.login(loginRequest,apiUrl,publicKey,privateKey);
        		if("0".equalsIgnoreCase(loginResponse.getResponseCode())){
        			//调用支付接口
        			CSBPayRequest csbPayrequest = new CSBPayRequest();
        			csbPayrequest.setAction("msc/txn/request");
        			csbPayrequest.setTransType(QrcodePay.TRANS_TYPE_PAY);
        			csbPayrequest.setPaymentId(QrcodePay.PAYMENTID_WEIXIN);
        			csbPayrequest.setBatchNo(QrcodePay.getBatchNo());
        			csbPayrequest.setTransAmount((int)(order.getOrderAmount()*100)+"");
        			csbPayrequest.setTraceNo(QrcodePay.getTraceNo());
        			csbPayrequest.setTransTime(QrcodePay.getTransTime());
        			csbPayrequest.setOdNo(order.getId()+"");
        			csbPayrequest.setPayType(QrcodePay.PAYTYPE_WEIXIN);
        			csbPayrequest.setDataAction(QrcodePay.ACTION_CSB);
        			csbPayrequest.setNotifyUrl(ipnUrl);
        			if("CNY".equals(order.getCurrencyCode())){
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_CNY);
        			}
        			else if("SGD".equals(order.getCurrencyCode())){
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_SGD);
        			}
        			else{
        				csbPayrequest.setCurrency(QrcodePay.CURRENCY_HKD);
        			}        			        			
        			
        			CSBPayResponse csbPayresponse = QrcodePay.CSBPay(csbPayrequest,apiUrl,publicKey,privateKey,merchId);        			
        			if("0".equalsIgnoreCase(csbPayresponse.getResponseCode())){
        				if("SUCCESS".equalsIgnoreCase(csbPayresponse.getQrcodeResult())){
        					//保存商酷支付接口返回的交易流水ID，以后用于查询结果
        					order.setTransactionNo(csbPayresponse.getTxnId());
        					orderService.updateOrder(order);
        					
        					//获取返回的支付二维码
        					String qrCodeUrl=csbPayresponse.getQrcode();
        					BufferedImage bufImage=QrcodePay.encodeAsBitmap(qrCodeUrl,200);
        					ByteArrayOutputStream bos=new ByteArrayOutputStream();
        					ImageIO.write(bufImage, "png", bos);
        					        					
        					ModelAndView mav=new ModelAndView();
        					mav.setViewName("platform/qrcode");  
        					mav.addObject("amount",order.getOrderAmount()); 
        					mav.addObject("channel",SystemConstants.CHANNEL_CODE_APMP_WEIXIN);
        					mav.addObject("txnId",order.getTransactionNo()); 
        					mav.addObject("orderId",csbPayrequest.getOdNo()); 
        					mav.addObject("batchNo",csbPayrequest.getBatchNo()); 
        					mav.addObject("traceNo",csbPayrequest.getTraceNo());
        					mav.addObject("transType",QrcodePay.TRANS_TYPE_PAY); 
        					mav.addObject("apiUrl",apiUrl); 
        					mav.addObject("qrcode",new BASE64Encoder().encode(bos.toByteArray()));   
        					return mav;
            			}
        				else{
            				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", csbPayresponse.getReultMsg()==null?"获取支付二维码失败!":csbPayresponse.getReultMsg());
            			}
        			}
        			else{
        				return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", csbPayresponse.getRequestResultMsg()==null?csbPayresponse.getErrorMsg():csbPayresponse.getRequestResultMsg());
        			}
        			
        			
        		}
        		else{
        			return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/result"),"msgCode", loginResponse.getRequestResultMsg()==null?loginResponse.getErrorMsg():loginResponse.getRequestResultMsg());
        		}        		        	    
        	}
        	
        	else{
        		return new ModelAndView(new RedirectView(request.getContextPath()+"/platform/select"));
        	}
	    	
    	}
    	    	    	
	}
        
}
