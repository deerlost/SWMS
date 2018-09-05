package com.inossem.wms.auth;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.inossem.wms.dao.auth.ResourcesMapper;
import com.inossem.wms.dao.auth.RoleMapper;
import com.inossem.wms.model.auth.MenuADK;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Role;  

public class RoleService {
	
	@Resource  
    private RoleMapper roleDao; 
	@Resource
    private ResourcesMapper resourcesDao;
	
	public ArrayList<Role> getRole() {
		ArrayList<Role> roleList = new ArrayList<Role>();
		roleList = this.roleDao.selectRole();
		return roleList;
	}
	
	public ArrayList<Resources> getResources(List<Integer> role){
		ArrayList<Resources> res = new ArrayList<Resources>();
		res = this.resourcesDao.selectResources(role);
		return res;
	}
	
	public ArrayList<Resources> selectAllResourcesToRoleSet(){
		ArrayList<Resources> res = new ArrayList<Resources>();
		res = this.resourcesDao.selectAllResourcesToRoleSet();
		return res;
	}
	
	public ArrayList<MenuADK> getResourcesToPortable(List<Integer> role){
		ArrayList<MenuADK> res = this.resourcesDao.selectResourcesToPortable(role);
		return res;
	}
}
