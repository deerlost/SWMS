package com.inossem.wms.dao.auth;

import java.util.ArrayList;

import com.inossem.wms.model.auth.ResourcesRole;

public interface ResourcesRoleMapper {
    int deleteByPrimaryKey(Integer resourceMapId);

    int insert(ResourcesRole record);

    int insertSelective(ResourcesRole record);

    ResourcesRole selectByPrimaryKey(Integer resourceMapId);

    int updateByPrimaryKeySelective(ResourcesRole record);

    int updateByPrimaryKey(ResourcesRole record);
    
    int deleteByRoleId(int roleId);
    
    ArrayList<ResourcesRole> selectResourcesRoleByRoleId(Integer roleId);
}