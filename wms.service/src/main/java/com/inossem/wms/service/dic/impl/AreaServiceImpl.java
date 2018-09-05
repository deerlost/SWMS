package com.inossem.wms.service.dic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.service.dic.IAreaService;

/**
 * 储存区
 * 
 * @author 刘宇 2018.03.01
 *
 */
@Transactional
@Service("areaService")
public class AreaServiceImpl implements IAreaService {
	@Autowired
	private DicWarehouseAreaMapper dicWarehouseAreaDao;

	@Override
	public List<DicWarehouseArea> listAreaIdAndAreaCodeAndAreaName() throws Exception {

		return dicWarehouseAreaDao.selectAllAreaIdAndAreaCodeAndAreaName();
	}

}
