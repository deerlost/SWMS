package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockOutputReturnPosition;

public interface BizStockOutputReturnPositionMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizStockOutputReturnPosition record);

    int insertSelective(BizStockOutputReturnPosition record);

    BizStockOutputReturnPosition selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizStockOutputReturnPosition record);

    int updateByPrimaryKey(BizStockOutputReturnPosition record);
    
    List<Map<String, Object>> selectPositionBatchInfo(Map<String, Object> map);
    
    List<BizStockOutputReturnPosition> selectPositionListByIdAndRid(BizStockOutputReturnPosition record);
    
    int updatePositionsToDelete(Integer returnId);
    
}