package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	private PersonMapper pm;

	@Autowired
	private CompanyMapper cm;

	public PageInfo<Person> findPersonPage(int pageNum, int pageSize,Person p) {
		PageHelper.startPage(pageNum, pageSize);
		List<Person> personList = pm.queryPerson(p);
		PageInfo<Person> pageInfo = new PageInfo<Person>(personList);
		return pageInfo;
	}

	
	public List<Person> expPerson(Person p) {
		List<Person> personList = pm.queryPerson(p);
		return personList;
	}

	
	public List<Company> queryEnableCompany(){
		return pm.queryEnableCompany();
	}
	
	public void add(Person p){
		p.setCreateTime(new Date());
		pm.addPerson(p);
	}

	
	public void update(Person p){
		pm.updatePerson(p);
	}
	
	

	// 删除
	public void delete(Integer id){
		pm.deleteByPrimaryKey(id);
	};

	public void addCompany(Company c){
		cm.insert(c);
	}
	
	public Company getCompanyByCname(String cname){
		return pm.getCompanyByCname(cname);
	}
	
	
	
}
