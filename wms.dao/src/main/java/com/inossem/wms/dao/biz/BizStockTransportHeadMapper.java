package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTransportHead;
import com.inossem.wms.model.vo.BizStockTransportHeadVo;

public interface BizStockTransportHeadMapper {

    BizStockTransportHead selectByPrimaryKey(Map<String,Object> param);

    BizStockTransportHeadVo selectHeadById(Map<String,Object> param);
    
    List<Map<String,Object>> selectOrderListOnPaging(Map<String,Object> param);
    
    int insertSelective(BizStockTransportHead record);
    
    int updateByPrimaryKeySelective(BizStockTransportHead record);
    
    List<Map<String,Object>> selectMoveType();
    
}