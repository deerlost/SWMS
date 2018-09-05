package com.inossem.wms.dao.stock;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.key.StockSnKey;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.vo.StockSnExpandVo;

public interface StockSnMapper {
	int deleteByPrimaryKey(Integer id);

	int deleteByStockId(Integer stockId);

	int insert(StockSn record);

	int insertSelective(StockSn record);

	StockSn selectByPrimaryKey(Integer id);

	StockSn selectByUniqueKey(StockSnKey key);

	List<StockSn> selectListByUniqueKey(StockSnKey key);
	
	int updateByPrimaryKeySelective(StockSn record);

	int updateByPrimaryKey(StockSn record);

	int updateByStockQty(StockSn record);

	List<StockSn> queryStockSnListByStockId(Integer stockId);

	void batchUpdateSnByPrimaryKey(@Param("list") List<StockSn> needUpdateDataList);

	void batchUpdateSnQtyByPrimaryKey(@Param("list") List<StockSn> needUpdateDataList);

	int deleteBySourcePrimaryKey(Integer stockId);
    
    StockSn selectByThreeKey(StockSnExpandVo stockSnExpandVo);
    
    
    int updateSourceSnByPrimaryKey(StockSn record);
    
    int insertSnSelective(StockSnExpandVo record);
}