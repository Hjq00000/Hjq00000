package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.dao.DevicetypeMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Devicetype;

@Service
public class DeviceService {
	
	@Autowired
	DeviceMapper dm;
	
	@Autowired
	DevicetypeMapper dtm;
	//查询+分页
	public PageInfo<Device> findDevicePage(Device device, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Device> deviceList = dm.findAll(device);
		PageInfo<Device> pageInfo = new PageInfo<Device>(deviceList);
		return pageInfo;
	}
	//查询devicetype表
	public List<Devicetype> findDevicetype() {
		
		return dtm.findDevicetype();
	}
	//根据名称查询是否重复
	public Device findDevice(Device device) {
		return dm.findDevice(device);
	}

	public void add(Device device) {
		device.setDevicecreatime(new Date());
		dm.insert(device);
	}

	public java.util.List<Devicetype> findDevicetype2() {
		return dtm.findDevicetype2();
	}

	public void deleteUser(int parseInt) {
		dm.deleteByPrimaryKey(parseInt);
		
	}

	public List<Device> export() {
		return dm.findAll(null);
	}


}
