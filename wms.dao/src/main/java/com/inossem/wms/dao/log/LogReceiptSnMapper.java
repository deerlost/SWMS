package com.inossem.wms.dao.log;

import com.inossem.wms.model.log.LogReceiptSn;

public interface LogReceiptSnMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(LogReceiptSn record);

	int insertSelective(LogReceiptSn record);

	LogReceiptSn selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LogReceiptSn record);

	int updateByPrimaryKey(LogReceiptSn record);
}