package com.inossem.wms.service.auth.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inossem.wms.dao.auth.ResourcesMapper;
import com.inossem.wms.dao.auth.ResourcesRoleMapper;
import com.inossem.wms.dao.auth.RoleMapper;
import com.inossem.wms.dao.auth.UserRoleMapper;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.ResourcesRole;
import com.inossem.wms.model.auth.Role;
import com.inossem.wms.service.auth.IRoleService;

@Service("role_Service")
public class RoleServiceImpl implements IRoleService {
	@Resource
	private RoleMapper roleDao;
	@Resource
	private ResourcesMapper resourcesDao;
	@Resource
	private ResourcesRoleMapper resourcesRoleDao;
	@Resource
	private UserRoleMapper userRoleDao;

	@Override
	public int addRole(Role role) {
		// TODO Auto-generated method stub
		return roleDao.insertSelective(role);
	}

	@Override
	public int modifyRole(Role role) {

		return roleDao.updateRole(role);
	}

	// 通过role_id查询resources——角色管理
	@Override
	public ArrayList<Resources> selectResourcesById(String roleId) {

		return this.resourcesDao.selectResourcesByRoleId(roleId);
	}

	// 获得所有权限resources——角色管理
	@Override
	public ArrayList<Resources> selectAllResources() {
		return this.resourcesDao.selectAllResources();
	}

	// 获得所有role——用户管理
	@Override
	public ArrayList<Role> selectAllRole() {

		return this.roleDao.selectAllRoles();
	}

	// 通过user_id查询role——用户管理
	@Override
	public ArrayList<Role> selectRoleByUserId(String userId) {

		return this.roleDao.selectRoleByUserId(userId);
	}

	@Override
	public ArrayList<Role> selectRole() {
		// TODO Auto-generated method stub
		return roleDao.selectRole();
	}

	@Override
	public int addResourcesRole(ResourcesRole resourcesRole) throws Exception {
		// TODO Auto-generated method stub
		return resourcesRoleDao.insert(resourcesRole);
	}

	@Override
	public Role selectRoleByRoleId(Integer roleId) throws Exception {
		// TODO Auto-generated method stub
		return roleDao.selectRoleByRoleId(roleId);
	}

	@Override
	public int deleteByRoleId(int roleId) throws Exception {

		return resourcesRoleDao.deleteByRoleId(roleId);
	}

	@Override
	public int insertResourcesRole(ResourcesRole record) {

		return resourcesRoleDao.insertSelective(record);
	}

	@Override
	public ArrayList<Role> selectRoleByRoleName(String roleName) {

		return roleDao.selectRoleByRoleName(roleName);
	}

	@Override
	public ArrayList<Resources> selectAllResourcesHaveUrl() {
		// TODO Auto-generated method stub
		return resourcesDao.selectAllResourcesHaveUrl();
	}

	@Override
	public List<Map<String, Object>> selectRoleList(HashMap<String, Object> paramMap) {

		return roleDao.selectRoleListOnPaging(paramMap);
	}

	@Override
	public int deleteByUserId(String userId) throws Exception {
		// TODO Auto-generated method stub
		return userRoleDao.deleteByUserId(userId);
	}

}
