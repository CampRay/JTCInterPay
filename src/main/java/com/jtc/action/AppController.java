package com.jtc.action;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.dto.TadminApps;
import com.jtc.dto.TadminUser;
import com.jtc.dto.TappChannels;
import com.jtc.dto.Tapplication;
import com.jtc.dto.Tchannel;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.AdminAppsService;
import com.jtc.service.AdminUserService;
import com.jtc.service.AppChannelsService;
import com.jtc.service.ApplicationService;
import com.jtc.service.ChannelService;
import com.jtc.service.OrderService;


@Controller
@RequestMapping(value="apps")
public class AppController extends BaseController {
	private Logger logger = Logger.getLogger(AppController.class);	
	
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
	
	
	@RequestMapping(value="left",method=RequestMethod.GET)
	public ModelAndView left(HttpServletRequest request){
		ModelAndView mav=new ModelAndView();		
		mav.setViewName("frontend/left");
		return mav;
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView apps(HttpServletRequest request){
		ModelAndView mav=new ModelAndView();		
		TadminUser tUser=getSessionUser(request);
		if(tUser!=null){
			request.getSession().removeAttribute("appId");
			mav.addObject("appsList", adminAppsService.getAllAppsByAdminId(tUser.getAdminId()));
			mav.setViewName("frontend/apps");
		}
		else{
			tUser=new TadminUser();		
			mav.addObject("user", tUser);
			mav.setViewName("login");
		}
		return mav;
	}
		
	/**
	 * <p>Description: 处理新增数据的ajax请求</p>
	 * @Title: addApp 
	 * @param jsonStr
	 * @param request	 
	 * @throws
	 */
	@RequestMapping(value="addApp",method=RequestMethod.POST)	
	public ModelAndView addApp(HttpServletRequest request,Tapplication app){		
		ModelAndView mav=new ModelAndView();	
		
		TadminUser tUser=getSessionUser(request);
		app.setCreatedTime(System.currentTimeMillis());
		app.setAppId(SecurityTools.GenerateUniqueText());
		applicationService.createApp(app);
		TadminApps adminApp=new TadminApps();
		adminApp.setApp(app);
		adminApp.setAdminUser(tUser);
		adminAppsService.createAdminApp(adminApp);
		mav.setViewName("redirect:/apps");
		
		return mav;
	}
	
	/**
	 * <p>Description: </p>
	 * @Title: app 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="app/{appId}",method=RequestMethod.GET)	
	public ModelAndView app(HttpServletRequest request, @PathVariable int appId){		
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			request.getSession().setAttribute("appId", appId);
			Tapplication app=adminApp.getApp();
			mav.addObject("app", app);
		}									
					
		mav.setViewName("frontend/app");
		return mav;
	}
	
	/**
	 * <p>Description: </p>
	 * @Title: app 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="orders/{appId}",method=RequestMethod.GET)	
	public ModelAndView appOrders(HttpServletRequest request,@PathVariable int appId){		
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			request.getSession().setAttribute("appId", appId);								
		}
		mav.addObject("channelList", channelService.getAllChannels());
		mav.setViewName("frontend/app_orders");
		return mav;
	}
	
	@RequestMapping(value="ordersList",method=RequestMethod.GET)
	@ResponseBody
	public String ordersList(HttpServletRequest request,DataTableParamter dtp){	
		Integer appId=(Integer)request.getSession().getAttribute("appId");		
		if(appId!=null){			
			PagingData pagingData=orderService.loadOrdersList(dtp,appId);
			if(pagingData.getAaData()==null){
				Object[] objs=new Object[]{};
				pagingData.setAaData(objs);
			}
			pagingData.setSEcho(dtp.sEcho);
			
			String rightsListJson= JSON.toJSONString(pagingData);
			return rightsListJson;
		}
		else{
			return null;
		}
		
	}
	
	/**
	 * <p>Description: </p>
	 * @Title: app 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="reports/{appId}",method=RequestMethod.GET)	
	public ModelAndView appReports(HttpServletRequest request,@PathVariable int appId){		
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			request.getSession().setAttribute("appId", appId);
			Tapplication app=adminApp.getApp();
			
		}
		mav.setViewName("frontend/app_reports");
		return mav;
	}
	
	/**
	 * <p>Description: </p>
	 * @Title: appChannels 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="channels/{appId}",method=RequestMethod.GET)	
	public ModelAndView appChannels(HttpServletRequest request,@PathVariable int appId){		
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			request.getSession().setAttribute("appId", appId);			
			
			List<Tchannel> channels=channelService.getAllChannels();
			List<TappChannels> appsChannels=appChannelsService.getAllChannelsByAppId(appId);
			for(TappChannels appChannel : appsChannels) {
				for (Tchannel channel : channels) {
					if(appChannel.getChannel().getId()==channel.getId()){
						channel.setStatus(true);
						break;
					}
				}
			}
			mav.addObject("channelList", channels);
			
			
		}
		mav.setViewName("frontend/app_channels");
		return mav;
	}
	
	/**
	 * <p>Description:查看渠道信息 </p>
	 * @Title: appChannels 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="channel/{channelId}",method=RequestMethod.GET)	
	public ModelAndView channel(HttpServletRequest request,@PathVariable int channelId){		
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		Integer appId=(Integer)request.getSession().getAttribute("appId");
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			TappChannels appChannel=appChannelsService.getAppChannel(appId, channelId);
			if(appChannel==null){
				appChannel=new TappChannels();	
				appChannel.setApp(adminApp.getApp());
				Tchannel channel=channelService.getChannelById(channelId);	
				channel.setStatus(false);
				appChannel.setChannel(channel);					
			}
			else{
//				if(!StringTool.isEmptyOrNull(appChannel.getSetting())){
//					//JSONObject jsonObj = (JSONObject) JSON.parse(settingJsonStr);
//					ChannelSetting channelSetting = (ChannelSetting) JSON.parse(appChannel.getSetting());
//					appChannel.setChannelSetting(channelSetting);
//				}
				appChannel.getChannel().setStatus(true);
			}

			mav.addObject("appChannel", appChannel);

		}
		mav.setViewName("frontend/app_channel_setting");
		return mav;
	}
	
	/**
	 * <p>Description: 更新渠道配置</p>
	 * @Title: addApp 
	 * @param jsonStr
	 * @param request	 
	 * @throws
	 */
	@RequestMapping(value="channelSetting",method=RequestMethod.POST)	
	public ModelAndView channelSetting(HttpServletRequest request,TappChannels appChannel){		
		ModelAndView mav=new ModelAndView();
		if(appChannel.getChannelSetting()!=null){			
			appChannel.setSetting(JSON.toJSONString(appChannel.getChannelSetting()));
		}
		
		if(appChannel.getId()==0){			
			appChannel.setCreatedTime(System.currentTimeMillis());	
			appChannelsService.createAppChannel(appChannel);
		}
		else{
			TappChannels appChannelDB=appChannelsService.getAppChannelById(appChannel.getId());
			appChannelDB.setSetting(appChannel.getSetting());
			appChannelsService.updateAppChannel(appChannelDB);
		}
		mav.setViewName("redirect:/apps/channel/"+appChannel.getChannel().getId());
		return mav;
	}
	
	/**
	 * <p>Description:进入应用配置页面 </p>
	 * @Title: app 
	 * @param request
	 * @param appId	 
	 * @throws
	 */
	@RequestMapping(value="setting/{appId}",method=RequestMethod.GET)	
	public ModelAndView appSetting(HttpServletRequest request,@PathVariable int appId){			
		ModelAndView mav=new ModelAndView();
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			request.getSession().setAttribute("appId", appId);								
			mav.addObject("app", adminApp.getApp());
			mav.setViewName("frontend/app_setting");
		}
		
		return mav;
	}
	
	/**
	 * <p>Description:进入应用配置页面 </p>
	 * @Title: app 
	 * @param request
	 * @param application	 
	 * @throws
	 */
	@RequestMapping(value="setting",method=RequestMethod.POST)	
	public ModelAndView appSetting(HttpServletRequest request,Tapplication app){		
		ModelAndView mav=new ModelAndView();	
		try{			
			if(app!=null){	
				Tapplication application=applicationService.getAppById(app.getId());
				application.setNotifyUrl(app.getNotifyUrl());
				application.setReturnUrl(app.getReturnUrl());
				applicationService.updateApp(application);				
			}
			mav.setViewName("redirect:/apps/setting/"+app.getId());
		}
		catch(MyException be){
			logger.error(be.getMessage());
		}	
		return mav;
	}
	
	
	
	@RequestMapping(value="delete/{appId}",method=RequestMethod.GET)	
	public ModelAndView deleteApps(@PathVariable int appId,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();	
		TadminUser tUser=getSessionUser(request);
		TadminApps adminApp=adminAppsService.getAdminApp(appId, tUser.getAdminId());
		if(adminApp!=null){
			adminAppsService.deleteAdminApp(adminApp);
			request.getSession().removeAttribute("appId");						
		}									
					
		mav.setViewName("redirect:/apps");
		return mav;
	}
		
			

	
}
