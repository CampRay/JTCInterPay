package com.jtc.koolyun.pay;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.InvalidKeyException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jtc.koolyun.http.CodeUtil;
import com.jtc.koolyun.http.NetEngine;
import com.jtc.koolyun.reponse.BSCPayResponse;
import com.jtc.koolyun.reponse.CSBPayResponse;
import com.jtc.koolyun.reponse.CancelResponse;
import com.jtc.koolyun.reponse.LoginResponse;
import com.jtc.koolyun.reponse.PageQueryResponse;
import com.jtc.koolyun.reponse.PayQueryResponse;
import com.jtc.koolyun.request.BSCPayRequest;
import com.jtc.koolyun.request.CSBPayRequest;
import com.jtc.koolyun.request.CancelRequest;
import com.jtc.koolyun.request.LoginRequest;
import com.jtc.koolyun.request.PageQueryRequest;
import com.jtc.koolyun.request.PayQueryRequest;

import sun.misc.BASE64Decoder;


public class QrcodePay {
	public final static String PAYTYPE_WEIXIN = "weixinOverSea";
	public final static String PAYTYPE_ALIPAY = "alipayOverSea";
	
	public final static String ACTION_CSB = "csbpay";
	public final static String ACTION_BSC = "bscpay";
	public final static String ACTION_CANCEL = "cancel";
	public final static String ACTION_REFUND = "refund";
	
	public final static String TRANS_TYPE_PAY = "1021"; //消费支付
	public final static String TRANS_TYPE_CANCEL = "3021";//消费撤销
	public final static String TRANS_TYPE_REFUND = "3051";//退款
	
	public final static String PAYMENTID_ALIPAY1 = "0000000102";//只支持跨境支付
	public final static String PAYMENTID_ALIPAY2 = "0000000340";//持跨境支付和香港本地钱包
	public final static String PAYMENTID_WEIXIN = "0000000104";//只支持跨境支付
	public final static String PAYMENTID_SETTING = "0000000000";
	
	public final static String CURRENCY_HKD = "344";
	public final static String CURRENCY_CNY = "156";
	public final static String CURRENCY_SGD = "702";
	public static String TraceNo = "000000";
	public static String BatchNo = "000000";	
	public static Map<String, String> resHeaderMap = new HashMap<String, String>();

	/**
	 * 登录接口
	 * 
	 * @param 登录对象
	 *            request
	 * @return 登录返回对象 response
	 */
	public static LoginResponse login(LoginRequest request,String apiUrl,String publicKey,String privateKey) {
		QrcodePay.setTraceNo("000000");
		LoginResponse response = new LoginResponse();
		String Pwd = CodeUtil.md5(request.getPwd()).toLowerCase();
		request.setPwd(Pwd);
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(request.getMerchId());
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		response = QrcodePay.fromJson(dataResult.toString(),
				LoginResponse.class);
		if ("0".equals(response.getResponseCode())) {
			response.setRequestResult(LoginResponse.SUCCESS);			
		} else if ("1".equals(response.getResponseCode())) {
			response.setRequestResult(LoginResponse.FAIL);
		} else {
			response.setRequestResult(LoginResponse.UNKNOW);
		}
		response.setRequestResultMsg(response.getErrorMsg());
		response.setRawData(dataResult.toString());
		QrcodePay.setBatchNo(response.getBatchNo());
		return response;
	}

	/**
	 * 
	 * @param CSBPayRequest
	 *            对象 request
	 * @return CSBPayResponse 对象 response
	 */
	public static CSBPayResponse CSBPay(CSBPayRequest request,String apiUrl,String publicKey,String privateKey,String merchId) {
		CSBPayResponse response = new CSBPayResponse();
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("payType");
		nameList.add("dataAction");
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(merchId);
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		String responseCode=dataResult.getString("responseCode");		
		if("0".equals(responseCode)){
			String data = "";
			try {
				data = (String) dataResult.get("data");
			} catch (Exception e) {
				data = dataResult.toString();
			}
			response = QrcodePay.fromJson(data, CSBPayResponse.class);
			response.setResponseCode(responseCode);
			if ("0".equals(response.getResponseCode())) {
				response.setRequestResult(CSBPayResponse.SUCCESS);
			} else if ("1".equals(response.getResponseCode())) {
				response.setRequestResult(CSBPayResponse.FAIL);
			} else {
				response.setRequestResult(CSBPayResponse.UNKNOW);
			}
			response.setRequestResultMsg(response.getErrorMsg());
			response.setRawData(dataResult.toString());
			return response;
		}
		else{
			response.setResponseCode(responseCode);
			response.setRequestResultMsg(dataResult.getString("errorMsg"));
			return response;
		}
	}

	/**
	 * BSC 接口
	 * 
	 * @param request
	 * @return
	 */
	public static BSCPayResponse BSCPay(BSCPayRequest request,String apiUrl,String publicKey,String privateKey,String merchId) {
		BSCPayResponse response = new BSCPayResponse();
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("payType");
		nameList.add("payCode");
		nameList.add("dataAction");
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(merchId);
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		String responseCode=dataResult.getString("responseCode");		
		if("0".equals(responseCode)){
			String data = "";
			try {
				data = (String) dataResult.get("data");
			} catch (Exception e) {
				data = dataResult.toString();
			}
			response = QrcodePay.fromJson(data, BSCPayResponse.class);
			if ("0".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(BSCPayResponse.SUCCESS);
			} else if ("1".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(BSCPayResponse.FAIL);
			} else {
				response.setRequestResult(BSCPayResponse.UNKNOW);
			}
			response.setRequestResultMsg(response.getErrorMsg());
			response.setRawData(dataResult.toString());
			response.setPayResultMsg(response.getReultMsg());
			return response;
		}
		else{
			response.setResponseCode(responseCode);
			response.setRequestResultMsg(dataResult.getString("errorMsg"));
			return response;
		}
	}

	/**
	 * 撤销交易接口
	 * 
	 * @param request
	 * @return
	 */
	public static CancelResponse Cancel(CancelRequest request,String apiUrl,String publicKey,String privateKey,String merchId) {
		CancelResponse response = new CancelResponse();
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("payType");
		nameList.add("refNo");
		nameList.add("dataAction");
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(merchId);
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		String responseCode=dataResult.getString("responseCode");		
		if("0".equals(responseCode)){
			String data = "";
			try {
				data = (String) dataResult.get("data");
			} catch (Exception e) {
				data = dataResult.toString();
			}
	
			response = QrcodePay.fromJson(data, CancelResponse.class);
			if ("0".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(CancelResponse.SUCCESS);
			} else if ("1".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(CancelResponse.FAIL);
			} else {
				response.setRequestResult(CancelResponse.UNKNOW);
			}
			response.setRequestResultMsg(response.getErrorMsg());
			response.setRawData(dataResult.toString());
			// response.setPayResultMsg(response.getReultMsg());
			return response;
		}
		else{
			response.setResponseCode(responseCode);
			response.setRequestResultMsg(dataResult.getString("errorMsg"));
			return response;
		}
	}

	/**
	 * 交易查询接口
	 * 
	 * @param request
	 * @return
	 */
	public static PayQueryResponse Query(PayQueryRequest request,String apiUrl,String publicKey,String privateKey,String merchId) {
		PayQueryResponse response = new PayQueryResponse();
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(merchId);
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		response = QrcodePay.fromJson(dataResult.toJSONString(), PayQueryResponse.class);				
		if("0".equals(response.getResponseCode())){
			response.setRequestResult(PayQueryResponse.SUCCESS);						
			response.setRawData(dataResult.toString());
			try {
				String statusMsg="UNKNOW";
				switch (Integer.parseInt(response.getStatusCode())) {
				case 0:
					statusMsg="成功";
					break;
				case 1:
					statusMsg="失敗";
					break;
				case 2:
					statusMsg="已冲正";
					break;
				case 3:
					statusMsg="已撤销";
					break;
	
				case 4:
					statusMsg="已完成";
					break;
	
				case 5:
					statusMsg="未知";
					break;
	
				case 6:
					statusMsg="撤销中";
					break;
	
				case 9:
					statusMsg="超时";
					break;
				}
				response.setStatusMsg(statusMsg);
			} catch (Exception e) {
			}
		}
		else if("1".equals(response.getResponseCode())){
			response.setRequestResult(PayQueryResponse.FAIL);			
		}
		else {
			response.setRequestResult(PayQueryResponse.UNKNOW);
		}
		return response;
	}
	/**
	 * 交易分页查询
	 * 
	 * @param request
	 * @return
	 */
	public static PageQueryResponse PageQuery(PageQueryRequest request,String apiUrl,String publicKey,String privateKey,String merchId) {
		PageQueryResponse response = new PageQueryResponse();
		JSONObject dataResult = null;
		JSONArray jsArray = null;
		ArrayList<String> nameList = new ArrayList<String>();
		try {
			jsArray = QrcodePay.reflect(request, nameList);
		} catch (Exception e) {
			System.out.println("对象解析出错！" + e);
		}
		HashMap<String, String> requestHeaders = NetEngine.getRequestHeader(merchId);
		dataResult = new NetEngine().post(new String(jsArray.toString()),
				requestHeaders,apiUrl,publicKey,privateKey);
		String responseCode=dataResult.getString("responseCode");		
		if("0".equals(responseCode)){			
			if ("0".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(PayQueryResponse.SUCCESS);
			} else if ("1".equals((String) dataResult.get("responseCode"))) {
				response.setRequestResult(PayQueryResponse.FAIL);
			} else {
				response.setRequestResult(PayQueryResponse.UNKNOW);
			}
			
			response.setRequestResultMsg((String) dataResult.get("errorMsg"));
			response.setRawData(dataResult.toString());
			return response;
		}
		else{
			response.setResponseCode(responseCode);
			response.setRequestResultMsg(dataResult.getString("errorMsg"));
			return response;
		}
	}
	
	
	
	// -------------------下面是工具类-----------------------------
	//
	private static PublicKey getPublicKey(String key) {
		byte[] keyBytes=null;
		try {
			keyBytes = new BASE64Decoder().decodeBuffer(key);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static PrivateKey getPrivateKey(String key) {
    	byte[] keyBytes=null;
		try {
			keyBytes = new BASE64Decoder().decodeBuffer(key);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    	KeyFactory keyFactory;
    	try {
    	keyFactory = KeyFactory.getInstance("RSA");
    	PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    	
    	return privateKey;
    	} catch (NoSuchAlgorithmException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	} catch (InvalidKeySpecException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	}
    	return null;
    	}

    	public static String byte2HexString(byte[] b) {
    	String ret = "";
    	for (int i = 0; i < b.length; i++) {
    	String hex = Integer.toHexString(b[i] & 0xFF);
    	if (hex.length() == 1) {
    	hex = '0' + hex;
    	}
    	ret += hex.toUpperCase();
    	}
    	return ret;
    }
	
	//用私钥对内容进行簽名
    public static String sign(String body,String privateKey){
    	    	
    	String sign = null;
		try {					
			PrivateKey key = getPrivateKey(privateKey);
    		Signature signature = Signature.getInstance("SHA1WithRSA");
    		signature.initSign(key);
    		signature.update(body.getBytes("utf-8"));
    		sign = CodeUtil.byte2HexString(signature.sign());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}    	       

	//用公钥验证签名
	public static boolean verify(byte[] data, String sign) {		
		String pkey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQ/f+i/3LHz53nXXHYO6HdL71H"
				+"5nf3azES2/KX+DJGWSAPfmNdeefJibWSogAttMfctGBECGZNKPoaQYStp7yqZaZy"
				+"Qzfx9Li86yo4Goav8Ze5t46SNajR2AtGqOzoAjM/Wuyw266ZIwU9uwSJmLWpAC6s"
				+"TTcddvaZ+XHL4LvUqQIDAQAB";		

		PublicKey publicKey = getPublicKey(pkey);
		Signature sig;
		try {
			sig = Signature.getInstance("SHA1WithRSA");
			sig.initVerify(publicKey);
			sig.update(data);
			return sig.verify(CodeUtil.hexString2Byte(sign));						
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * json转对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String str, Class<T> type) {
//		Gson gson = new Gson();		
//		return gson.fromJson(str, type);
		return JSONObject.parseObject(str, type);
	}

	// 解析对象返回 jsonArray
	public static JSONArray reflect(Object e, ArrayList<String> nameList)
			throws Exception {
		Class cls = e.getClass();
		Field[] fields = cls.getDeclaredFields();
		JSONArray jsArray = new JSONArray();
		JSONObject jsObjDetial = new JSONObject();
		JSONObject jsObjBody = new JSONObject();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			boolean IsData = false;
			for (String s : nameList) {
				if (f.getName().equals(s)) {
					IsData = true;
				}
			}
			if (f.getName().equals("dataAction")) {
				jsObjDetial.put("action", f.get(e));
				continue;
			}
			if (IsData) {
				jsObjDetial.put(f.getName(), f.get(e));
			} else {
				jsObjBody.put(f.getName(), f.get(e));
			}
		}

		if (!jsObjDetial.toString().equals("{}")) {
			jsObjBody.put("data", jsObjDetial.toString());
		}
		jsArray.add(jsObjBody);
		return jsArray;
	}

	// 获取traceNo号
	public static String getTraceNo() {
		String s = QrcodePay.TraceNo;
		int number = Integer.parseInt(QrcodePay.TraceNo) + 1;
		QrcodePay.setTraceNo(s.substring(0, 6 - (number + "").length())
				+ number);
		return QrcodePay.TraceNo;
	}

	// 获取时间
	public static String getTransTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String transTime = dateFormat.format(now);
		return transTime;
	}
	
	
	/**
	 * 生成二维码
	 * @param contents 内容	 
	 * @param dimension 二维码的尺寸
	 * @return
	 * @throws WriterException
	 */
	public static BufferedImage encodeAsBitmap(String contents,int dimension) throws WriterException {
	    if (contents == null) {
	      return null;
	    }
	    Map<EncodeHintType,Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
	    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	    //容错级别
	    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
	    //设置空白边距的宽度
	    hints.put(EncodeHintType.MARGIN, 0);
	    BitMatrix result;
	    try {
	      result = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, dimension, dimension, hints);
	      int width = result.getWidth();
	      int height = result.getHeight();
	      int WHITE = 0xFFFFFFFF;
		  int BLACK = 0xFF000000;
	      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	      for (int x = 0; x < width; x++) {
             for (int y = 0; y < height; y++) {
            	 bufferedImage.setRGB(x, y, result.get(x, y) ? BLACK : WHITE);
             }
          }	      
	      return bufferedImage;	      
	    } catch (IllegalArgumentException iae) { 
	      return null;
	    }	    	    	    	    
	}
	  
	public static void setTraceNo(String traceNo) {
		TraceNo = traceNo;
	}

	public static String getBatchNo() {
		return BatchNo;
	}

	public static void setBatchNo(String batchNo) {
		BatchNo = batchNo;
	}

	public static Map<String, String> getResHeaderMap() {
		return resHeaderMap;
	}

	public static void setResHeaderMap(Map<String, String> resHeaderMap) {
		QrcodePay.resHeaderMap = resHeaderMap;
	}
}
