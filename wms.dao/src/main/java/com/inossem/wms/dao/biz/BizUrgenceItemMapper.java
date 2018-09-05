package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizUrgenceItem;

public interface BizUrgenceItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizUrgenceItem record);

    int insertSelective(BizUrgenceItem record);

    BizUrgenceItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizUrgenceItem record);

    int updateByPrimaryKey(BizUrgenceItem record);
    
    int deleteByUrgenceId(Integer urgenceId);
    
    int deleteItem(BizUrgenceItem record);
    
    int updateByUrgenceId(BizUrgenceItem record);
}