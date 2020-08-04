package com.xiaoshu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.PersonService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("person")
public class PersonController extends LogController{
	static Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	private PersonService ps;
	
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("personIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		
		List<Company> companyList = ps.queryCompany();
		request.setAttribute("companyList", companyList);
		return "person";
	}
	
	
	@RequestMapping(value="personList",method=RequestMethod.POST)
	public void userList(Person p ,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Person> personList= ps.findPerson(p, pageNum, pageSize);
			
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",personList.getTotal() );
			jsonObj.put("rows", personList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
		
		
	}
	
	
	

	// 新增或修改
	@RequestMapping("reservePerson")
	public void reserveUser(HttpServletRequest request,Person p,HttpServletResponse response){
		Integer Id = p.getId();
		JSONObject result=new JSONObject();
		try {
			if (Id != null) {   // userId不为空 说明是修改
			
					ps.update(p);
					result.put("success", true);
				
			}else {   // 添加
			
					ps.add(p);
					result.put("success", true);
		
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deletePerson")
	public void delPerson(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				ps.deletePerson(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
}
