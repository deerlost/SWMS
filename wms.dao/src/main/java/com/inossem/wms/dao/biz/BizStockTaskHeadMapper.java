package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockTaskHead;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;

public interface BizStockTaskHeadMapper {
    int deleteByPrimaryKey(Integer stockTaskId);

    int insert(BizStockTaskHead record);

    int insertSelective(BizStockTaskHead record);

    BizStockTaskHead selectByPrimaryKey(Integer stockTaskId);

    int updateByPrimaryKeySelective(BizStockTaskHead record);

    int updateByPrimaryKey(BizStockTaskHead record);
    
    List<BizStockTaskHeadVo> getBizStockTaskHeadListOnPaging (BizStockTaskHeadVo record) ;
    
    List<BizStockTaskHeadVo> getBizStockTaskHeadListInReq(BizStockTaskHeadVo record);
    
    String getReferReceiptCodeInReq (Integer stockTaskId);
}