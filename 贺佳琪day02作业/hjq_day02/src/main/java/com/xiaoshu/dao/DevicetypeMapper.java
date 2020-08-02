package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Devicetype;
import com.xiaoshu.entity.DevicetypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DevicetypeMapper extends BaseMapper<Devicetype> {

	List<Devicetype> findDevicetype();

	List<Devicetype> findDevicetype2();
    
}