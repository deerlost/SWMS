package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.vo.BizStockOutputReturnHeadVo;

public interface BizStockOutputReturnHeadMapper {
    int deleteByPrimaryKey(Integer returnId);

    int insert(BizStockOutputReturnHead record);

    int insertSelective(BizStockOutputReturnHead record);

    BizStockOutputReturnHead selectByPrimaryKey(Integer returnId);
    
    BizStockOutputReturnHead selectByReturnId(Integer returnId);
    
    BizStockOutputReturnHead selectByReturnCode(String returnCode);

    int updateByPrimaryKeySelective(BizStockOutputReturnHead record);

    int updateByPrimaryKey(BizStockOutputReturnHead record);
    
    List<BizStockOutputReturnHeadVo> selectReturnHeadListOnPaging(BizStockOutputReturnHeadVo paramVo);
    //川维
    List<BizStockOutputReturnHeadVo> selectReturnHeadListForCWOnPaging(BizStockOutputReturnHeadVo paramVo);
    
    List<Map<String, Object>> selectMatreqReturnInfoByReturnId(Map<String, Object> param);
    
    Map<String, Object> selectCountForDelivery(String referReceiptCode);
}