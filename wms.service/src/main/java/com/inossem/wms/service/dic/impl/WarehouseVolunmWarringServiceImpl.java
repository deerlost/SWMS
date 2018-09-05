package com.inossem.wms.service.dic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.service.dic.IWarehouseVolunmWarringService;
@Service
public class WarehouseVolunmWarringServiceImpl implements IWarehouseVolunmWarringService{
    @Autowired
    private DicWarehouseAreaMapper  dicWarehouseAreaMapper;
    
    
	@Override
	public List<Map<String, Object>> selectWareHouseWarring(Map<String, Object> param) {	
				
		return dicWarehouseAreaMapper.selectWareHouseWarring(param);
	}
	
	@Override
	public List<Map<String, Object>> selectWareHouseWarringForPortal(Map<String, Object> param) {					
		return dicWarehouseAreaMapper.selectWareHouseWarringForPortal(param);
	}

	@Override
	public List<Map<String, Object>> selectWareHouseWarringByAreaId(int area_id) {
		return dicWarehouseAreaMapper.selectWareHouseWarringByAreaId(area_id);
	}

}
