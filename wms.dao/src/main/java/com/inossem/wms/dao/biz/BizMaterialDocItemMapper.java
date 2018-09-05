package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizMaterialDocItem;

public interface BizMaterialDocItemMapper {

	BizMaterialDocItem selectByPrimaryKey(Integer itemId);

	int insertSelective(BizMaterialDocItem record);

	int updateByPrimaryKeySelective(BizMaterialDocItem record);

	List<BizMaterialDocItem> selectByReferenceOrder(Map<String, Object> param);

	List<Map<String, Object>> selectDBRKItemByAllotIdAndDeliveryId(Map<String, Object> map);

	/**
	 * 出入库分页查询
	 * 
	 * @author 刘宇 2018.02.24
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectOutAndInStockOnPaging(Map<String, Object> map);

	List<BizMaterialDocItem> selectByItemIds(List<Integer> itemIds);
	
	//从物料凭证获取周转金额(入)
	List<Map<String, Object>> getInputAmount(Map<String, Object> map);
	//从物料凭证获取周转金额(出)
	List<Map<String, Object>> getOutputAmount(Map<String, Object> map);
}