package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.StockBatchVo;

import net.sf.json.JSONObject;

public interface IAllocService {
	public List<BizAllocateHeadVo> getAllocList(Map<String, Object> conditionMap);
	
	public List<DicStockLocationVo> getLocationsByCorpId(Integer corpId);

	List<StockBatchVo> getMaterials(Map<String, Object> map) throws Exception;

	Map<String, Object> createAllocateParams(CurrentUser cuser, JSONObject obj) throws Exception;

	Map<String, Object> save(Map<String, Object> param) throws Exception;

	boolean allocateDel(Integer allocateId) throws Exception;

	int deleteDBDByAllotId(Integer allocateId) throws Exception;

	List<BizAllocateHeadVo> getAllotListByCondition(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> getAllotItemsByCondition(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getAllotItemsOnCreateDelivery(Map<String, Object> map) throws Exception;

	Map<String, Object> saveDeliveryByAllotItemIds(BizAllocateDeliveryHead delivery, List<Integer> allotItemIds)
			throws Exception;

	BizAllocateDeliveryHead getDeliveryById(Integer allocateDeliveryId) throws Exception;

	boolean deleteDeliveryById(Integer allocateDeliveryId) throws Exception;

	List<Map<String, Object>> getDeliverieListByCondition(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> getDeliveryItemsByConditions(Map<String, Object> map) throws Exception;

	List<String> getDeliveryCodeByAllocateId(Integer allocateId) throws Exception;
}
