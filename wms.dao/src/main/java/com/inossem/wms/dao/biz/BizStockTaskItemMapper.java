package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskItem;
import com.inossem.wms.model.vo.BizStockTaskItemVo;

public interface BizStockTaskItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockTaskItem record);

    int insertSelective(BizStockTaskItem record);

    BizStockTaskItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockTaskItem record);

    int updateByPrimaryKey(BizStockTaskItem record);
    
    int insertItemForPosition(Map<String, Object> map);
    
    int insertItemForPositionNew(BizStockTaskItem record);

    List<BizStockTaskItemVo> getItemsByStockTaskId(BizStockTaskItem bizStockTaskItem);

}