package com.jtc.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.commons.MyException;
import com.jtc.commons.SecurityTools;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TadminUser;
import com.jtc.dto.Tsetting;
import com.jtc.model.PagingData;
import com.jtc.service.SystemSettingService;


@Controller
@RequestMapping(value="/settings")
public class SettingController extends BaseController {
	
	
	@Resource
	private SystemSettingService systemSettingService;
		

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView settings(HttpServletRequest request){
		List<String> list = null;
		Map<String, List<String>> system_setting =new LinkedHashMap<String, List<String>>();
		ModelAndView mav=new ModelAndView();
		try{
			PagingData pagingData = systemSettingService.loadSystemsetting();
			Object[] objs = pagingData.getAaData();
			for(int i=0;i<objs.length;i++){
				list = new ArrayList<String>();
				Tsetting tsetting = (Tsetting)objs[i];
				list.add(new String(tsetting.getValue(),"UTF-8"));
				list.add(tsetting.getDescr());
				system_setting.put(tsetting.getName(), list);
			}
		}catch(MyException m){
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TadminUser tUser=getSessionUser(request);
		if(tUser!=null){
			mav.addObject("system_setting",system_setting);
		    mav.setViewName("settings/systemsetting");
		}
		else{
			tUser=new TadminUser();		
			mav.addObject("user", tUser);
			mav.setViewName("login");
		}
		return mav;
	}
	
    @RequestMapping(value="storesetting",method=RequestMethod.GET)
	public ModelAndView storeSettings(HttpServletRequest request){
		List<String> list = null;
		Map<String, List<String>> store_setting =new LinkedHashMap<String, List<String>>();
		ModelAndView mav=new ModelAndView();
		try{
			PagingData pagingData = systemSettingService.getStoreSetting();
			Object[] objs = pagingData.getAaData();
			for(int i=0;i<objs.length;i++){
				list = new ArrayList<String>();
				Tsetting tsetting = (Tsetting)objs[i];
				list.add(new String(tsetting.getValue(),"UTF-8"));
				list.add(tsetting.getDescr());
				store_setting.put(tsetting.getName(), list);
			}
		}catch(MyException m){
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.addObject("store_setting",store_setting);
		mav.setViewName("settings/storesetting");
		return mav;
	}
	
	@RequestMapping(value="editsetting",method=RequestMethod.POST)
	@ResponseBody
	public String editStoreSettings(HttpServletRequest request,String name,String value){
		JSONObject resp = new JSONObject();
		
		try{
			Tsetting tsetting = systemSettingService.getSystemSettingByName(name);
			if(tsetting != null){
				if(SystemConstants.ACCESS_PASSWORD.equals(name)||SystemConstants.TOKEN.equals(name)){
				    tsetting.setValue(SecurityTools.MD5(value).getBytes("UTF-8"));
				}else{
					tsetting.setValue(value.getBytes("UTF-8"));
				}
	            systemSettingService.updateSystemsetting(tsetting);
	            systemSettingService.cachedSystemSettingData();
			}			
		}catch(MyException m){
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.put("status", true);
		return JSON.toJSONString(resp);
	}
	
	 @RequestMapping(value="editstoreimage",method=RequestMethod.POST)
     @ResponseBody
     public String editStoreImage(HttpServletRequest request,@RequestParam(value="images",required=true)MultipartFile file,@RequestParam int flag) throws IOException{
    	JSONObject resp = new JSONObject();
    	String realPath = request.getSession().getServletContext().getRealPath("/");
    	Tsetting tsetting = null;
    	File image;
    	if(flag == 1){
    		image = new File(realPath,File.separator+"upload"+File.separator+"store"+File.separator+"store_background.jpg");
    		if(image.exists()){
        		image.delete();
        	}
    		tsetting = systemSettingService.getSystemSettingByName(SystemConstants.PAGE_BACKGROUND_File);
        }else{
        	image = new File(realPath,File.separator+"upload"+File.separator+"store"+File.separator+"store_logo.png");
    		if(image.exists()){
        		image.delete();
        	}
    		tsetting = systemSettingService.getSystemSettingByName(SystemConstants.RESTAURANT_LOGO_File);
        }
    	tsetting.setValue(file.getBytes());
    	FileUtils.copyInputStreamToFile(file.getInputStream(), image);
    	systemSettingService.updateSystemsetting(tsetting);
    	resp.put("status", true);
 	    return JSON.toJSONString(resp);
    	
    	}
	 
	    @RequestMapping(value="locale",method=RequestMethod.GET)
		public void setLocale(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    	//String local=(String)request.getParameter("locale");
	    	//changeLocale(request,local);
	    	request.getSession().removeAttribute("locale");			
			Locale locale=LocaleContextHolder.getLocale();
			request.getSession().setAttribute("locale", locale);
			String referer = request.getHeader("referer");
			response.sendRedirect(referer);
		}
	 
}
