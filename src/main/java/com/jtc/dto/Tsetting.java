package com.jtc.dto;

// Generated Oct 29, 2014 11:20:20 AM by Hibernate Tools 3.4.0.CR1

/**
 * BpsSetting generated by hbm2java
 */
public class Tsetting implements java.io.Serializable {

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = 5873012088660829829L;
	private Integer id;
	private String name;
	private byte[] value;
	private String descr;
	private short sort;

	public Tsetting() {
	}

	public Tsetting(String name, byte[] value, short sort) {
		this.name = name;
		this.value = value;
		this.sort = sort;
	}

	public Tsetting(String name, byte[] value, String descr, short sort) {
		this.name = name;
		this.value = value;
		this.descr = descr;
		this.sort = sort;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getValue() {
		return this.value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public short getSort() {
		return this.sort;
	}

	public void setSort(short sort) {
		this.sort = sort;
	}

}