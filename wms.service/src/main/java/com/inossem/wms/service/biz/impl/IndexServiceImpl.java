package com.inossem.wms.service.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.IndexMapper;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.service.biz.IIndexService;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IIndexService {

	@Autowired
	private IndexMapper indexDao;

	/**
	 * 获取已完成
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getCompletedNum(HashMap<String, Object> map) throws Exception {
		return indexDao.countCompletedBills(map);
	}

	/**
	 * 获取待审批
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getUnCompletedNum(HashMap<String, Object> map) throws Exception {
		ArrayList<HashMap<String, Object>> completedNum = indexDao.countCompletedBills(map);
		ArrayList<HashMap<String, Object>> allNum = indexDao.countBills(map);
		ArrayList<HashMap<String, Object>> unCompletedNum = new ArrayList<HashMap<String, Object>>();
		if (completedNum != null && allNum != null) {
			for (HashMap<String, Object> obj : completedNum) {
				for (HashMap<String, Object> allObj : allNum) {
					String type = (String) obj.get("type");
					String allType = (String) allObj.get("type");
					if (type.equals(allType)) {
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("type", type);
						item.put("countNum", (Long) allObj.get("countNum") - (Long) obj.get("countNum"));
						unCompletedNum.add(item);
					}
				}
			}
		}
		return unCompletedNum;
	}

	@Override
	public ArrayList<Resources> getCurrentUserResouses(List<Integer> role) throws Exception {

		return indexDao.selectCurrentUserResources(role);
	}

	/**
	 * 获取资源路径信息
	 */
	@Override
	public ArrayList<Resources> getCurrentUserShowResouses(HashMap<String, Object> map) throws Exception {
		
		return indexDao.selectCurrentUserResourcesChecked(map);
	}

	/**
	 * 根据ID获取资源信息
	 */
	@Override
	public Resources getResourceById(Integer resourcesId) throws Exception {
		
		return indexDao.selectResourceById(resourcesId);
	}

	/**
	 * 保存显示资源状态
	 */
	@Override
	public int insertCheckResources(ArrayList<HashMap<String, Object>> list,String userId,Byte type) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("type", type);
		indexDao.deleteCheckedResources(map);
		return indexDao.insertCheckedResources(list);
	}

}
