package com.inossem.wms.service.dic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.service.dic.IDicClassTypeService;
    @Transactional
	@Service("dicClassTypeService")
public class DicClassTypeServiceImpl implements IDicClassTypeService{
    @Autowired
    private DicClassTypeMapper dicClassTypeMapper;
	public List<Map<String,Object>> selectAll() {	
		return dicClassTypeMapper.selectAll();
	}

}
