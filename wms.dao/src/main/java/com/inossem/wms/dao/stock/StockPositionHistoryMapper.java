package com.inossem.wms.dao.stock;

import com.inossem.wms.model.stock.StockPositionHistory;

public interface StockPositionHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StockPositionHistory record);

    int insertSelective(StockPositionHistory record);

    StockPositionHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockPositionHistory record);

    int updateByPrimaryKey(StockPositionHistory record);
}