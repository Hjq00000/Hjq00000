package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
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

	public PageInfo<Person> findPerson(Person p, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Person> personList = pm.queryAll(p);
		PageInfo<Person> pageInfo = new PageInfo<Person>(personList);
		return pageInfo;
	}
	
	public List<Company> queryCompany(){
		return pm.queryCompany();
	}

	public void add(Person p) {
		pm.addPerson(p);
		
	}

	public void deletePerson(int parseInt) {
		// TODO Auto-generated method stub
		pm.deleteByPrimaryKey(parseInt);
	}

	public void update(Person p) {
		// TODO Auto-generated method stub
		pm.update(p);
	}


}
