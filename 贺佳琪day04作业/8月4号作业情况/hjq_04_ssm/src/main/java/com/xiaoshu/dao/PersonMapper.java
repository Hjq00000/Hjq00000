package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import java.util.List;

public interface PersonMapper extends BaseMapper<Person> {

	public List<Person> queryAll(Person p);

	public List<Company> queryCompany();

	public void addPerson(Person p);

	public void update(Person p);
  


}