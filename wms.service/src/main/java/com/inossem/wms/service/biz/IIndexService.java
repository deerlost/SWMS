package com.inossem.wms.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.inossem.wms.model.auth.Resources;



public interface IIndexService {

	/**
	 * 获取已完成数量
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, Object>> getCompletedNum(HashMap<String, Object> map) throws Exception;

	/**
	 * 获取总数
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, Object>> getUnCompletedNum(HashMap<String, Object> map) throws Exception;

	/**
	 * 获取当前登录人3级菜单
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Resources> getCurrentUserResouses(List<Integer> role) throws Exception;

	/**
	 * 获取资源路径信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Resources> getCurrentUserShowResouses(HashMap<String, Object> map) throws Exception;

	/**
	 * 根据ID获取资源信息
	 * @param resourcesId
	 * @return
	 * @throws Exception
	 */
	public Resources getResourceById(Integer resourcesId) throws Exception;
	
	/**
	 * 保存要显示资源状态
	 * @param list
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public int insertCheckResources(ArrayList<HashMap<String, Object>> list ,String userId,Byte type)throws Exception ;
}
