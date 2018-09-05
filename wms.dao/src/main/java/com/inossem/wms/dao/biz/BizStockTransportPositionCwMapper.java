package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.biz.BizStockTransportPositionCw;

public interface BizStockTransportPositionCwMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizStockTransportPositionCw record);

    int insertSelective(BizStockTransportPositionCw record);

    BizStockTransportPositionCw selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizStockTransportPositionCw record);

    int updateByPrimaryKey(BizStockTransportPositionCw record);
    
    List<Map<String, Object>> selectByParams(Map<String, Object> map);
    
    List<BizStockTransportPositionCw> selectVoByParams(Map<String, Object> map);
    int updateDeleteByPrimaryKey(BizStockTransportPositionCw record);
}