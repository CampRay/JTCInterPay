package com.jtc.dto;

import java.io.Serializable;


/**
 * The persistent class for the pg_country database table.
 * 
 */
public class Tcurrency implements Serializable {	
	
	private static final long serialVersionUID = -2841568329601638408L;

	private int id;

	private String name;	

	private String code;		
			
	private String symbols;
					

	public Tcurrency() {
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


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getSymbols() {
		return symbols;
	}


	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}	

}