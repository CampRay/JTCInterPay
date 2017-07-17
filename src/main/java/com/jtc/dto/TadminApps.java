package com.jtc.dto;

import java.io.Serializable;


/**
 * The persistent class for the group_user database table.
 * 
 */
public class TadminApps implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;		

	private Tapplication app;		
	
	private TadminUser adminUser;
	
	

	public TadminApps() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tapplication getApp() {
		return app;
	}

	public void setApp(Tapplication app) {
		this.app = app;
	}

	public TadminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(TadminUser adminUser) {
		this.adminUser = adminUser;
	}	
		
	

}