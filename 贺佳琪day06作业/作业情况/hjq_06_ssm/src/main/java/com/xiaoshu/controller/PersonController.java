package com.xiaoshu.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
import com.xiaoshu.util.TimeUtil;
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
	
	List<Person> pl = new ArrayList<Person>();
	
	@RequestMapping("personIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
	
		List<Company> companyList = ps.queryEnableCompany();
		request.setAttribute("companyList", companyList);
		return "person";
	}
	
	
	@RequestMapping(value="personList",method=RequestMethod.POST)
	public void userList(Person p,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Person> personList= ps.findPersonPage(pageNum, pageSize, p);
			pl = ps.expPerson(p);
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
		Integer id = p.getId();
		JSONObject result=new JSONObject();
		try {
			if (id != null) {   // userId不为空 说明是修改
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
				ps.delete(Integer.parseInt(id));
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
	
	
	@RequestMapping("exp")
	public void expPerson(HttpServletResponse response,HttpServletRequest request) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		String[] title={"编号","快递员姓名","性别","特点","入职时间","创建时间","所属公司"};
		
		HSSFRow fr = sheet.createRow(0);
		for (int i = 0; i < title.length; i++) {
			
			fr.createCell(i).setCellValue(title[i]);
		
		}
		
		for (int i = 0; i < pl.size() ; i++) {
			Person person = pl.get(i);
			HSSFRow row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(person.getId());
			row.createCell(1).setCellValue(person.getExpressName());
			row.createCell(2).setCellValue(person.getSex());
			row.createCell(3).setCellValue(person.getExpressTrait());
			row.createCell(4).setCellValue(TimeUtil.formatTime(person.getEntryTime(),"yyyy-MM-dd"));
			row.createCell(5).setCellValue(TimeUtil.formatTime(person.getCreateTime(),"yyyy-MM-dd"));
			row.createCell(6).setCellValue(person.getCompany().getExpressName());
		}
		
		File exc = new File("e:/1910.xls");
		OutputStream os = new FileOutputStream(exc);
		wb.write(os);
		wb.close();
		
		
	}

	
	@RequestMapping("impPerson")
	public void impPerson(HttpServletRequest request,HttpServletResponse response,MultipartFile excel) throws Exception {
		JSONObject result = new JSONObject();
		//1.通过上传的文件输入流得到工作簿
		HSSFWorkbook wb=new HSSFWorkbook(excel.getInputStream());
		//2.通过下标得到工作簿中的第一张表
		HSSFSheet sheet=wb.getSheetAt(0);
		//3.通过LastRowNum得到总行数，总行数就是你的循环次数
		int hs=sheet.getLastRowNum();
		//4.循环，循环从几开始取决于你是否有表头，因为我的代码有表头，所以我从1开始。
		for(int i=1;i<=hs;i++) {
			//5.循环得到行
			HSSFRow r=sheet.getRow(i);
			Person p=new Person();
			p.setExpressName(r.getCell(0).getStringCellValue());
			p.setSex(r.getCell(1).getStringCellValue());
			p.setEntryTime(new Date());
			p.setExpressTrait("");
			
			Company c=ps.getCompanyByCname(r.getCell(2).getStringCellValue());
			if(c!=null) {
				p.setExpressTypeId(c.getId());
			}else {
				Company ac=new Company();
				ac.setExpressName(r.getCell(2).getStringCellValue());
				ac.setCreateTime(new Date());
				ac.setStatus(1);
				ps.addCompany(ac);
				Company c2=ps.getCompanyByCname(r.getCell(2).getStringCellValue());
				p.setExpressTypeId(c2.getId());
			}
			ps.add(p);
		}
		result.put("success", true);
		WriterUtil.write(response, result.toString());
	}
	
	
	
}
