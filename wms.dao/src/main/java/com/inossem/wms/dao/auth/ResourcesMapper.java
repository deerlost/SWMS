package com.inossem.wms.dao.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.inossem.wms.model.auth.MenuADK;
import com.inossem.wms.model.auth.Resources;

public interface ResourcesMapper {
    int insert(Resources record);

    int insertSelective(Resources record);
    
    /**
     * 根据权限查询可用URL
     * @param record
     * @return
     */
    ArrayList<Resources> selectResources(List<Integer> role);
    /**
     * 根据权限查询可用URL(安卓)
     * @param record
     * @return
     */
    ArrayList<MenuADK> selectResourcesToPortable(List<Integer> role);
    
   	int   updateByPrimaryKey(Resources record);
    
    //查询所有resources(角色管理)    
    ArrayList<Resources> selectAllResources();
    
    ArrayList<Resources> selectAllResourcesHaveUrl();
    
    //根据roleId查询已有resources(角色管理)
    ArrayList<Resources> selectResourcesByRoleId(String roleId);
    
    //条件查询权限列表
    ArrayList<Resources> selectResourcesList(HashMap<String, Object> paramMap);
    
    
  //条件查询权限列表总行数
    int selectListCount(HashMap<String, Object> paramMap);
    
    ArrayList<Resources> selectAllResourcesToRoleSet();
    
}