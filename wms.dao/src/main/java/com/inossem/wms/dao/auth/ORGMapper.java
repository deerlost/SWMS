package com.inossem.wms.dao.auth;

import java.util.ArrayList;

import com.inossem.wms.model.auth.ORG;

public interface ORGMapper {
	int deleteByPrimaryKey(String orgId);

	int insert(ORG record);

	int insertSelective(ORG record);

	ORG selectByPrimaryKey(String orgId);
	
	ArrayList<ORG> selectAllOrg();
	
	ORG selectParentORGByORGID(String orgId);

	int updateByPrimaryKeySelective(ORG record);

	int updateByPrimaryKey(ORG record);
	
	ArrayList<ORG> selectChildrenOrg(String parentOrgId);
	
	int selectChildrenOrgCount(String parentOrgId);

	
}