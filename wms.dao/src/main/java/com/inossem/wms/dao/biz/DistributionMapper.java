package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

public interface DistributionMapper {
	//按条件查询所有配货单
	public List<T> selectCargo(Map<String,String> paramMap);
}
