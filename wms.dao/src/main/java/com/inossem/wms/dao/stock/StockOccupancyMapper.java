package com.inossem.wms.dao.stock;

import com.inossem.wms.model.stock.StockOccupancy;

public interface StockOccupancyMapper {
	
	StockOccupancy selectByUniqueKey(StockOccupancy stockOccupancy);
	
	int deleteByByUniqueKey(StockOccupancy stockOccupancy);

	int updateQty(StockOccupancy stockOccupancy);
	
    int insertSelective(StockOccupancy record);
    
}