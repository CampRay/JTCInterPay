/**   
 * @Title: CurrencyServiceImpl.java 
 * @Package com.jtc.service 
 * @date 6 23, 2017 7:21:25 PM
 * @version V1.0   
 */ 
package com.jtc.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.commons.SystemConfig;
import com.jtc.commons.SystemConstants;
import com.jtc.dao.CurrencyDao;
import com.jtc.dto.Tcurrency;
import com.jtc.dto.Tsetting;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.CurrencyService;

/** 
 * <p>Types Description</p>
 * @ClassName: CurrencyServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {
	@Autowired
	private CurrencyDao currencyDao;

	
	public Tcurrency getCurrencyById(int id) {
		return currencyDao.get(id);
	}	

	
	public void createCurrency(Tcurrency currency) {
		currencyDao.create(currency);		
	}

	
	public void updateCurrency(Tcurrency currency) {
		currencyDao.update(currency);
	}

	
	public void deleteCurrency(Tcurrency currency) {
		currencyDao.delete(currency);		
	}

	
	public void deleteCurrencyById(int id) {
		currencyDao.delete(id);
	}

	
	public void deleteCurrencysByIds(Integer[] ids) {
		currencyDao.deleteAll(ids);
	}
		
	
	public Tcurrency loadCurrencyByCode(String code){
		return currencyDao.findUnique("code", code);
	}

	
	public PagingData loadCurrencysList(DataTableParamter rdtp) {
		String searchJsonStr=rdtp.getsSearch();
		if(searchJsonStr!=null&&!searchJsonStr.isEmpty()){
			List<Criterion> criterionsList=new ArrayList<Criterion>();
			JSONObject jsonObj= (JSONObject)JSON.parse(searchJsonStr);
			Set<String> keys=jsonObj.keySet();						
			for (String key : keys) {
				String val=jsonObj.getString(key);
				if(val!=null&&!val.isEmpty()){
					criterionsList.add(Restrictions.eq(key, jsonObj.get(key)));
				}
			}
			Criterion[] criterions=new Criterion[criterionsList.size()];
			int i=0;
			for (Criterion criterion : criterionsList) {
				criterions[i]=criterion;	
				i++;
			}
			return currencyDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return currencyDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
				
	public void cachedData() {
		List <Tcurrency> currencyList = currencyDao.LoadAll();				
		SystemConfig.Currency_Map.clear();		
		for(Tcurrency currency:currencyList){
			SystemConfig.Currency_Map.put(currency.getCode(),currency.getSymbols());
			
		}		
	}
}
