package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTakeItem;

public interface BizStockTakeItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockTakeItem record);

    int insertSelective(BizStockTakeItem record);

    BizStockTakeItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockTakeItem record);

    int updateByPrimaryKey(BizStockTakeItem record);

	List<Map<String, Object>> queryItemListOnPaging(Map<String, Object> paramMap);

	List<Map<String, Object>> getListByStockTakeId(Integer stockTakeId);

	void deleteByStockTakeId(Integer stockTakeId);

	void insertItemList(List<Map<String, Object>> insertItemList);
}