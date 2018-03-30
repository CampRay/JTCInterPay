package com.jtc.koolyun.request;

public class PageQueryRequest{
	private String action;
	private String pageNo;
	private String pageSize;
	private String startDate;
	private String endDate;
	private String ioperator;
	public String getAction(){
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIoperator() {
		return ioperator;
	}
	public void setIoperator(String ioperator) {
		this.ioperator = ioperator;
	}
	
}
