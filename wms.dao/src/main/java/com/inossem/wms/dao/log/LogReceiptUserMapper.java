package com.inossem.wms.dao.log;

import java.util.ArrayList;

import com.inossem.wms.model.log.LogReceiptUser;

public interface LogReceiptUserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(LogReceiptUser record);

	int insertSelective(LogReceiptUser record);

	LogReceiptUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LogReceiptUser record);

	int updateByPrimaryKey(LogReceiptUser record);

	/**
	 * 查询所有
	 * 
	 * @author 刘宇 2018.03.29
	 * @return
	 */
	ArrayList<LogReceiptUser> selectAllLogReceiptUser();
}