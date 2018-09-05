package com.inossem.wms.service.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.ResourcesRole;
import com.inossem.wms.model.auth.Role;

public interface IRoleService {
	
	//条件查询角色列表 (角色管理列表页)
	List<Map<String, Object>> selectRoleList(HashMap<String, Object> paramMap);
	
	// 查询角色的权限——角色管理
	ArrayList<Resources> selectResourcesById(String roleId);

	// 查询所有权限——角色管理
	ArrayList<Resources> selectAllResources();

	// 查询所有权限——角色管理(带url)
	ArrayList<Resources> selectAllResourcesHaveUrl();

	public int addRole(Role role);

	public int modifyRole(Role role);

	// 查询所有角色——用户管理
	ArrayList<Role> selectAllRole();

	// 条件查询该用户已有角色——用户管理
	ArrayList<Role> selectRoleByUserId(String userId);

	ArrayList<Role> selectRole();

	// 为角色添加权限
	int addResourcesRole(ResourcesRole resourcesRole) throws Exception;

	//Role selectRoleByRoleId(int roleId) throws Exception;

	int deleteByRoleId(int roleId) throws Exception;

	// 向角色权限表中插入数据
	int insertResourcesRole(ResourcesRole record);

	ArrayList<Role> selectRoleByRoleName(String roleName);

	// 删除角色权限表中的数据
	int deleteByUserId(String userId) throws Exception;

	Role selectRoleByRoleId(Integer roleId) throws Exception;

}
