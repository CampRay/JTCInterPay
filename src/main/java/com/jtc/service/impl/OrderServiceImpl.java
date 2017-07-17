/**   
 * @Title: OrderServiceImpl.java 
 * @Package com.jtc.service 
 * @date 6 23, 2017 7:21:25 PM
 * @version V1.0   
 */ 
package com.jtc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.dao.OrderDao;
import com.jtc.dto.Torder;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.OrderService;

/** 
 * <p>Types Description</p>
 * @ClassName: OrderServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;

	
	public Torder getOrderById(int id) {
		return orderDao.get(id);
	}	

	
	public void createOrder(Torder order) {
		orderDao.create(order);		
	}

	
	public void updateOrder(Torder order) {
		orderDao.update(order);
	}

	
	public void deleteOrder(Torder order) {
		orderDao.delete(order);		
	}

	
	public void deleteOrderById(int id) {
		orderDao.delete(id);
	}

	
	public void deleteOrdersByIds(Integer[] ids) {
		orderDao.deleteAll(ids);
	}
	
	
	public List<Torder> loadOrderByStatus(int status){
		return orderDao.findBy("status", status);
	}
		
	
	public List<Torder> loadOrderByNotified(boolean notified){
		String[] keys=new String[]{"notified","status"};
		Object[] values=new Object[]{Boolean.valueOf(notified),Integer.valueOf(1)};
		return orderDao.findBy(keys, values);
	}
		
	
	public PagingData loadOrdersList(DataTableParamter rdtp,Integer appId) {
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("MM/dd/yyyy");
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("app.id", appId));
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					if(key=="channel.id"){
						criterionsList.add(Restrictions.eq(key, jsonObj.getInteger(key)));
					}
					else if(key=="status"){
						criterionsList.add(Restrictions.eq(key, jsonObj.getInteger(key)));
					}
					else if(key=="startTime"){
						Date sdate = null;
						try {
							sdate = simpleDateFormat.parse(val);							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Long startLong=sdate.getTime();						
						criterionsList.add(Restrictions.ge("createdTime", startLong));
					}
					else if(key=="endTime"){
						Date edate = null;
						try {
							edate = simpleDateFormat.parse(val);							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Long endLong=edate.getTime();						
						criterionsList.add(Restrictions.le("createdTime", endLong));
					}
					else{
						criterionsList.add(Restrictions.eq(key, jsonObj.get(key)));
					}
				}
			}
				
			Criterion[] criterions=new Criterion[criterionsList.size()];
			int i=0;
			for (Criterion criterion : criterionsList) {
				criterions[i]=criterion;	
				i++;
			}
			return orderDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		else{
			Criteria criteria=orderDao.createCriteria();
			criteria.add(Restrictions.eq("app.id", appId));
			return orderDao.findPage(criteria,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		
	}
				

}
