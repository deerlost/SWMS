package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskPositionCw;

public interface BizStockTaskPositionCwMapper {
    int deleteByPrimaryKey(Integer itemPositionId);
    
    int deleteByStockTaskId(Integer stockTaskId);

    int insert(BizStockTaskPositionCw record);

    int insertSelective(BizStockTaskPositionCw record);

    BizStockTaskPositionCw selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizStockTaskPositionCw record);

    int updateByPrimaryKey(BizStockTaskPositionCw record);
    
    int updateByStockTaskId(BizStockTaskPositionCw record);
    
     
    List<BizStockTaskPositionCw> selectByReferReceiptIdAndType(BizStockTaskPositionCw record);
    int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);
    
    List<Map<String, Object>> selectByTaskIdForPrint(int stockTaskId);
}