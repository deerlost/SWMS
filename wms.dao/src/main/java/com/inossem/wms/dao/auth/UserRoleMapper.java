package com.inossem.wms.dao.auth;

import com.inossem.wms.model.auth.UserRole;

public interface UserRoleMapper {
	int deleteByPrimaryKey(Integer userMapId);

	int insert(UserRole record);

	int insertSelective(UserRole record);

	UserRole selectByPrimaryKey(Integer userMapId);

	int updateByPrimaryKeySelective(UserRole record);

	int updateByPrimaryKey(UserRole record);

	int deleteByUserId(String userId);
}