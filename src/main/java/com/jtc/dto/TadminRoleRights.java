package com.jtc.dto;

// Generated Oct 29, 2014 11:20:20 AM by Hibernate Tools 3.4.0.CR1

/**
 * BpsAdminRoleRights generated by hbm2java
 */
public class TadminRoleRights implements java.io.Serializable {

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = 979892202536550526L;
	private int roleId;
	private long roleRights;
	private long[] rights;

	public TadminRoleRights() {
	}

	public TadminRoleRights(int roleId, long roleRights) {
		this.roleId = roleId;
		this.roleRights = roleRights;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public long getRoleRights() {
		return this.roleRights;
	}

	public void setRoleRights(long roleRights) {
		this.roleRights = roleRights;
	}

	public long[] getRights() {
		return rights;
	}

	public void setRights(long[] rights) {
		this.rights = rights;
	}

}