package com.inossem.wms.dao.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.Role;

public interface RoleMapper {
	int insert(Role record);

	int insertSelective(Role record);

	/**
	 * 查询所有权限
	 * 
	 * @param record
	 * @return
	 */
	List<Map<String, Object>> selectRoleListOnPaging(HashMap<String, Object> paramMap);

	// 查询所有角色(用户管理)
	ArrayList<Role> selectAllRoles();

	// 通过user_id查询已有role(用户管理)
	ArrayList<Role> selectRoleByUserId(String userId);

	// 查询角色总共行数
	int selectRoleCount(HashMap<String, Object> paramMap);

	ArrayList<Role> selectRole();

	Role selectRoleByRoleId(Integer roleId);

	ArrayList<Role> selectRoleByRoleName(String roleName);

	int updateRole(Role record);

}