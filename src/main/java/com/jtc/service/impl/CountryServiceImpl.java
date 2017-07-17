/**   
 * @Title: CountryServiceImpl.java 
 * @Package com.jtc.service 
 * @date 6 23, 2017 7:21:25 PM
 * @version V1.0   
 */ 
package com.jtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtc.dao.CountryDao;
import com.jtc.dto.Tcountry;
import com.jtc.model.DataTableParamter;
import com.jtc.model.PagingData;
import com.jtc.service.CountryService;

/** 
 * <p>Types Description</p>
 * @ClassName: CountryServiceImpl 
 * @author Phills Li 
 * 
 */
@Service
public class CountryServiceImpl implements CountryService {
	@Autowired
	private CountryDao countryDao;

	
	public Tcountry getCountryById(int id) {
		return countryDao.get(id);
	}	

	
	public void createCountry(Tcountry country) {
		countryDao.create(country);		
	}

	
	public void updateCountry(Tcountry country) {
		countryDao.update(country);
	}

	
	public void deleteCountry(Tcountry country) {
		countryDao.delete(country);		
	}

	
	public void deleteCountryById(int id) {
		countryDao.delete(id);
	}

	
	public void deleteCountrysByIds(Integer[] ids) {
		countryDao.deleteAll(ids);
	}
		
	
	public Tcountry loadCountryByCode(String code){
		return countryDao.findUnique("code", code);
	}

	
	public PagingData loadCountrysList(DataTableParamter rdtp) {
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
			return countryDao.findPage(criterions,rdtp.iDisplayStart, rdtp.iDisplayLength);
		}
		return countryDao.findPage(rdtp.iDisplayStart, rdtp.iDisplayLength);
	}
				

}
