package com.jtc.commons;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jtc.dto.TadminNodes;

public class SystemConfig {
	
		
	public static String TOKEN;  
	
	public static Map<String,String> Admin_Token_Map=new LinkedHashMap<String,String>();
	
	public static Map<String,Long> Admin_Nodes_Url_Map=new LinkedHashMap<String,Long>();
					
	public static Map<TadminNodes,List<TadminNodes>> Admin_Nodes_Menu_Map=new LinkedHashMap<TadminNodes,List<TadminNodes>>();
	
	public static Map<String,List<TadminNodes>> Admin_Nodes_Group_Map=new LinkedHashMap<String,List<TadminNodes>>();
	
	public static Map<String,String> Admin_Setting_Map=new Hashtable<String,String>();
	
	public static Map<String,String> Currency_Map=new Hashtable<String,String>();
	
}
