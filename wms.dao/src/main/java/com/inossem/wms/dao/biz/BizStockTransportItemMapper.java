package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportItem;
import com.inossem.wms.model.vo.BizStockTransportItemVo;

public interface BizStockTransportItemMapper {

	BizStockTransportItem selectByPrimaryKey(Map<String,Object> param);
	
	List<BizStockTransportItemVo> selectItemById(Map<String,Object> param);
	
    int insertSelective(BizStockTransportItem record);

    int updateByPrimaryKeySelective(BizStockTransportItem record);

}