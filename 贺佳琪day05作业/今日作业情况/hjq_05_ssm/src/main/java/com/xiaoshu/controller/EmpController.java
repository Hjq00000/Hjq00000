package com.xiaoshu.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.Emp;
import com.xiaoshu.entity.EmpVo;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.EmpService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("emp")
public class EmpController extends LogController{
	static Logger logger = Logger.getLogger(EmpController.class);

	@Autowired
	private EmpService es;
		
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("empIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
	
		List<Dept> dList = es.findDeptList();
		request.setAttribute("dList",dList);
		
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		return "emp";
	}
	
	
	@RequestMapping(value="empList",method=RequestMethod.POST)
	public void userList(EmpVo emp,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<EmpVo> empList= es.findEmpPage(emp,pageNum,pageSize);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",empList.getTotal() );
			jsonObj.put("rows", empList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveEmp")
	public void reserveUser(MultipartFile picFile, Emp emp,HttpServletRequest request,User user,HttpServletResponse response) throws Exception{
		Integer eId = emp.geteId();
		JSONObject result=new JSONObject();
		
		if(picFile!=null && picFile.getSize()>0){
			//获取图片名称xxx.jpg
			String filename = picFile.getOriginalFilename();
			//获取后缀名称.jpg
			String suffix = filename.substring(filename.lastIndexOf("."));
			//重新命名123455.jpg
			String newFileName = System.currentTimeMillis()+suffix;
			//设置虚拟路径
			File file = new File("e:/pic/"+newFileName);
			//上传图片
			picFile.transferTo(file);
			//将新图片名称设置到数据表中
			emp.setPic(newFileName);
		}
		
		try {
			if (eId != null) {   // userId不为空 说明是修改
				Emp userName = es.existUserWithUserName(emp.geteName());
//				if(userName != null && userName.geteId().compareTo(eId)==0){
//					emp.seteId(eId);
					es.updateEmp(emp);
					result.put("success", true);
//			}else{
//					result.put("success", true);
//					result.put("errorMsg", "该用户名被使用");
//				}
				
			}else {   // 添加
				if(es.existUserWithUserName(emp.geteName())==null){  // 没有重复可以添加
					es.addEmp(emp);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteEmp")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				es.deleteEmp(Integer.parseInt(id));
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
