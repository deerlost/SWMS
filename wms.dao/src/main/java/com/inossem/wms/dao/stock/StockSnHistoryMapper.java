package com.inossem.wms.dao.stock;

import com.inossem.wms.model.stock.StockSnHistory;

public interface StockSnHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StockSnHistory record);

    int insertSelective(StockSnHistory record);

    StockSnHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockSnHistory record);

    int updateByPrimaryKey(StockSnHistory record);
}