package com.jtc.koolyun.http;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jtc.koolyun.pay.QrcodePay;

public class NetEngine {
	private final static String PREFERENCES_KEY_TERMINAL_ID = "terminalId";
	private static final String HEADER_SESSION_ID = "x-apsessionid"; // sessionid
	private static final String HEADER_VERSION = "x-apversion"; // version, the new version is 1.0
	private static final String HEADER_SIGNATURE = "x-apsignature"; // The data checksum
	private static final String HEADER_TERMINAL_ID = "x-apterminalid"; // client user ID
	
	private static final String HEADER_KEY_EXCHANGE = "x-apkeyexchange"; // The key exchange data	
	private static final String HEADER_KEY_APPKey = "x-appkey";// channel
																				// id

    private static final String HEADER_KEY_LANGUAGE = "Accept-Language";
    private static final String HEADER_KEY_ACCEPT = "Accept";


    private static final int CONNECT_RESULT_SUCCESS = 0;
    private static final int CONNECT_RESULT_NO_CONNECTION = -1;

	public static HashMap<String, String> getRequestHeader(String appKey) {
		HashMap<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put(HEADER_VERSION, "1.0");
		requestHeaders.put(HEADER_KEY_APPKey, appKey);
        requestHeaders.put(HEADER_KEY_ACCEPT, "application/json");
        requestHeaders.put(HEADER_KEY_LANGUAGE, "zh-CN");
        requestHeaders.put("X-APFormat", "json");
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        requestHeaders.put("x-apsessionid", QrcodePay.resHeaderMap.get("x-apsessionid"));
       // requestHeaders.put("x-apsignature", QrcodePay.resHeaderMap.get("x-apsignature"));
        
        return requestHeaders;
	}


	private  JSONObject responseHeaderWithRequest(Map<String, String> headerMap) {
		String terminal = headerMap.get(HEADER_TERMINAL_ID);
		if (null != terminal && terminal.length() > 0) {
			Map<String, Object> preferencesDataMap = new HashMap<String, Object>();
			preferencesDataMap.put(PREFERENCES_KEY_TERMINAL_ID, terminal);
		}
		boolean isKeyExchangeSucc = false;		
		JSONObject responseHeader = new JSONObject();
		if (!isKeyExchangeSucc) {
			try {
				responseHeader.put("keyExchange", "1");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return responseHeader;
	}

	//发送http请求
    public JSONObject post(String body, HashMap<String, String> headerMap,String apiUrl,String publicKey,String privateKey) {
    	JSONObject resJsonObject = new JSONObject();                          
        String postStr = String.format("params=%s", body);
        System.out.println("postStr:"+postStr);
        //接入方要使用私钥对JSON报文（不包含“params=”)进行SHA1WithRSA签名
        String signature = QrcodePay.sign(body,privateKey);
        headerMap.put(HEADER_SIGNATURE, signature);
        System.out.println("requestHeaders = "+headerMap.toString());
	    HttpConn conn = new HttpConn(new ConnParams(apiUrl, headerMap, postStr));
	        
	    int result = conn.excute();	    
	    if (CONNECT_RESULT_SUCCESS == result) {
		    Header[] responseHeaders = conn.getResponseHeaders();
		    Map<String, String> resHeaderMap = new HashMap<String, String>();
		    for (Header header : responseHeaders) {
			    resHeaderMap.put(header.getName(), header.getValue());
		    }
		    String sign =  resHeaderMap.get("x-apsignature");
//			    System.out.println("头返回："+resHeaderMap.toString());
		    QrcodePay.setResHeaderMap(resHeaderMap);
		    JSONArray resBodyJsonArray = null;
		    byte[] bytesResponseData = conn.getResponseData();
		    String strResponseData = "";
		    if (null != bytesResponseData) {
			    try {
				    strResponseData = new String(bytesResponseData, "UTF-8");
				    System.out.println("responsetStr = "+strResponseData);
			    } catch (UnsupportedEncodingException e1) {
				    e1.printStackTrace();
			    }
			    try {				    	
			    	resBodyJsonArray = JSON.parseArray(strResponseData);					    
				    return resBodyJsonArray.getJSONObject(0);
			    } catch (JSONException e) {
				    resBodyJsonArray = null;
			    }
		    }
		    //验证签名
		    if(QrcodePay.verify(bytesResponseData,sign)){			    
			    JSONObject resHeaderJsonObject = responseHeaderWithRequest(resHeaderMap);
			    try {
				    if (null != resHeaderJsonObject) {
					    String reex = headerMap.get(HEADER_KEY_EXCHANGE);
					    if ("".equals(reex)) {
						    resHeaderJsonObject.remove("keyExchange");
					    } else {
						
					    }
					    resJsonObject.put("header", resHeaderJsonObject);
				    }

				    resJsonObject.put("body", resBodyJsonArray);
			    } catch (JSONException e) {
				    e.printStackTrace();
			    }
		    }
		    else{
		    	resJsonObject.put("responseCode", "0");		    	
		    	resJsonObject.put("errorMsg", "签名验证失败");
		    }
	    } else {
		    String keyExchange = "0";
		    if (!("".equals(headerMap.get(HEADER_KEY_EXCHANGE)))) {
			    keyExchange = "1";
		    }
		    int responseCode = conn.getResponseCode();
		    try {
			    resJsonObject.put("errCode", String.valueOf(responseCode));
			    if (CONNECT_RESULT_NO_CONNECTION == result) {
				    resJsonObject.put("errorMsg", "没有网络连接");
			    } else {
				    resJsonObject.put("errorMsg", connectErrorDescript(responseCode));
			    }
			    resJsonObject.put("keyExchange", keyExchange);
		    } catch (JSONException e) {
			    e.printStackTrace();
		    }
	    }
	    conn = null;
	    return resJsonObject;
    }
        	

	private static String connectErrorDescript( int errorCode) {
		String errorDesc = null;
		switch (errorCode / 100) {
		case 3:
			errorDesc = errorCode + "重定向";
			break;
		case 4:
			switch (errorCode) {
			case HttpStatus.SC_BAD_REQUEST:
				errorDesc = errorCode + "错误请求";
				break;
			case HttpStatus.SC_UNAUTHORIZED:
				errorDesc = errorCode + "未授权";
				break;
			case HttpStatus.SC_FORBIDDEN:
				errorDesc = errorCode + "禁止访问";
				break;
			case HttpStatus.SC_NOT_FOUND:
				errorDesc = errorCode + "找不到请求内容";
				break;
			default:
				errorDesc = errorCode + "客户端错误";
				break;
			}
			break;
		case 5:
			switch (errorCode) {
			case HttpStatus.SC_BAD_GATEWAY:
				errorDesc = errorCode + "网关错误";
				break;
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				errorDesc = errorCode + "服务不可�?";
				break;
			case HttpStatus.SC_GATEWAY_TIMEOUT:
				errorDesc = errorCode + "网关超时";
				break;
			case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
				errorDesc = errorCode + "不支持的 HTTP版本";
				break;
			default:
				errorDesc = errorCode + "服务器错误";
				break;
			}
			break;
		default:
			switch (errorCode) {
			case HttpConn.RESPONSECODE_EXCEPTION_TIMEOUT:
				errorDesc = "联网超时";
				break;
			default:
				errorDesc = errorCode + "通讯故障";
				break;
			}
			break;
		}

		return errorDesc;
	}
}
