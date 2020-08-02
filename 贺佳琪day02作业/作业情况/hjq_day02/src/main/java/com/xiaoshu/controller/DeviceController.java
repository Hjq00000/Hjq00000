package com.xiaoshu.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Devicetype;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.service.DeviceService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("device")
public class DeviceController {
	
	@Autowired
	DeviceService ds;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("deviceIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Devicetype> dtList = ds.findDevicetype();
		List<Devicetype> List = ds.findDevicetype2();
		System.out.println(dtList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("dtList", dtList);
		request.setAttribute("List", List);
		return "device";
	}
	
	
	@RequestMapping(value="deviceList",method=RequestMethod.POST)
	public void userList(Device device,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Device> userList= ds.findDevicePage(device, pageNum, pageSize);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("reserveUser")
	public void reserveUser(HttpServletRequest request,Device device,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			Device d = ds.findDevice(device);
			// 添加
			if (d != null) { // 没有重复可以添加
				result.put("success", true);
				result.put("errorMsg", "该用户名被使用");
			} else {
				ds.add(device);
				result.put("success", true);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("deleteUser")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				ds.deleteUser(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("exp")
	public void exp(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		String[] head = {"编号","设备名称","设备类型名称","内存","机身颜色","价格","设备状态","创建时间"};
		HSSFRow frow = sheet.createRow(0);
		for (int i = 0; i < head.length; i++) {
			frow.createCell(i).setCellValue(head[i]);
		}
		List<Device> dList = ds.export();
		
		for (int i = 0; i < dList.size(); i++) {
			Device d = dList.get(i);
			HSSFRow r = sheet.createRow(i+1);
			r.createCell(0).setCellValue(d.getDeviceid());
			r.createCell(1).setCellValue(d.getDevicename());
			r.createCell(2).setCellValue(d.getDevicetype().getTypename());
			r.createCell(3).setCellValue(d.getDeviceram());
			r.createCell(4).setCellValue(d.getColor());
			r.createCell(5).setCellValue(d.getDeviceid());
			r.createCell(6).setCellValue(d.getStatus());
			r.createCell(7).setCellValue(TimeUtil.formatTime(d.getDevicecreatime(), "yyyy-MM-dd HH:mm:ss"));
		}
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("设备管理列表.xls", "UTF-8"));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/octet-stream");
        wb.write(response.getOutputStream());
		wb.close();
	}

}
