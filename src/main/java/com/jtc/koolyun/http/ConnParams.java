package com.jtc.koolyun.http;


import java.util.Map;

/**
 * params for httpconnect
 * 
 * @author ttlu
 * 
 */
public final class ConnParams {
	private String url; 
	private Map<String, String> headers;
	private String postData;

	public ConnParams(String url,Map<String, String> headers, String postData) {
		this.url = url;
		this.headers = headers;
		this.postData = postData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String urlStr) {
		this.url = urlStr;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getPostData() {
		return postData;
	}
}
