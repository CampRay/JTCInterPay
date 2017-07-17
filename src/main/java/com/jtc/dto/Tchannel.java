package com.jtc.dto;

import java.io.Serializable;


/**
 * The persistent class for the channel database table.
 * 
 */
public class Tchannel implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;	

	private String name;
	
	private String envi;
	
	private String desc;
		
	private String code;
	
	private boolean status=false;//是否已開通此支付渠道
		
	public Tchannel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnvi() {
		return envi;
	}

	public void setEnvi(String envi) {
		this.envi = envi;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}			

}