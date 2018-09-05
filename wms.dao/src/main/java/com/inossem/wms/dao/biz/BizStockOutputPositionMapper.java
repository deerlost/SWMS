package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizStockOutputPosition;
import com.inossem.wms.model.vo.BizStockOutputPositionVo;

import java.util.List;
import java.util.Map;

public interface BizStockOutputPositionMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizStockOutputPosition record);

    int insertSelective(BizStockOutputPosition record);

    BizStockOutputPosition selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizStockOutputPosition record);

    int updateByPrimaryKey(BizStockOutputPosition record);

    int deletePostionByOrderId(Map<String, Object> param);

    List<BizStockOutputPositionVo> selectPostionByOrderId(Map<String, Object> param);
    
    int saveMatDocInfo(Map<String,Object> param);
}