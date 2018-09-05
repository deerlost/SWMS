package com.inossem.wms.dao.stock;

import java.util.Map;

import com.inossem.wms.model.stock.StockBatchBegin;

public interface StockBatchBeginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StockBatchBegin record);

    int insertSelective(StockBatchBegin record);

    StockBatchBegin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockBatchBegin record);

    int updateByPrimaryKey(StockBatchBegin record);
    
    Map<String, Object> selectByParam(Map<String, Object> map);
    
    Map<String, Object> selectCurrentStockByParam(Map<String, Object> map);
    
}