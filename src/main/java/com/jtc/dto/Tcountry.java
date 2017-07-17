package com.jtc.dto;

import java.io.Serializable;


/**
 * The persistent class for the pg_country database table.
 * 
 */
public class Tcountry implements Serializable {	
	private static final long serialVersionUID = -5983910250891983927L;

	private int id;

	private String name;	

	private String twoCode;		
			
	private String threeCode;
	
	private int numberCode;
			

	public Tcountry() {
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


	public String getTwoCode() {
		return twoCode;
	}


	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}


	public String getThreeCode() {
		return threeCode;
	}


	public void setThreeCode(String threeCode) {
		this.threeCode = threeCode;
	}


	public int getNumberCode() {
		return numberCode;
	}


	public void setNumberCode(int numberCode) {
		this.numberCode = numberCode;
	}


}