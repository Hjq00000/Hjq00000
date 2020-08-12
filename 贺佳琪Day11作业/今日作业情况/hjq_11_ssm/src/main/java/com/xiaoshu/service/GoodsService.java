package com.xiaoshu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodstypeMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.GoodsVo;
import com.xiaoshu.entity.Goodstype;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class GoodsService {

	@Autowired
	GoodsMapper gm;


	// 新增
	public void addGoods(Goods t) throws Exception {
		t.setCreatetime(new Date());
		gm.insert(t);
	};

	// 修改
	public void updateGoods(Goods t) throws Exception {
		gm.updateByPrimaryKeySelective(t);
	};
//
//	// 删除
//	public void deleteUser(Integer id) throws Exception {
//		userMapper.deleteByPrimaryKey(id);
//	};


	public PageInfo<GoodsVo> findGoodsPage(GoodsVo goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<GoodsVo> goodsList = gm.findPage(goods);
		PageInfo<GoodsVo> pageInfo = new PageInfo<GoodsVo>(goodsList);
		return pageInfo;
	}

	@Autowired
	private GoodstypeMapper tm;
	public List<Goodstype> findGoodstype() {
		// TODO Auto-generated method stub
		return tm.selectAll();
	}

	public List<GoodsVo> findExcel(GoodsVo goods) {
		// TODO Auto-generated method stub
		return gm.findPage(goods);
	}

	public List<GoodsVo> findEcharts() {
		// TODO Auto-generated method stub
		return gm.findEcharts();
	}


}
