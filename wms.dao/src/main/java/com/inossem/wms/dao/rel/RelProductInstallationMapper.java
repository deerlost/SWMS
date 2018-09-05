package com.inossem.wms.dao.rel;

import com.inossem.wms.model.rel.RelProductInstallation;

public interface RelProductInstallationMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(RelProductInstallation record);

	int insertSelective(RelProductInstallation record);

	RelProductInstallation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RelProductInstallation record);

	int updateByPrimaryKey(RelProductInstallation record);
	
	int updateIsDeleteAll();
	
}