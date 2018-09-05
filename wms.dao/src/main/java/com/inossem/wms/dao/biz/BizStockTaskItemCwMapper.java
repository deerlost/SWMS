package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.vo.BizStockTaskItemVo;

public interface BizStockTaskItemCwMapper {
    int deleteByPrimaryKey(Integer itemId);
    
    int deleteByStockTaskId(Integer stockTaskId);

    int insert(BizStockTaskItemCw record);

    int insertSelective(BizStockTaskItemCw record);

    BizStockTaskItemCw selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockTaskItemCw record);
    
    int updateByStockTaskId(BizStockTaskItemCw record);

    int updateByPrimaryKey(BizStockTaskItemCw record);
    
    List<BizStockTaskItemCw> selectByReferReceiptIdAndType(BizStockTaskItemCw record);
    
    List<BizStockTaskItemVo> getItemsByStockTaskId(BizStockTaskItemCw bizStockTaskItem);
    
    List<Map<String, Object>> getItemsByStockTaskIdForWarehouse(BizStockTaskItemCw bizStockTaskItem);

    Map<String, Object> selectHaveShelvesQty(Map<String, Object> map);
    
    Byte selectMaxStatus(BizStockTaskItemCw record);
    
    int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);
    
    List<BizStockTaskItemCw> selectByDate(Map<String, Object> map);
    
}