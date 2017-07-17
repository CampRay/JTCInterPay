package com.jtc.service;

import java.util.List;

import com.jtc.dto.Torder;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;

public interface OrderService {
	
	Torder getOrderById(int id);		
	
	void createOrder(Torder order);
	
	void updateOrder(Torder order);
	
	void deleteOrder(Torder order);
	
	void deleteOrderById(int id);
	
	void deleteOrdersByIds(Integer[] ids);	
	
	List<Torder> loadOrderByStatus(int status);
	
	List<Torder> loadOrderByNotified(boolean notified);
	
	PagingData loadOrdersList(DataTableParamter rdtp,Integer appId );
		
		
}
