package com.jtc.core;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.jtc.commons.SystemConfig;
import com.jtc.commons.SystemConstants;
import com.jtc.dto.TadminUser;

public class PermissionInteceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		String reqMethod=request.getMethod().toUpperCase();
		String contextPath=request.getContextPath();
		String uri=request.getRequestURI();
		uri=uri.substring(contextPath.length()+1);
		//uri=uri.split("/")[2];
		request.getSession().removeAttribute("locale");
		Object res = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(res==null){
			res = "en_US";
		}
		request.getSession().setAttribute("locale", res.toString());
//		String contextPath=request.getContextPath();
//		if((contextPath+"/login").equalsIgnoreCase(uri)){
//			return true;
//		}
		TadminUser loginUser= (TadminUser)request.getSession().getAttribute(SystemConstants.LOGINED);
		//If not login
		if (loginUser == null) {
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		else{
			//The super user has all the rights
			if(loginUser.getAdminRole().getRoleId()==1){
				return true;
			}
			else{
				String reqPath=reqMethod+"@"+"/"+uri;
				Long rightsBit=SystemConfig.Admin_Nodes_Url_Map.get(reqPath);
				if(rightsBit==null){
					Set<String> keys=SystemConfig.Admin_Nodes_Url_Map.keySet();
					for (String key : keys) {
						if(key.endsWith("*")){
							if(reqPath.startsWith(key.substring(0, key.length()-2))){
								rightsBit=SystemConfig.Admin_Nodes_Url_Map.get(key);
							}
						}
					}
					if(rightsBit==null){
						response.sendRedirect(request.getContextPath()+"/common/noRights");
						return false;
					}
				}
				Long rolesAllRights=(Long)request.getSession().getAttribute(SystemConstants.RIGHTS);
				if(rolesAllRights==null){
					return false;
				}
				else{
					if((rightsBit&rolesAllRights)>0){
						return true;
					}
					else{
						response.sendRedirect(request.getContextPath()+"/common/noRights");
						return false;
					}
				}
			}
			
		}		
	}

}
