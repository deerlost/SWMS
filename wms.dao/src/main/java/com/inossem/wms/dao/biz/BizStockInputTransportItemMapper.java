package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizStockInputTransportItem;

public interface BizStockInputTransportItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockInputTransportItem record);

    int insertSelective(BizStockInputTransportItem record);

    BizStockInputTransportItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockInputTransportItem record);

    int updateByPrimaryKey(BizStockInputTransportItem record);

	List<Map<String, Object>> selectByStockTransportId(Integer stock_input_id);
	
	List<Map<String, Object>> selectByStockInputTransportId(@Param("stockTransportId") Integer stockTransportId);
	
	List<Map<String, Object>> queryTransStockOutAndInOnPaging( Map<String, Object> param);
	
	Map<String, Object> queryPrice(Map<String, Object> param);

	
}