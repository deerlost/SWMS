package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicUnit;

public interface DicUnitMapper {
	int deleteByPrimaryKey(Integer unitId);

	int insert(DicUnit record);

	int insertSelective(DicUnit record);

	DicUnit selectByPrimaryKey(Integer unitId);

	int updateByPrimaryKeySelective(DicUnit record);

	int updateByPrimaryKey(DicUnit record);

	List<DicUnit> selectAll();
	
	List<String> selectAllCode();
	
	DicUnit	getByCode(String unitCode);
}