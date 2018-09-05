package com.inossem.wms.dao.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.inossem.wms.model.auth.Resources;

public interface IndexMapper {

	/**
	 * 查询完成单据
	 * @param userId
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> countCompletedBills(HashMap<String, Object> map) ;
	/**
	 * 获取所有单据数量
	 * @param userId
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> countBills(HashMap<String, Object> map) ;
	

	/**
	 * 查询当前登录人3级菜单
	 * @param role
	 * @return
	 */
	public ArrayList<Resources> selectCurrentUserResources(List<Integer> role) ;

	/**
	 * 查询当前登录人显示菜单
	 * @param role
	 * @return
	 */
	public ArrayList<Resources> selectCurrentUserResourcesChecked(HashMap<String, Object> map) ;
	
	public Resources selectResourceById(Integer resourcesId);
	
	public int insertCheckedResources(ArrayList<HashMap<String, Object>> list);

	public int deleteCheckedResources(HashMap<String, Object> map) ;
}
