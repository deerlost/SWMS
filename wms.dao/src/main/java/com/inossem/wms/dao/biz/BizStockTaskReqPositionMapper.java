package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskReqPosition;

public interface BizStockTaskReqPositionMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizStockTaskReqPosition record);

    int insertSelective(BizStockTaskReqPosition record);

    BizStockTaskReqPosition selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizStockTaskReqPosition record);

    int updateByPrimaryKey(BizStockTaskReqPosition record);
    
    List<BizStockTaskReqPosition> selectByReqId(Integer stockTaskReqId);
    
    List<BizStockTaskReqPosition> selectByCondition(Map<String, Object> map);
    
    int updatePalletIdTOZeroByPalletId(Integer palletId);
    
    int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);
    

    int updateByPackageId(BizStockTaskReqPosition record);
    
}